package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

// tweetybird specific imports
import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;
import dev.narlyx.tweetybird.TweetyBird;

@SuppressWarnings("FieldCanBeLocal")  // suppress "may be converted to local variable" warnings
/// Robot Configuration Class.
public class Config {

    // private instances
    private final LinearOpMode opMode;
    private DcMotor leftSlide, rightSlide, intakeMotor, liftMotor;
    private DcMotor activeSlide; // defines which slide motor to use
    private Servo intakeFlip, clawLeft, clawRight, boxServo;
    private static double clawLeftOpen, clawRightOpen, clawLeftClosed, clawRightClosed;

    // public instances
    public DcMotor fl, fr, bl, br;  // drive motors
    public TouchSensor robotFrontBottomSensor, robotFrontTopSensor, liftBottomSensor, slideSensor;
    public RobotClaw claw;          // claw class
    public RobotClaw.RoadRunner rRClaw; // claw for RR
    public RobotLift lift;          // lift class
    public RobotLift.RoadRunner rRLift; // lift for RR
    public RobotSlide slide;        // slide class
    public RobotWrist wrist;        // wrist class
    public RobotBox box;            // box class
    public RobotBox.RoadRunner rRBox; // box for RR
    public RobotIntake intake;      // intake class
    public ThreeWheeled odometer;   // odometer pods
    public Mecanum mecanum;         // mecanum wheels
    public TweetyBird tweetyBird;   // tweetyBird

    private static final double
            wristCollectPos     = 0.02,
            wristUpPos          = 0.6,
            wristTransferPos    = 0.9,
            boxDumpPos          = 0.83,
            boxTransferPos      = 0.07,
            boxStorePos         = 0.2,
            clawClosedPos       = 0.3, // smaller # = More Closed (0-1) (left claw)
            clawOpenPos         = 0.5,  // larger # = More Open (0-1)  (left claw)
            liftDownwardPower   = 0.4,
            liftUpwardPower     = 0.8,
            intakeInPower       = 0.8,
            intakeOutPower      = 0.4;

    private static final int
            liftAboveChamberPos = 1665,
            liftBelowChamberPos = 1200,
            liftWallPos         = 280,
            liftAboveWallPos    = 400;


    public static final int
            liftLimitPos    = 3300,
            slideMaxPos     = 450,
            intakeOnPos     = 250,
            slideBrakePos   = 100; // distance the slide has BRAKE mode (ZPB)

    // allow opMode to be referenced
    public Config(LinearOpMode opMode){this.opMode = opMode;}

