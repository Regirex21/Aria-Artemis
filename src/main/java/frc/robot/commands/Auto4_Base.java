package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;

public class Auto4_Base extends SequentialCommandGroup {

  public Auto4_Base(CANDriveSubsystem driveSubsystem, CANFuelSubsystem ballSubsystem) {

    addCommands(

      // =============================
      // 0. Reset + lock heading
      // =============================
      new InstantCommand(driveSubsystem::resetGyro),
      new InstantCommand(driveSubsystem::lockHeading),

      // =============================
      // 1. Avanzar recto
      // =============================
      new AutoDrive(driveSubsystem, 0.5, 0.0).withTimeout(1),

      // =============================
      // 2. Disparo
      // =============================
      new Launch(ballSubsystem).withTimeout(10)

    );
  }
}