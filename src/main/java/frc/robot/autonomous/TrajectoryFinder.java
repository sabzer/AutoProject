/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomous;

public class TrajectoryFinder {//deceleration is NEGATIVE* remember that pls
    MotionProfile profile;
    PathFinder pathfinder;

    double totalDistance;
    double acc;
    double dec;
    double v_cruise;
    double v_start;
    double v_end;

    double t_acc;
    double d_acc;
    double t_cruise;
    double d_cruise;
    double t_dec;
    double d_dec;

    MotionPoint[] trying;
    double LOOP_HERTZ;

    public enum MotionProfile{
        TRIANGULAR,
        TRAPEZOIDAL,
        S_CURVE;//this isn't programmed in, idk how to do that. Using trapezoidal most often
    }

    public TrajectoryFinder(MotionProfile profile, PathFinder.PathType pathtype, Waypoint[] waypoint, double acc, double v_cruise, double dec, double v_start, double v_end)
    {
        this.pathfinder = new PathFinder(waypoint, pathtype);
        this.profile = profile;
        this.acc=acc;
        this.v_cruise=v_cruise;
        this.dec=dec;
        this.v_start=v_start;
        this.v_end=v_end;
        initializeTraj();
    }

    private void initializeTraj()
    {
        for(int i=0; i<pathfinder.splines.length; i++) totalDistance+=pathfinder.splines[i].arcLength;
        double vtemp=Math.sqrt((totalDistance+v_start*v_start*0.5/acc-v_end*v_end*0.5/dec)/(0.5/acc-0.5/dec));
        if(v_cruise>vtemp) 
        {
            v_cruise=vtemp;
            profile=MotionProfile.TRIANGULAR;
        }

        d_acc=(v_cruise*v_cruise-v_start*v_start)/2/acc;
        t_acc=(v_cruise-v_start)/acc;

        d_dec=(v_end*v_end-v_cruise*v_cruise)/2/dec;
        t_dec=(v_end-v_cruise)/dec;

        if(profile==MotionProfile.TRIANGULAR)
        {
            d_cruise=0;
            t_cruise=0;
        }
        else if(profile==MotionProfile.TRAPEZOIDAL)
        {
            d_cruise=totalDistance-d_acc-d_dec;
            t_cruise=d_cruise/v_cruise;
        }
    }

    public double[] getPointInfo(double t)
    {
        double[] result = new double[3];//contains position, velocity, acceleration in that order
        if(profile==MotionProfile.TRAPEZOIDAL)
        {
            if(t>=0&t<=t_acc)
            {
                result[0]=v_start*t+0.5*acc*t*t;
                result[1]=v_start+acc*t;
                result[2]=acc;
                return result;
            }
            if(t>=t_acc&t<=(t_cruise+t_acc))
            {
                result[0]=d_acc+(t-t_acc)*v_cruise;
                result[1]=v_cruise;
                result[2]=0;
                return result;
            }
            if(t>=(t_acc+t_cruise)&t<(t_acc+t_cruise+t_dec))
            {
                result[0]=d_acc+d_cruise+((t-t_acc-t_cruise)*v_cruise+(t-t_acc-t_cruise)*(t-t_acc-t_cruise)*dec*0.5);
                result[1]=v_cruise+dec*(t-t_acc-t_cruise);
                result[2]=dec;
                return result;
            }
        }
        if(profile==MotionProfile.TRIANGULAR)//this entire thing is unnecessary. It's the same as previous (trapezoidal) code, but with t_cruise and d_cruise trivialized to 0, which already happened. somehow it makes me less nervous to have this though
        {
            if(t>=0&t<=t_acc)
            {
                result[0]=v_start*t+0.5*acc*t*t;
                result[1]=v_start+acc*t;
                result[2]=acc;
                return result;
            }
            if(t>=(t_acc)&t<(t_acc+t_dec))
            {
                result[0]=d_acc+((t-t_acc)*v_cruise+(t-t_acc)*(t-t_acc)*dec*0.5);
                result[1]=v_cruise+dec*(t-t_acc);
                result[2]=dec;
                return result;
            }
        }
        return result;
    }

    public void getMotionProfile()
    {
        trying = new MotionPoint[(int) ((t_acc + t_cruise + t_dec)*LOOP_HERTZ + 1)];
        double spd;
        double curv;
        double xp;
        double yp;
        double xpp;
        double ypp;
        double sval;
        int splno;
        double d;
        for(int i=0; i<trying.length; i++)
        {
            spd=getPointInfo(i/LOOP_HERTZ)[1];
            splno= (int) (pathfinder.getSplineNo(getPointInfo(i/LOOP_HERTZ)[0])[0]);
            d=pathfinder.getSplineNo(getPointInfo(i/LOOP_HERTZ)[0])[1];
            sval=pathfinder.splines[splno].ltos.ceilingEntry(d).getValue();
            xp=pathfinder.splines[splno].evaluateFunction(pathfinder.splines[splno].xprimecoef, sval);
            xpp=pathfinder.splines[splno].evaluateFunction(pathfinder.splines[splno].xdoubleprimecoef, sval);
            yp=pathfinder.splines[splno].evaluateFunction(pathfinder.splines[splno].yprimecoef, sval);
            ypp=pathfinder.splines[splno].evaluateFunction(pathfinder.splines[splno].ydoubleprimecoef, sval);

            curv=Math.abs(xpp*yp-ypp*xp)/Math.pow((xp*xp + yp*yp), 1.5);
            trying[i] = new MotionPoint(spd,1/curv);
        }
    }
    

}
