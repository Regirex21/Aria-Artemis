package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.CANFuelSubsystem;

import static frc.robot.Constants.FuelConstants.*;

public class Launch extends Command {

  private final CANFuelSubsystem fuelSubsystem;

  public Launch(CANFuelSubsystem fuelSystem) {
    this.fuelSubsystem = fuelSystem;
    addRequirements(fuelSystem);
  }

  @Override
  public void initialize() {

    fuelSubsystem.setLauncherVelocity(
        SmartDashboard.getNumber(
            "Launching launcher roller value",
            LAUNCHER_RPM));

    fuelSubsystem.setFeederRoller(
        SmartDashboard.getNumber(
            "Launching feeder roller value",
            LAUNCHING_FEEDER_VOLTAGE));
  }

  @Override
  public void end(boolean interrupted) {
    fuelSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}