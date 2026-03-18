package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;

public class TurnAngle extends Command {

  private final CANDriveSubsystem drive;
  private final double targetAngle;

  public TurnAngle(CANDriveSubsystem drive, double angle) {
    this.drive = drive;
    this.targetAngle = angle;
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    drive.resetGyro(); // importante
  }

  @Override
  public void execute() {
    double error = targetAngle - drive.getHeading();

    double kP = 0.01; // ajustable
    double turn = kP * error;

    drive.driveArcade(0, turn);
  }

  @Override
  public void end(boolean interrupted) {
    drive.driveArcade(0, 0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(targetAngle - drive.getHeading()) < 2;
  }
}