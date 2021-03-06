package frc.robot;

/**
 * A class holding all of the constants of every mechanism on the robot.
 * Place global constants in this class, and mechanism-specific constants inside their respective mechanism subclass.
 * When accessing a mechanism-specific port, call Constants.[MECHANISM].[CONSTANT]
 */
public class Constants {
    //All general constants go here
    //public static final double TIME_STEP = 0.02;

    public static final int TALON_TIMEOUT = 10; // in ms

    public static class Drivetrain {
        public static final double TICKS_PER_METER = 256 / (4 * 0.0254 * Math.PI);
        public static final double TICKS_PER_RAD = 1024 / (2 * Math.PI); // TODO: change to real value

        public static final double MAX_VEL = 3;// in m/s
        public static final double TIME_STEP = 0.02; // in seconds
        public static final double MAX_ACCELERATION = 0.4;// in m/s^2 (currently not the correct number)
        public static final int MAX_CURRENT = 35; // in ampere
    }

    public static class SwerveDrive {
        public static final double ROBOT_LENGTH = 0.75; // in meters
        public static final double ROBOT_WIDTH = 0.75; // in meters

        // the speed of the robot, this constant multiplies the speed outputs from the joysticks
        public static final double SPEED_MULTIPLIER = 0.7;

        // the rotational speed of the robot, this constant multiplies the rotation output of the joystick
        public static final double ROTATION_MULTIPLIER = Math.PI;

        public static final double JOYSTICK_THRESHOLD = 0.05; // TODO: tune to actual value

        // encoder 0 for angle motors
        public static final int[] MECHANICAL_OFFSET = new int[]{-1720, -1690, -1141, 1}; //TODO: incorrect
    }

    public static class SwerveModule {
        // TODO: set PIDF
        public static final double KP = 0;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KF = 0;
    }



    public static class ExampleSubsystem1 {
        //All of the Subsystem specific constants go here,and need to be static.

        //public static final double TICKS_PER_METER = 256 / (4*0.0254*Math.PI);
        //public static final double MAX_VELOCITY = 5;
    }
}