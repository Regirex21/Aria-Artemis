package frc.robot;

import static frc.robot.Constants.OperatorConstants.DRIVER_CONTROLLER_PORT;
import static frc.robot.Constants.OperatorConstants.OPERATOR_CONTROLLER_PORT;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.commands.*;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;

public class RobotContainer {

  // =============================
  //SUBSYSTEMS
  // =============================
  private final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();
  private final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();

  // =============================
  //CONTROLES
  // =============================
  private final CommandXboxController driverController =
      new CommandXboxController(DRIVER_CONTROLLER_PORT);

  private final CommandXboxController operatorController =
      new CommandXboxController(OPERATOR_CONTROLLER_PORT);

  // =============================
  // AUTO CHOOSER
  // =============================
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  // =============================
  //CONSTRUCTOR
  // =============================
  public RobotContainer() {

    configureBindings();

    //AUTO CONFIG
    autoChooser.setDefaultOption("auto base", new Auto4_Base(driveSubsystem, fuelSubsystem));
    autoChooser.addOption("Auto1 Human Player", new Auto1_HumanPlayer(driveSubsystem, fuelSubsystem));
    autoChooser.addOption("Auto2 izquierda",new Auto2_Izquierda(driveSubsystem, fuelSubsystem));
    autoChooser.addOption("Auto3 Derecha", new Auto3_Derecha(driveSubsystem, fuelSubsystem));

    SmartDashboard.putData("Auto Chooser", autoChooser);

    // =============================
    //DEFAULT COMMANDS
    // =============================

    driveSubsystem.setDefaultCommand(
        new Drive(driveSubsystem, driverController)
    );

    fuelSubsystem.setDefaultCommand(
        fuelSubsystem.run(() -> fuelSubsystem.stop())
    );
  }

  // =============================
  // 🎮 BUTTON BINDINGS
  // =============================
  private void configureBindings() {

    // -------- OPERATOR --------

    // Intake
    operatorController.leftBumper()
      .whileTrue(new Intake(fuelSubsystem));

    // Shoot sequence
    operatorController.rightBumper()
      .whileTrue(new LaunchSequence(fuelSubsystem));

    // Eject
    operatorController.a()
      .whileTrue(new Eject(fuelSubsystem));

    // -------- DRIVER --------

    // Lock heading (NavX)
    driverController.leftBumper()
        .onTrue(driveSubsystem.runOnce(() -> driveSubsystem.lockHeading()));

    driverController.leftBumper()
        .onFalse(driveSubsystem.runOnce(() -> driveSubsystem.unlockHeading()));
  }

  // =============================
  //AUTO
  // =============================
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}