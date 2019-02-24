/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomous;
import frc.robot.autonomous.Waypoint;
/**
 * Add your docs here.
 */
public class PathFinder {

    private Waypoint[] waypoints;
    public PathType pathType;

    public Spline[] splines; 
    public enum PathType{
        CUBIC_HERMITE,
        QUINTIC_HERMITE,
        B_SPLINE;
    }

    public class Spline{//technically like a pair of splines and not a spline, i'm a liar forgive me for my sins
        double[] xcoef = new double[6];//pos 0 is c_0x^0, pos 1 is c_1x^1, etc... c_5x^5. Cubic -> c_5, c_4=0
        double[] ycoef = new double[6];
        double[] xprimecoef = new double[5];
        double[] yprimecoef = new double[5];
        double[] xdoubleprimecoef = new double[4];
        double[] ydoubleprimecoef = new double[4];

        double arcLength;
        double knot_Distance;

        public Spline()
        {
        }

        private void setArcLength()
        {
            int NUM_STEPS=10000;
            double ds=1/NUM_STEPS;
            arcLength=0;
            double integrand=0;
            double s=0;
            for(int i=1; i<=NUM_STEPS; i++)
            {
                s=i*ds;
                integrand=Math.sqrt(Math.pow(evaluateFunction(xprimecoef, s), 2)+ Math.pow(evaluateFunction(yprimecoef, s), 2));
                arcLength+=integrand;
            }
        }

        private double evaluateFunction(double[] coefficients, double s)
        {
            double value=0;
            for(int i=0; i<coefficients.length; i++)
                value+=Math.pow(s,i)*coefficients[i];
            return value;
        }
    }

    public PathFinder(Waypoint[] waypoints, PathType pathType)
    {
        copy(waypoints);
        pathType=this.pathType;
        generateAllSplines();
    }

    private void copy(Waypoint[] waypoints)
    {
        waypoints = new Waypoint[this.waypoints.length];
        for(int i=0; i<this.waypoints.length; i++)
            waypoints[i] = new Waypoint(this.waypoints[i].x, this.waypoints[i].y, this.waypoints[i].deg);
    }

    private boolean generateAllSplines()
    {
        splines = new Spline[waypoints.length-1];
        for(int i=0; i<splines.length; i++)
        {
            splines[i] = new Spline();
            splines[i]=generateASpline(waypoints[i], waypoints[i+1], splines[i]);
        }
        return true;
    }

    private Spline generateASpline(Waypoint start, Waypoint end, Spline temp)
    {
        return generateASpline(start.x, start.y, start.deg, end.x, end.y, end.deg, temp);
    }

    private Spline generateASpline(double x0, double y0, double theta0, double x1, double y1, double theta1, Spline temp)
    {
        temp.knot_Distance = Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0));
        
        double cc=temp.knot_Distance*1.1;//tangent vector magnitude, Matlab investigates this a little more.
        //tangent magnitude doesnt "mean" much. Matlab experimentation shows different values and why they're chosen
        double a0x=0;//Second Derivative variable, there are only so many letters of the alphabet.
        double m0x=cc*Math.cos(SabaMath.d2r(theta0));

        double a0y=0;
        double m0y=cc*Math.sin(SabaMath.d2r(theta0));

        double k0 = Math.abs(m0x*a0y-m0y*a0x)/Math.pow(m0x*m0x+m0y*m0y,1.5);//for ease of change in future, this is 0 anyway

        double a1x=0;
        double m1x = cc*Math.cos(SabaMath.d2r(theta1));

        double a1y=0;
        double m1y=cc*Math.sin(SabaMath.d2r(theta1));

        double k1 = Math.abs(m1x*a1y-m1y*a1x)/Math.pow(m1x*m1x+m1y*m1y, 1.5);

        if (pathType==PathType.CUBIC_HERMITE)//Equations come from my heart and soul, and totally not basis functions
        {
            temp.xcoef[5]=0;
            temp.xcoef[4]=0;
            temp.xcoef[3]=-2*x1+2*x0+m0x+m1x;
            temp.xcoef[2]=-3*x0-2*m0x+3*x1-m1x;
            temp.xcoef[1]=m0x;
            temp.xcoef[0]=x0;

            temp.ycoef[5]=0;
            temp.ycoef[4]=0;
            temp.ycoef[3]=-2*y1+2*y0+m0y+m1y;
            temp.ycoef[2]=-3*y0-2*m0y+3*y1-m1y;
            temp.ycoef[1]=m0y;
            temp.ycoef[0]=y0;
        }
        if(pathType==PathType.QUINTIC_HERMITE)//Curvature is 0, so splines can be line-ish near the end. :) if this can change, we'll figure out later
        {
            temp.xcoef[5]=a1x/2-a0x/2-3*m1x-3*m0x+6*x1-6*x0;
            temp.xcoef[4]=-a1x+1.5*a0x+7*m1x+8*m0x-15*x1+15*x0;
            temp.xcoef[3]=a1x/2-1.5*a0x-4*m1x-6*m0x+10*x1-10*x0;
            temp.xcoef[2]=a0x/2;
            temp.xcoef[1]=m0x;
            temp.xcoef[0]=x0;

            temp.ycoef[5]=a1y/2-a0y/2-3*m1y-3*m0y+6*y1-6*y0;
            temp.ycoef[4]=-a1y+1.5*a0y+7*m1y+8*m0y-15*y1+15*y0;
            temp.ycoef[3]=a1y/2-1.5*a0y-4*m1y-6*m0y+10*y1-10*y0;
            temp.ycoef[2]=a0y/2;
            temp.ycoef[1]=m0y;
            temp.ycoef[0]=y0;
        }
        initializeSpline(temp);
        return temp;
    }

    private void initializeSpline(Spline temp)
    {
        temp.xprimecoef = SabaMath.takeDerivative(temp.xcoef);
        temp.xdoubleprimecoef = SabaMath.takeDerivative(temp.xprimecoef);

        temp.yprimecoef = SabaMath.takeDerivative(temp.ycoef);
        temp.ydoubleprimecoef = SabaMath.takeDerivative(temp.yprimecoef);
        //knot distance was done in generating spline method :)
        temp.setArcLength();
    }
}
