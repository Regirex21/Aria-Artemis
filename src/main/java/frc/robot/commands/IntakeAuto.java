package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANFuelSubsystem;

public class IntakeAuto extends Command {

  private final CANFuelSubsystem fuelSubsystem;

  public IntakeAuto(CANFuelSubsystem fuelSystem) {
    this.fuelSubsystem = fuelSystem;
    addRequirements(fuelSystem);
  }

  @Override
  public void execute() {
    fuelSubsystem.runIntake();
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