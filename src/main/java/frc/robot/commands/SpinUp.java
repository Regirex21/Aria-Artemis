package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.CANFuelSubsystem;

import static frc.robot.Constants.FuelConstants.*;

public class SpinUp extends Command {

  private final CANFuelSubsystem fuelSubsystem;

  public SpinUp(CANFuelSubsystem fuelSystem) {
    this.fuelSubsystem = fuelSystem;
    addRequirements(fuelSystem);
  }

  @Override
  public void initialize() {

    fuelSubsystem.setLauncherVelocity(
        SmartDashboard.getNumber(
            "Launching launcher roller value",
            LAUNCHER_RPM));
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}