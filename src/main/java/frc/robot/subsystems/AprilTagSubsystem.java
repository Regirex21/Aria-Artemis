package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.apriltag.*;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class AprilTagSubsystem {

    private UsbCamera camera;
    private CvSink cvSink;
    private Mat mat;

    private AprilTagDetector detector;
    private AprilTagPoseEstimator estimator;

    private int counter = 0;

    public AprilTagSubsystem() {

        // Cámara
        camera = CameraServer.startAutomaticCapture();
        camera.setResolution(640, 480);
        camera.setFPS(20);

        cvSink = CameraServer.getVideo();
        mat = new Mat();

        // Detector
        detector = new AprilTagDetector();
        detector.addFamily("tag36h11");

        // Tamaño real del tag (AJÚSTALO SI NO ES EXACTO)
        double tagSize = 0.165; // metros


        double fx = 900;
        double fy = 900;
        double cx = 320;
        double cy = 240;

        estimator = new AprilTagPoseEstimator(
            new AprilTagPoseEstimator.Config(tagSize, fx, fy, cx, cy)
        );
    }

    public void process() {

        counter++;
        if (counter % 3 != 0) return; // reduce carga

        if (cvSink.grabFrame(mat) == 0) return;

        // Procesamiento
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(mat, mat, new Size(5, 5), 0);

        // Detección
        AprilTagDetection[] detections = detector.detect(mat);

        // DEBUG
        SmartDashboard.putNumber("Detecciones", detections.length);

        if (detections.length > 0) {

            AprilTagDetection tag = detections[0];

            var pose = estimator.estimate(tag);

            double distance = pose.getTranslation().getNorm();

            SmartDashboard.putNumber("Distancia de tiro", distance);
        }
    }
}