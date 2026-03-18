package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Auto2_Izquierda extends SequentialCommandGroup {

  public Auto2_Izquierda(CANDriveSubsystem drive, CANFuelSubsystem shooter) {

    addCommands(

      // Reset inicial 
      new InstantCommand(drive::resetGyro),

      // 1. Atrás
      new AutoDrive(drive, -0.4, 0).withTimeout(1.2),

      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // 2. Disparo
      new Launch(shooter).withTimeout(2),

      // 3. Giro izquierda 45°
      new TurnAngle(drive, -45),

      // 4. Avanzar
      new AutoDrive(drive, 0.5, 0).withTimeout(1.5),

      new AutoDrive(drive, 0, 0).withTimeout(0.1),

      // 5. Giro derecha 90°
      new TurnAngle(drive, 90),

      // 6. Avanzar + intake
      new AutoDrive(drive, 0.4, 0)
          .alongWith(new IntakeAuto(shooter))
          .withTimeout(2.0),

      // detener todo
      new AutoDrive(drive, 0, 0).withTimeout(0.1)
    );
  }
}