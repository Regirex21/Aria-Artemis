package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.CANFuelSubsystem;

import static frc.robot.Constants.FuelConstants.*;

public class Eject extends Command {

  private final CANFuelSubsystem fuelSubsystem;

  public Eject(CANFuelSubsystem fuelSystem) {
    this.fuelSubsystem = fuelSystem;
    addRequirements(fuelSystem);
  }

  @Override
  public void initialize() {

    fuelSubsystem.setIntakeLauncherVoltage(
        -SmartDashboard.getNumber(
            "Intaking intake roller value",
            INTAKING_INTAKE_VOLTAGE));

    fuelSubsystem.setFeederRoller(
        -SmartDashboard.getNumber(
            "Intaking feeder roller value",
            INTAKING_FEEDER_VOLTAGE));
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