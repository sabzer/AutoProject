/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomous;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.autonomous.PathFinder.PathType;

/**
 * Add your docs here.
 */
public class AutonomousSubsystem extends Subsystem {

  public AutonomousSubsystem()
  {
    Waypoint[] way = {new Waypoint(0,0,90), new Waypoint(0.25,1,90), new Waypoint(1,3,35)};
    PathFinder path = new PathFinder(way, PathType.QUINTIC_HERMITE);
    for(int i=0; i<path.splines.length; i++)
    {
      System.out.println("Arc Length is " + path.splines[i].arcLength + " for spline " + i );
      for(int j=0; j<=100; j++)
      {
        System.out.println(path.splines[i].evaluateFunction(path.splines[i].xcoef, j*.01) + ", " + path.splines[i].evaluateFunction(path.splines[i].ycoef, j*.01)+ ", " + j*.01);
      }
      System.out.println("End Sub-spline");
    }
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
