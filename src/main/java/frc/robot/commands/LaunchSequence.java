package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.Constants.FuelConstants;
import frc.robot.subsystems.CANFuelSubsystem;

public class LaunchSequence extends Command {

  private final CANFuelSubsystem fuelSubsystem;
  private final Timer timer = new Timer();

  public LaunchSequence(CANFuelSubsystem fuelSubsystem) {
    this.fuelSubsystem = fuelSubsystem;
    addRequirements(fuelSubsystem);
  }

  @Override
  public void initialize() {

    timer.reset();
    timer.start();

    // arrancar shooter
    fuelSubsystem.setLauncherVelocity(FuelConstants.LAUNCHER_RPM);
  }

  @Override
  public void execute() {

    double t = timer.get();

    // feeder reverse inmediatamente
    if (t < 0.2) {
      fuelSubsystem.setFeederRoller(-FuelConstants.LAUNCHING_FEEDER_VOLTAGE);
    }

    // feeder forward
    else {
      fuelSubsystem.setFeederRoller(FuelConstants.LAUNCHING_FEEDER_VOLTAGE);
    }
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