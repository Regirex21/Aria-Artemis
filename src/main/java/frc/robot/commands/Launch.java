package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.CANFuelSubsystem;

import static frc.robot.Constants.FuelConstants.*;

public class Launch extends Command {

  private final CANFuelSubsystem fuelSubsystem;

  private double rpm;
  private double feederVoltage;
  private boolean useDashboard;

  // 👉 Constructor actual (teleop)
  public Launch(CANFuelSubsystem fuelSystem) {
    this.fuelSubsystem = fuelSystem;
    this.useDashboard = true;
    addRequirements(fuelSystem);
  }

  // 👉 Nuevo constructor (auto)
  public Launch(CANFuelSubsystem fuelSystem, double rpm, double feederVoltage) {
    this.fuelSubsystem = fuelSystem;
    this.rpm = rpm;
    this.feederVoltage = feederVoltage;
    this.useDashboard = false;
    addRequirements(fuelSystem);
  }

  @Override
  public void initialize() {

    if(useDashboard) {
        rpm = SmartDashboard.getNumber(
            "Launching launcher roller value",
            LAUNCHER_RPM);

        feederVoltage = SmartDashboard.getNumber(
            "Launching feeder roller value",
            LAUNCHING_FEEDER_VOLTAGE);
    }

    fuelSubsystem.setLauncherVelocity(rpm);
    fuelSubsystem.setFeederRoller(feederVoltage);
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