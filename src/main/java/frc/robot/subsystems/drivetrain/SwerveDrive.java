package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.Utils;

import static frc.robot.Ports.SwerveDrive.*;

public class SwerveDrive extends SubsystemBase {

    private AHRS gyro = new AHRS(SPI.Port.kMXP);
    public SwerveModule[] swerveModules = new SwerveModule[4];
    private boolean isFieldOriented;

    public SwerveDrive(boolean isFieldOriented) {
        gyro.reset();

        swerveModules[0] = new SwerveModule(0, new TalonFX(DRIVE_MOTOR_1), new TalonSRX(ANGLE_MOTOR_1), FRONT_RIGHT_INVERTED);
        swerveModules[1] = new SwerveModule(1, new TalonFX(DRIVE_MOTOR_2), new TalonSRX(ANGLE_MOTOR_2), FRONT_LEFT_INVERTED);
        swerveModules[2] = new SwerveModule(2, new TalonFX(DRIVE_MOTOR_3), new TalonSRX(ANGLE_MOTOR_3), BACK_RIGHT_INVERTED);
        swerveModules[3] = new SwerveModule(3, new TalonFX(DRIVE_MOTOR_4), new TalonSRX(ANGLE_MOTOR_4), BACK_LEFT_INVERTED);

        this.isFieldOriented = isFieldOriented;
    }


    /**
     * sets the wheels of the robot to the calculated angle and speed
     * @param forward the Y value of the joystick
     * @param strafe the X value of the joystick
     * @param rotation the rotation Z of the joystick
     */
    public void holonomicDrive(double forward, double strafe, double rotation) {

        double[] robotHeading = getRobotHeading(forward, strafe, rotation, Math.toRadians(gyro.getAngle()));

        double[] velocities = calculateWheelVelocities(robotHeading);
        double[] polar;
        double[][] controls = new double[4][2];

        // converts the cartesian velocities to polar and transfers them to a control matrix
        for (int i = 0; i < 4; i ++) {
            polar = Utils.cartesianToPolar(velocities[2*i], velocities[2*i + 1]);
            controls[i][0] = polar[0];
            controls[i][1] = polar[1];
        }

        // feeds the corresponding control to each wheel
        for (int k = 0; k < 4; k++) {
            swerveModules[k].setSpeed(controls[k][0]);
            swerveModules[k].setAngle(controls[k][1]);
        }
    }

    /**
     * turns the joystick inputs into the robot heading
     * @param forward the Y value of the joystick
     * @param strafe the X value of the joystick
     * @param rotation the rotation Z of the joystick
     * @param robotAngle the current angle of the robot in radians
     * @return an array of the robot heading
     */
    public double[] getRobotHeading(double forward, double strafe, double rotation, double robotAngle) {
        // turns the joystick values into the heading of the robot
        forward *= Constants.SwerveDrive.SPEED_MULTIPLIER;
        strafe *= Constants.SwerveDrive.SPEED_MULTIPLIER;
        rotation *= Constants.SwerveDrive.ROTATION_MULTIPLIER;

        // if the drive style is field oriented, changes the forward and strafe to be according to the field coordinate system
        if (isFieldOriented) {
            // multiplies the 2D rotation matrix by the robot heading, there by rotating the coordinate system
            // see https://en.wikipedia.org/wiki/Rotation_matrix
            double[][] rotationMat = { {Math.cos(robotAngle), -Math.sin(robotAngle)},
                                        {Math.sin(robotAngle), Math.cos(robotAngle)} };
            double[] speeds = Utils.matrixVectorMult(rotationMat, new double[]{forward, strafe});
            forward = speeds[0];
            strafe = speeds[1];
        }
        double[] robotHeading = {forward, strafe, rotation};

        return robotHeading;
    }

    /**
     * calculates the velocity vector of each wheel 
     * @param robotHeading the three joystick outputs:
     * forward the heading of the robot in the Y direction
     * strafe the heading of the robot in the X direction
     * rotation the rotation of the robot
     * @return an array of length 8 in which each pair is the X and Y velocities of each wheel
     */
    public double[] calculateWheelVelocities(double[] robotHeading) {

        // calculates the distance from the center of the robot to the wheels
        double Rx = Constants.SwerveDrive.ROBOT_WIDTH/2;
        double Ry = Constants.SwerveDrive.ROBOT_LENGTH/2;

        // the sign vectors of Rx and Ry
        double[] signX = {1, 1, -1, -1};
        double[] signY = {-1, 1, 1, -1};

        // creates an inverse matrix of all the mathematical operations needed to calculate the wheel velocities
        // see https://file.tavsys.net/control/controls-engineering-in-frc.pdf pg.144
        double[][] M = new double[8][3];

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                M[i][0] = 0;
                M[i][1] = 1;
                M[i][2] = Rx * signX[i/2];
            } else {
                M[i][0] = 1;
                M[i][1] = 0;
                M[i][2] = Ry * signY[i/2];
            }
        }

        // multiplies M by the robotHeading to obtain the wheel velocities
        double[] wheelVelocities = Utils.matrixVectorMult(M, robotHeading);

        return wheelVelocities;

    }

    /**
     * set the angle of the wheels on the robot to lock the robot in place
     */
    public void lock() {
        // calculates the lock angles of the wheels
        double[] lockAngles = calculateLockAngles();

        for (int i = 0; i < 4; i++) {
            swerveModules[i].setSpeed(0);
            swerveModules[i].setAngle(lockAngles[i]);
        }
    }

    /**
     * calculates the angles for which the wheels will lock in place
     */
    public double[] calculateLockAngles() {
        double[] lockAngles = new double[4];

        for (int i = 0; i < 4; i++) {
            lockAngles[i] = Math.PI / 2 - Math.atan(Constants.SwerveDrive.ROBOT_WIDTH / Constants.SwerveDrive.ROBOT_LENGTH) + i * Math.PI/2;
        }

        return lockAngles;
    }


    /**
     * stops all the wheels
     */
    public void stop() {
        for (int i = 0; i < swerveModules.length; i++) {
            swerveModules[i].setSpeed(0);
            swerveModules[i].stopAngleMotor();
        }

    }

}
