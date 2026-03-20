package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CajaExtensibleSubsystem extends SubsystemBase {

    private final Servo leftServo = new Servo(1);
    private final Servo rightServo = new Servo(2);

    // Posiciones base
    private double currentPosition = 0.12;
    private final double EXTENDED = 0.3;
    private final double RETRACTED = 0.12;

    // Timer para vibración
    private final Timer timer = new Timer();

    public CajaExtensibleSubsystem() {
        timer.start(); //inicia timer
    }

    // Aplicar posición (con inversión para el servo en espejo)
    private void applyPosition(double value) {
        leftServo.set(value);
        rightServo.set(1.0 - value);
    }

    // =============================
    // CONTROL MANUAL
    // =============================
    public void extend() {
        currentPosition = EXTENDED;
        applyPosition(currentPosition);
    }

    public void retract() {
        currentPosition = RETRACTED;
        applyPosition(currentPosition);
    }

    // =============================
    // VIBRACIÓN CON DELAY PARA DISPARO
    // =============================
    public void vibrateDelayed(double delaySeconds) {
        if (timer.get() > delaySeconds) {
            double offset = 0.02 * Math.sin(timer.get() * 10);
            applyPosition(currentPosition + offset);
        } else {
            applyPosition(currentPosition);
        }
    }

    // Resetear timer cuando empieza disparo
    public void resetTimer() {
        timer.reset();
    }
}