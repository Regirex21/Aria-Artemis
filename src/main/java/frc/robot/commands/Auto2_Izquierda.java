package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Auto2_Izquierda extends SequentialCommandGroup {

  public Auto2_Izquierda(CANDriveSubsystem drive, CANFuelSubsystem shooter) {

    addCommands(

      // =============================
      // 0. Reset inicial (hub = 0°)
      // =============================
      new InstantCommand(drive::resetGyro),

      // =============================
      // 1. Atrás
      // =============================
      new AutoDrive(drive, -0.4, 0).withTimeout(1.2),
      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // =============================
      // 2. Disparo
      // =============================
      new Launch(shooter).withTimeout(2),

      // =============================
      // 3. Giro izquierda ~45°
      // (equivalente a -45 → 315)
      // =============================
      new TurnAngle(drive, 312),

      // =============================
      // 4. Avanzar
      // =============================
      new AutoDrive(drive, 0.5, 0).withTimeout(1.5),
      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // =============================
      // 5. Giro hacia zona intake
      // =============================
      new TurnAngle(drive, 90),

      // =============================
      // 6. Avanzar + intake para precarga
      // =============================
      new AutoDrive(drive, 0.4, 0)
          .alongWith(new IntakeAuto(shooter))
          .withTimeout(2.0),

      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // =============================
      // 7. GIRAR AL FRENTE REAL PARA TELEOP
      // =============================
      new TurnAngle(drive, 315),

      // pequeña pausa
      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // =============================
      // 8. RESET PARA TELEOP
      // =============================
      new InstantCommand(drive::resetGyro),

      // detener todo
      new AutoDrive(drive, 0, 0).withTimeout(0.1)
    );
  }
}