package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.studica.frc.AHRS;

import static frc.robot.Constants.DriveConstants.*;

public class CANDriveSubsystem extends SubsystemBase {

  //Motores
  private final SparkMax leftLeader;
  private final SparkMax leftFollower;
  private final SparkMax rightLeader;
  private final SparkMax rightFollower;

  //NavX
  private final AHRS navx;

  //Heading control
  private double targetHeading = 0;
  private boolean headingLocked = false;
  private final double kPHeading = 0.03;

  // 🚗 Drive
  private final DifferentialDrive drive;

  @SuppressWarnings("removal")
  public CANDriveSubsystem() {

    // Motores
    leftLeader = new SparkMax(LEFT_LEADER_ID, MotorType.kBrushed);
    leftFollower = new SparkMax(LEFT_FOLLOWER_ID, MotorType.kBrushed);
    rightLeader = new SparkMax(RIGHT_LEADER_ID, MotorType.kBrushed);
    rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushed);

    // NavX
    navx = new AHRS(AHRS.NavXComType.kMXP_SPI);
    navx.reset();

    // Drive
    drive = new DifferentialDrive(leftLeader, rightLeader);

    // CAN timeout
    leftLeader.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightFollower.setCANTimeout(250);

    // Configuración motores
    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(10);
    config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);

    // Followers
    config.follow(leftLeader);
    leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.follow(rightLeader);
    rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Leaders
    config.disableFollowerMode();
    rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.inverted(true);
    leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // =============================
  //NAVX / HEADING CONTROL
  // =============================

  public void resetGyro() {
    navx.reset();
  }

  public double getHeading() {
    return navx.getYaw();
  }

  public void lockHeading() {
    targetHeading = navx.getYaw();
    headingLocked = true;
  }

  public void unlockHeading() {
    headingLocked = false;
  }

  private double getHeadingCorrection() {
    double error = targetHeading - navx.getYaw();
    return error * kPHeading;
  }

  // =============================
  //DRIVE
  // =============================

  public void driveArcade(double xSpeed, double zRotation) {

    // Si el driver gira manualmente → desactiva lock
    if (Math.abs(zRotation) > 0.05) {
      headingLocked = false;
    }

    // Corrección automática con NavX
    if (headingLocked && Math.abs(zRotation) < 0.05) {
      zRotation = getHeadingCorrection();
    }

    drive.arcadeDrive(xSpeed, zRotation);
  }

  public void stop() {
    drive.arcadeDrive(0, 0);
  }

  // =============================
  // TELEMETRÍA
  // =============================

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Heading", navx.getYaw());
    SmartDashboard.putBoolean("Heading Locked", headingLocked);
  }
}