    /// Initializes robot components.
    public void init(){
        // hardwareMap to get motors
        HardwareMap hwMap = opMode.hardwareMap;

        ///Define Motors
        // front left wheel
        fl = hwMap.get(DcMotor.class, "FL");
        fl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // front right wheel
        fr = hwMap.get(DcMotor.class, "FR");
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // back left wheel
        bl = hwMap.get(DcMotor.class, "BL");
        bl.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // back right wheel
        br = hwMap.get(DcMotor.class, "BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // slide left motor
        leftSlide = hwMap.get(DcMotor.class,"HorzLeft");
        leftSlide.setDirection(DcMotor.Direction.FORWARD);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // slide right motor
        rightSlide = hwMap.get(DcMotor.class,"HorzRight");
        rightSlide.setDirection(DcMotor.Direction.REVERSE);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // define slide motor to use
        activeSlide = leftSlide;

        // intake motor
        intakeMotor = hwMap.get(DcMotor.class,"IntakeMotor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // lift motor
        liftMotor = hwMap.get(DcMotor.class,"Vert");
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        /// Define Servos
        intakeFlip = hwMap.get(Servo.class,"IntakeFlip");
        boxServo = hwMap.get(Servo.class,"BoxServo");
        clawLeft = hwMap.get(Servo.class,"ClawLeft");
        clawRight = hwMap.get(Servo.class,"ClawRight");


        // define values for claw servos
        clawLeftOpen    = clawOpenPos;
        clawRightOpen   = 1- clawOpenPos;
        clawLeftClosed  = clawClosedPos;
        clawRightClosed = 1- clawClosedPos;


        /// Define Touch Sensors
        robotFrontBottomSensor = hwMap.get(TouchSensor.class,"FrontTouch");
        robotFrontTopSensor = hwMap.get(TouchSensor.class,"TopTouch");
        liftBottomSensor = hwMap.get(TouchSensor.class,"MagnetTouch");
        slideSensor = hwMap.get(TouchSensor.class,"HorzTouch");


        /// Define Classes for OpMode Use
        claw = new RobotClaw(clawLeft,clawRight);
        rRClaw = claw.new RoadRunner();
        lift = new RobotLift(liftMotor);
        rRLift = lift.new RoadRunner();
        slide = new RobotSlide(activeSlide);
        wrist = new RobotWrist(intakeFlip);
        box = new RobotBox(boxServo);
        rRBox = box.new RoadRunner();
        intake = new RobotIntake(intakeMotor);

        // built drive
        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fl)
                .setFrontRightMotor(fr)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();

        // build odometer
        odometer = new ThreeWheeled.Builder()
                .setLeftEncoder(bl)
                .setRightEncoder(fr)
                .setMiddleEncoder(br)

                .setEncoderTicksPerRotation(2000)
                .setEncoderWheelRadius(0.944882)

                //Change the true/false values to correct directions
                .setFlipLeftEncoder(true)
                .setFlipRightEncoder(true)
                .setFlipMiddleEncoder(true)

                .setSideEncoderDistance(12)
                .setMiddleEncoderOffset(9.75)
                .build();

        // reset odometer position
        odometer.resetTo(0,0,0);
    }

    /// Initializes Tweety Bird.
    public void initTweetyBird(){
        odometer.resetTo(0,0,0);
        tweetyBird = new TweetyBird.Builder()
                // Your configuration options here
                .setDistanceBuffer(1) //inches
                .setDriver(mecanum)
                .setLinearOpMode(opMode)
                .setMaximumSpeed(0.7)
                .setMinimumSpeed(0.4)
                .setOdometer(odometer)
                .setRotationBuffer(4)
                .setLoggingEnabled(true)
                .build();
    }

    /// Class that contains actions for intake wrist.
    public static class RobotWrist {
        Servo intakeWrist;

        // constructor
        private RobotWrist(Servo intakeWrist){this.intakeWrist = intakeWrist;}

        /** Set specific wrist position.
         *
         * @param pos servo position
         */
        public void position(double pos){
            intakeWrist.setPosition(pos);
        }

        /// Set wrist in collect position.
        public void collect(){
            intakeWrist.setPosition(wristCollectPos);
        }

        /// Set wrist in transfer position.
        public void transfer(){
            intakeWrist.setPosition(wristTransferPos);
        }

        /// Set wrist in up position.
        public void up(){
            intakeWrist.setPosition(wristUpPos);
        }
    }

    /// Class that contains actions for box.
    public static class RobotBox {
        Servo boxServo;

        // constructor
        private RobotBox(Servo boxServo){this.boxServo = boxServo;}

        // rr actions
        public class RoadRunner{

            // dump action
            public class Dump implements Action{

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    dump();
                    return false;
                }
            }
            // method that instantiates for ease of use
            public Action rRDump(){return new Dump();}

            // store action
            public class Store implements Action{

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    store();
                    return false;
                }
            }
            // method that instantiates for ease of use
            public Action rRStore(){return new Store();}

            // transfer action
            public class Transfer implements Action{

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    transfer();
                    return false;
                }
            }
            // method that instantiates for ease of use
            public Action rRTransfer(){return new Transfer();}


        }

        /** Set specific box position.
         *
         * @param pos servo position
         */
        public void position(double pos){
            boxServo.setPosition(pos);
        }

        /// Set box in dump position.
        public void dump(){
            boxServo.setPosition(boxDumpPos);
        }

        /// Set box in storage position.
        public void store(){
            boxServo.setPosition(boxStorePos);
        }

        /// Set box in transfer position.
        public void transfer(){
            boxServo.setPosition(boxTransferPos);
        }


    }

    /// Class that contains actions for claw.
    public static class RobotClaw {
        Servo clawLeft, clawRight;

        // constructor
        private RobotClaw(Servo clawLeft, Servo clawRight){
            this.clawLeft = clawLeft;
            this.clawRight = clawRight;
        }

        // rr actions
        public class RoadRunner{
            // close action
            public class Close implements Action{

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    close();
                    return false;
                }
            }
            // method that instantiates for ease of use
            public Action rRClose(){return new Close();}

            // open action
            public class Open implements Action{

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    open();
                    // wait for the claws to open
                    return false;
                }
            }
            // method that instantiates for ease of use
            public Action rROpen(){return new Open();}
        }

        /// Close the claw.
        public void close(){
            clawLeft.setPosition(clawLeftClosed);
            clawRight.setPosition(clawRightClosed);
        }
        /// Open the claw.
        public void open(){
            clawLeft.setPosition(clawLeftOpen);
            clawRight.setPosition(clawRightOpen);
        }
    }

    /// Class that contains actions for intake.
    public static class RobotIntake {
        DcMotor intakeMotor;

        // constructor
        private RobotIntake(DcMotor intakeMotor){this.intakeMotor = intakeMotor;}

        /// Set intake to 'in' power.
        public void in(){
            intakeMotor.setPower(intakeInPower);
        }

        /// Set intake to 'out' power
        public void out(){
            intakeMotor.setPower(intakeOutPower);
        }

        /// Set intake power to zero.
        public void stop(){
            intakeMotor.setPower(0);
        }
    }

    /// Class that contains actions for lift.
    public static class RobotLift {
        DcMotor liftMotor;

        // constructor
        private RobotLift(DcMotor liftMotor){this.liftMotor = liftMotor;}

        // rr actions
        public class RoadRunner{
            // above chamber action
            public class AboveChamber implements Action{
                boolean wait;
                public AboveChamber(boolean wait){this.wait = wait;}

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    runToPosition(liftAboveChamberPos);
                    return wait && !motorAtTarget(liftMotor);
                }
            }
            // methods that instantiate aboveChamber for convenience
            public Action aboveChamber(boolean wait){return new AboveChamber(wait);}
            public Action aboveChamber(){return new AboveChamber(false);}

            // wall action
            public class Wall implements Action{
                boolean wait;
                public Wall(boolean wait){this.wait = wait;}

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    runToPosition(liftWallPos);
                    return wait && !motorAtTarget(liftMotor);
                }
            }
            // methods that instantiate wall for convenience
            public Action wall(boolean wait){return new Wall(wait);}
            public Action wall(){return new Wall(false);}

            // above wall action
            public class AboveWall implements Action{
                boolean wait;
                public AboveWall(boolean wait){this.wait = wait;}

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    runToPosition(liftAboveWallPos);
                    return wait && !motorAtTarget(liftMotor);
                }
            }
            // methods that instantiate above wall for convenience
            public Action aboveWall(boolean wait){return new AboveWall(wait);}
            public Action aboveWall(){return new AboveWall(false);}

            // below chamber action
            public class BelowChamber implements Action{
                boolean wait;
                public BelowChamber(boolean wait){this.wait = wait;}

                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    runToPosition(liftBelowChamberPos);
                    return wait && !motorAtTarget(liftMotor);
                }
            }
            // methods that instantiate above below chamber convenience
            public Action belowChamber(boolean wait){return new BelowChamber(wait);}
            public Action belowChamber(){return new BelowChamber(false);}

        }

        /// Move lift above chamber.
        public void aboveChamber(){
            runToPosition(liftAboveChamberPos);
        }

        /// Move lift to wall height.
        public void wall(){
            runToPosition(liftWallPos);
        }

        /// Move lift above wall.
        public void aboveWall(){
            runToPosition(liftAboveWallPos);
        }

        /// Move lift below chamber.
        public void belowChamber(){
            runToPosition(liftBelowChamberPos);
        }

        /** Move to position and reduce speed if going down.
         *
         * @param target desired position in ticks.
         */
        public void runToPosition(int target){
            if(target < getPosition()){ // target is down
                motorRunToPosition(liftMotor, target, liftDownwardPower);
            }
            else{ //target is up
                motorRunToPosition(liftMotor, target, liftUpwardPower);
            }
        }

        /** Move to position using runMode RUN_TO_POSITION
         *
         * @param target desired position in ticks.
         * @param power desired power.
         */
        public void runToPosition(int target, double power){
            motorRunToPosition(liftMotor, target, power);
        }

        /// Return lift position in ticks.
        public int getPosition(){
            return liftMotor.getCurrentPosition();
        }

        /** Set ZPB for lift.
         *
         * @param ZPB behavior.
         */
        public void setZPB(DcMotor.ZeroPowerBehavior ZPB){
            if(liftMotor.getZeroPowerBehavior() != ZPB){
                liftMotor.setZeroPowerBehavior(ZPB);
            }
        }

        /** Set RunMode for lift.
         *
         * @param runMode desired runMode.
         * */
        public void setRunMode(DcMotor.RunMode runMode){
            if(liftMotor.getMode() != runMode){
                liftMotor.setMode(runMode);
            }
        }

        /** Set power for lift.
         *
         * @param power desired power.
         */
        public void setPower(double power){
            liftMotor.setPower(power);
        }
    }

    /// Class that contains actions for slide.
    public static class RobotSlide {
        DcMotor activeSlide;

        // constructor
        public RobotSlide(DcMotor activeSlide){this.activeSlide = activeSlide;}

        /** Move slide to given position.
         * @param target desired position.
         * @param power motor power.
         */
        public void runToPosition(int target, double power){
            motorRunToPosition(activeSlide, target, power);
        }

        /// Return slide position in ticks.
        public int getPosition(){
            return activeSlide.getCurrentPosition();
        }

        /** Set ZPB for slide.
         * @param ZPB Behavior.
         */
        public void setZPB(DcMotor.ZeroPowerBehavior ZPB){
            if(activeSlide.getZeroPowerBehavior() != ZPB){
                activeSlide.setZeroPowerBehavior(ZPB);
            }
        }

        /** Set RunMode for slide.
         * @param runMode desired runMode.
         */
        public void setRunMode(DcMotor.RunMode runMode){
            if(activeSlide.getMode() != runMode){
                activeSlide.setMode(runMode);
            }
        }

        /** Set power for slide.
         * @param power desired power.
         */
        public void setPower(double power){
            activeSlide.setPower(power);
        }
    }

    /** Uses DcMotor RunMode RUN_TO_POSITION to move motor.
     *
     * @param motor motor to move.
     * @param target target position (in ticks).
     * @param power power for motor.
     */
    private static void motorRunToPosition(DcMotor motor, int target, double power){
        motor.setTargetPosition(target);
        if(motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        motor.setPower(power);
    }

    /** Returns if the motor is within 5 ticks of target
     *
     * @param motor motor to check
     * @return if motor is at target
     */
    private static boolean motorAtTarget(DcMotor motor){
        return motor.getCurrentPosition() <= motor.getTargetPosition() +5
                && motor.getCurrentPosition() >= motor.getTargetPosition() -5;
    }
}