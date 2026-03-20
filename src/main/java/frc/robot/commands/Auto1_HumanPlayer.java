package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;

public class Auto1_HumanPlayer extends SequentialCommandGroup {

    public Auto1_HumanPlayer(CANDriveSubsystem drive, CANFuelSubsystem fuel) {

        addCommands(

            // =============================
            // 0. Reset + lock heading
            // =============================
            new InstantCommand(drive::resetGyro),
            new InstantCommand(drive::lockHeading),

            // =============================
            // 1. Atrás recto
            // =============================
            new AutoDrive(drive, -0.7, 0).withTimeout(3.0),
            new WaitCommand(0.3),

            // =============================
            // 2. Spin-up
            // =============================
            new Launch(fuel, 4800, -7),
            new WaitCommand(1.0),

            // =============================
            // 3. Disparo
            // =============================
            new Launch(fuel, 4800, 9),
            new WaitCommand(1.0),

            // =============================
            // 4. LIBERAR HEADING
            // =============================
            new InstantCommand(drive::unlockHeading),

            // =============================
            // 5. GIRAR AL FRENTE REAL
            // =============================
            new TurnAngle(drive, 315),

            // pequeña pausa
            new WaitCommand(0.2),

            // =============================
            // 6. RESET PARA TELEOP
            // =============================
            new InstantCommand(drive::resetGyro),

            // detener todo
            new AutoDrive(drive, 0, 0).withTimeout(0.1)
        );
    }
}