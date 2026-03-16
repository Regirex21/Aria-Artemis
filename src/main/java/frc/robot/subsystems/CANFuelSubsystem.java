package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem extends SubsystemBase {

  private final SparkMax feederRoller;
  private final SparkMax intakeLauncherRoller;

  private final RelativeEncoder launcherEncoder;
  private final SparkClosedLoopController launcherPID;

  private double targetRPM = LAUNCHER_RPM;

  @SuppressWarnings("removal")
  public CANFuelSubsystem() {

    intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushless);
    feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushed);

    launcherEncoder = intakeLauncherRoller.getEncoder();
    launcherPID = intakeLauncherRoller.getClosedLoopController();

    // FEEDER CONFIG
    SparkMaxConfig feederConfig = new SparkMaxConfig();
    feederConfig.smartCurrentLimit(FEEDER_MOTOR_CURRENT_LIMIT);

    feederRoller.configure(
        feederConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    // LAUNCHER CONFIG
    SparkMaxConfig launcherConfig = new SparkMaxConfig();

    launcherConfig.inverted(false);
    launcherConfig.smartCurrentLimit(LAUNCHER_MOTOR_CURRENT_LIMIT);

    launcherConfig.closedLoop.pid(0.0003, 0, 0);
    launcherConfig.closedLoop.velocityFF(0.0002);

    intakeLauncherRoller.configure(
        launcherConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    // Dashboard defaults
    SmartDashboard.putNumber("Shooter Target RPM", LAUNCHER_RPM);
    SmartDashboard.putNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE);
    SmartDashboard.putNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE);
  }

  @SuppressWarnings("removal")
  public void setLauncherVelocity(double rpm) {
    targetRPM = rpm;
    launcherPID.setReference(rpm, ControlType.kVelocity);
  }

  public void setIntakeLauncherVoltage(double voltage) {
    intakeLauncherRoller.setVoltage(voltage);
  }

  public void setFeederRoller(double voltage) {
    feederRoller.setVoltage(voltage);
  }

  public void stop() {
    feederRoller.set(0);
    intakeLauncherRoller.set(0);
  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("Shooter RPM", launcherEncoder.getVelocity());

    SmartDashboard.putNumber(
        "Shooter Voltage",
        intakeLauncherRoller.getAppliedOutput() * intakeLauncherRoller.getBusVoltage());

    SmartDashboard.putNumber(
        "Shooter Current",
        intakeLauncherRoller.getOutputCurrent());

    SmartDashboard.putNumber("Shooter Target RPM", targetRPM);
  }
}