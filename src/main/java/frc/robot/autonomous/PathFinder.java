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

    Waypoint[] waypoints;
    public enum PathType{
        CUBIC_HERMITE,
        QUINTIC_HERMITE,
        B_SPLINE;
    }

    public PathFinder(Waypoint[] waypoints)
    {
        copy(waypoints);
    }

    private void copy(Waypoint[] waypoints)
    {
        waypoints = new Waypoint[this.waypoints.length];
        for(int i=0; i<this.waypoints.length; i++)
            waypoints[i] = new Waypoint(this.waypoints[i].x, this.waypoints[i].y, this.waypoints[i].deg);
    }
}
