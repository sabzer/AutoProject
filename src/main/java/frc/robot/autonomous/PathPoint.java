/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomous;
/**
 * Add your docs here.
 */
public class PathPoint {
    double x;
    double y;
    double s;

    public PathPoint(double x, double y, double s)
    {
        this.x=x;
        this.y=y;
        this.s=s;
    }
}
