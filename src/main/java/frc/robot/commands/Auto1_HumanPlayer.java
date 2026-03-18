package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;

public class Auto1_HumanPlayer extends SequentialCommandGroup {

    public Auto1_HumanPlayer(CANDriveSubsystem drive, CANFuelSubsystem fuel) {

   addCommands(

    new AutoDrive(drive, -0.7, 0).withTimeout(3.0),
    new WaitCommand(0.3),

    // spin-up
    new Launch(fuel, 4800, -7),
    new WaitCommand(1.0),

    // dispara
    new Launch(fuel, 4800, 9),
    new WaitCommand(10)

);
    }
}