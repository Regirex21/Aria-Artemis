// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;

import com.studica.frc.AHRS;

public class CANDriveSubsystem extends SubsystemBase {
  private final SparkMax leftLeader;
  private final SparkMax leftFollower;
  private final SparkMax rightLeader;
  private final SparkMax rightFollower;


  private final AHRS navx;
  private double targetHeading = 0;
  private boolean headingLocked = false;
  private final double kPHeading = 0.03;
  private final double kPTurn = 0.01;
  private final double maxTurnSpeed = 0.5;
  private final double minTurnSpeed = 0.08;
  private final double angleTolerance = 2.0;

  private final DifferentialDrive drive;

  @SuppressWarnings("removal")
  public CANDriveSubsystem() {
    // create brushed motors for drive
    leftLeader = new SparkMax(LEFT_LEADER_ID, MotorType.kBrushed);
    leftFollower = new SparkMax(LEFT_FOLLOWER_ID, MotorType.kBrushed);
    rightLeader = new SparkMax(RIGHT_LEADER_ID, MotorType.kBrushed);
    rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushed);

    
    navx = new AHRS(AHRS.NavXComType.kMXP_SPI);
    navx.reset();

    // set up differential drive class
    drive = new DifferentialDrive(leftLeader, rightLeader);

    // Set can timeout. Because this project only sets parameters once on
    // construction, the timeout can be long without blocking robot operation. Code
    // which sets or gets parameters during operation may need a shorter timeout.
    leftLeader.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightFollower.setCANTimeout(250);

    // Create the configuration to apply to motors. Voltage compensation
    // helps the robot perform more similarly on different
    // battery voltages (at the cost of a little bit of top speed on a fully charged
    // battery). The current limit helps prevent tripping
    // breakers.
    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(10);
    config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);

    // Set configuration to follow each leader and then apply it to corresponding
    // follower. Resetting in case a new controller is swapped
    // in and persisting in case of a controller reset due to breaker trip
    config.follow(leftLeader);
    leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.follow(rightLeader);
    rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Remove following, then apply config to right leader
    config.disableFollowerMode();
    rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // Set config to inverted and then apply to left leader. Set Left side inverted
    // so that postive values drive both sides forward
    config.inverted(true);
    leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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

public double getHeading() {
    return navx.getYaw();
}

public void resetGyro() {
    navx.reset();
}

public void turnToAngle(double targetAngle) {

    double error = angleError(targetAngle, navx.getYaw());

    double turnSpeed = error * kPTurn;

    turnSpeed = Math.max(Math.min(turnSpeed, 0.5), -0.5);

    drive.arcadeDrive(0, turnSpeed);
}

public boolean atAngle(double targetAngle) {
    return Math.abs(angleError(targetAngle, navx.getYaw())) < angleTolerance;
}

private double angleError(double target, double current) {
    double error = target - current;
    error = (error + 180) % 360 - 180;
    return error;
}

public void snapTurn(double targetAngle) {

    double error = angleError(targetAngle, navx.getYaw());

    if (Math.abs(error) < angleTolerance) {
        drive.arcadeDrive(0, 0);
        return;
    }

    double turnSpeed = error * kPTurn;

    turnSpeed = Math.max(Math.min(turnSpeed, maxTurnSpeed), -maxTurnSpeed);

    if (Math.abs(turnSpeed) < minTurnSpeed) {
        turnSpeed = minTurnSpeed * Math.signum(turnSpeed);
    }

    drive.arcadeDrive(0, turnSpeed);
}


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Robot Heading", navx.getYaw());
    SmartDashboard.putNumber("Pitch", navx.getPitch());
SmartDashboard.putNumber("Roll", navx.getRoll());
  }

public void driveArcade(double xSpeed, double zRotation) {

    if (Math.abs(zRotation) > 0.05) {
        headingLocked = false;
    }

    if (headingLocked && Math.abs(zRotation) < 0.05) {
        zRotation = getHeadingCorrection();
    }

    drive.arcadeDrive(xSpeed, zRotation);
}

}
