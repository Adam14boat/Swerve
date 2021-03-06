package frc.robot;

/**
 * A class holding all of the ports on the robot.
 * Place mechanism-specific ports inside their own sub-class.
 * When accessing a mechanism-specific port, call Ports.[MECHANISM].[PORT_NAME]
 */
public class Ports {
    public static class ExampleSubsystem1 {
        //public static int TALON_PORT = 1;
    }

    public static class SwerveDrive {
        // front right
        public static final int DRIVE_MOTOR_1 = 11;
        public static final int ANGLE_MOTOR_1 = 12;
        public static final boolean[] FRONT_RIGHT_INVERTED = {false, false, false, false};

        // front left
        public static final int DRIVE_MOTOR_2 = 21;
        public static final int ANGLE_MOTOR_2 = 22;
        public static final boolean[] FRONT_LEFT_INVERTED = {false, false, false, false};

        // back right
        public static final int DRIVE_MOTOR_3 = 31;
        public static final int ANGLE_MOTOR_3 = 32;
        public static final boolean[] BACK_RIGHT_INVERTED = {false, false, false, false};

        // back left
        public static final int DRIVE_MOTOR_4 = 41;
        public static final int ANGLE_MOTOR_4 = 42;
        public static final boolean[] BACK_LEFT_INVERTED = {false, false, false, false};

        // encoder not continuous feedback
        public static final boolean IS_NOT_CONTINUOUS_FEEDBACK = true;
    }

}
