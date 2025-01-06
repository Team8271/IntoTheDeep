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

    //Global Variables
    public DcMotor fl, fr, bl, br, leftHorz, rightHorz, vertMotor;
    public Servo VertFlipServo, HorzFlipServo, clawLeftServo, clawRightServo, intakeServo;

    //TweatyBird Odometry Pod Flipping values
    boolean flipLeftEncoder;
    boolean flipRightEncoder;
    boolean flipMiddleEncoder;

    public ThreeWheeled odometer;

    public Mecanum mecanum;
    public TweetyBird tweetyBird;

    //Let the config refer to opMode
    public NewRobotConfig(LinearOpMode opMode) {
        this.opMode = opMode;
    }


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

    }
}
