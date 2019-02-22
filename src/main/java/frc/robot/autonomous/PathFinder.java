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

    public class Spline{
        double c5,c4, c3, c2, c1;
        double c0;//not needed if you do the shift thing

        double arcLength;

        public Spline()
        {
        }

        public Spline(double c5, double c4, double c3, double c2, double c1, double c0)
        {

        }

        private void setArcLength()
        {

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
        if (pathType==PathType.CUBIC_HERMITE)
        {
            temp.c5=0;
            temp.c4=0;
        }
        if(pathType==PathType.QUINTIC_HERMITE)
        {

        }
        temp.setArcLength();
        return temp;
    }

}
