/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomous;

public class TrajectoryFinder {

    public TrajectoryFinder() {
    }

    public enum MotionProfile{
        TRIANGULAR,
        TRAPEZOIDAL,
        S_CURVE;
    }
}