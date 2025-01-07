package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;
import dev.narlyx.tweetybird.TweetyBird;


public class NewRobotConfig {
    private final LinearOpMode opMode;

    //**Easily changeable values**\\
    //Vertical pre-positions
    private final int aboveChamber = 1234, belowChamber = 1200;
    //Claw values (0-1 less-more closed)
    private final double clawFullyClosed = 1, clawFullyOpen = 0;



    //Global Variables
    public DcMotor fl, fr, bl, br, leftHorz, rightHorz, vertMotor; //Motors
    public Servo VertFlipServo, HorzFlipServo, clawLeftServo,
            clawRightServo, intakeServo, wristServo; //Servos
    private double clawLeftOpen, clawRightOpen, clawLeftClosed, clawRightClosed; //Claw Values


    //TweatyBird Values
    boolean flipLeftEncoder;        //Used for flipping odom pods
    boolean flipRightEncoder;       //Used for flipping odom pods
    boolean flipMiddleEncoder;      //Used for flipping odom pods
    public ThreeWheeled odometer;   //Used for setting up odom pods
    public Mecanum mecanum;         //Used for mecanum wheels for TweetyBird
    public TweetyBird tweetyBird;   //Used for tweetyBird setup

    //Let the config refer to opMode
    public NewRobotConfig(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    //Init method
    public void init(boolean autoConfig) {
        //Used to define all motors and servos
        HardwareMap hwMap = opMode.hardwareMap;

        //Define Motors
        //Define the front left wheel
        fl = hwMap.get(DcMotor.class, "FL");
        fl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Define the front right wheel
        fr = hwMap.get(DcMotor.class, "FR");
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Define the back left wheel
        bl = hwMap.get(DcMotor.class, "BL");
        bl.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Define the back right wheel
        br = hwMap.get(DcMotor.class, "BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Defining the Horizontal Left Motor
        leftHorz = hwMap.get(DcMotor.class,"HorzLeft");
        leftHorz.setDirection(DcMotor.Direction.REVERSE);
        leftHorz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftHorz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Defining the Horizontal Right Motor
        rightHorz = hwMap.get(DcMotor.class,"HorzRight");
        rightHorz.setDirection(DcMotor.Direction.FORWARD);
        rightHorz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHorz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Defining the Vertical Motor
        vertMotor = hwMap.get(DcMotor.class,"Vert");
        vertMotor.setDirection(DcMotor.Direction.FORWARD);
        vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Define Servos
        VertFlipServo = hwMap.get(Servo.class, "VertFlip");
        HorzFlipServo = hwMap.get(Servo.class, "HorzFlip");
        clawLeftServo = hwMap.get(Servo.class, "clawLeft");
        clawRightServo = hwMap.get(Servo.class, "clawRight");
        intakeServo = hwMap.get(Servo.class, "intakeServo");
        wristServo = hwMap.get(Servo.class, "wristServo");

        //Define Claw Positions
        clawLeftClosed = 1-clawFullyClosed;
        clawLeftOpen = 1-clawFullyOpen;
        clawRightClosed = clawFullyClosed;
        clawRightOpen = clawFullyOpen;


        //Used to define wheels in TweetyBird
        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fl)
                .setFrontRightMotor(fr)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();

        //Uses custom flip values per teleop due to rough programming
        if(autoConfig){
            flipLeftEncoder = false;
            flipRightEncoder = true;
            flipMiddleEncoder = false;
        }
        else{
            flipLeftEncoder = true;
            flipRightEncoder = false;
            flipMiddleEncoder = false;
        }

        //Odometer sets up TweetyBird Odometry Pods
        odometer = new ThreeWheeled.Builder()
                .setLeftEncoder(bl)
                .setRightEncoder(fl)
                .setMiddleEncoder(br)

                .setEncoderTicksPerRotation(2000)
                .setEncoderWheelRadius(0.944882)

                //Change the true/false values to correct directions
                .setFlipLeftEncoder(flipLeftEncoder)  //false for auto     true
                .setFlipRightEncoder(flipRightEncoder) //true for auto       false
                .setFlipMiddleEncoder(flipMiddleEncoder) //false for auto    false for teleop

                .setSideEncoderDistance(12.75)
                .setMiddleEncoderOffset(9.75)
                .build();

        //Ensures that the Odometry is set at 0 when it starts
        odometer.resetTo(0,0,0);

        //Define thread1
        PIDControl thread1 = new PIDControl(opMode);


    }

    //Init TweatyBird
    public void initTweatyBird(){
        tweetyBird = new TweetyBird.Builder()
                // Your configuration options here
                .setDistanceBuffer(1) //inches
                .setDriver(mecanum)
                .setLinearOpMode(opMode)
                .setMaximumSpeed(0.5)
                .setMinimumSpeed(0.2)
                .setOdometer(odometer)
                .setRotationBuffer(1)
                .setDebuggingEnabled(true)
                .build();



    }

    public void closeClaw(){
        clawLeftServo.setPosition(clawLeftClosed); //Set left servo to close position
        clawRightServo.setPosition(clawRightClosed); //Set right servo to close position
    }

    public void openClaw(){
        clawLeftServo.setPosition(clawLeftOpen); //Set left servo to open position
        clawRightServo.setPosition(clawRightOpen); //Set right servo to open position
    }

    //Used to start the PID
    public void PIDRun(){
        PIDControl thread1 = new PIDControl(opMode);
        opMode.telemetry.addLine("Starting PIDControl thread");
        thread1.start();

    }


    public void clipSpecimen(){
        PIDControl thread1 = new PIDControl(opMode);
        //clips the specimen on high chamber
        thread1.start();
        thread1.targetPosition = aboveChamber; //ensure its in correct position
        while(thread1.isBusy){
            opMode.telemetry.addLine("Waiting for thread1");
            opMode.telemetry.update();
        }
        thread1.targetPosition = belowChamber;
        openClaw();
        //thread1.interrupt();//add some de-buggin stuff here but not now lol
    }



}
