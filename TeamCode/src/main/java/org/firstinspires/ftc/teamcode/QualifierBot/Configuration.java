package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.TweetyBird;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;


public class Configuration {
    private LinearOpMode opMode;

    //Global Variables
    public int vertMax = 5050, horzMax = 400;
    public int intakeOnDistance = 250;
    public int vertAboveChamber = 4070, vertWall = 1635, vertBelowChamber = 3811;


    public DcMotor fr, fl, bl, br, horzMotor, vertMotor, intakeMotor;

    public Servo flipServo, redServo, blueServo, boxServo; //flipservo

    public TouchSensor verticalLimiter, horizontalLimiter, frontSensor;

    public IMU imu;

    public RevColorSensorV3 blonker;

    boolean flipLeftEncoder;
    boolean flipRightEncoder;
    boolean flipMiddleEncoder;

    //The light on color sensor is controlled via a switch on the device



    public ThreeWheeled odometer;
    private boolean leftEncoder, rightEncoder, middleEncoder;

    public Mecanum mecanum;
    public TweetyBird tweetyBird;


    public Configuration(LinearOpMode opMode){
        this.opMode=opMode;
    }


    public void init(boolean autoConfig){
        HardwareMap hwMap=opMode.hardwareMap;

        fl = hwMap.get(DcMotor.class,"FL");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fr = hwMap.get(DcMotor.class,"FR");
        fr.setDirection(DcMotor.Direction.FORWARD);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bl = hwMap.get(DcMotor.class,"BL");
        bl.setDirection(DcMotor.Direction.FORWARD);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        br = hwMap.get(DcMotor.class,"BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        horzMotor = hwMap.get(DcMotor.class,"Horz");
        horzMotor.setDirection(DcMotor.Direction.FORWARD);
        horzMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horzMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        vertMotor = hwMap.get(DcMotor.class,"Vert");
        vertMotor.setDirection(DcMotor.Direction.REVERSE);
        vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        intakeMotor = hwMap.get(DcMotor.class, "In");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        verticalLimiter = hwMap.get(TouchSensor.class, "VerticalLimiter");
        horizontalLimiter = hwMap.get(TouchSensor.class, "HorizontalLimiter");
        frontSensor = hwMap.get(TouchSensor.class, "frontSensor");

        flipServo = hwMap.get(Servo.class, "Flip");
        redServo = hwMap.get(Servo.class, "Red");
        blueServo = hwMap.get(Servo.class, "Blue");
        boxServo = hwMap.get(Servo.class, "Box");

        blonker = hwMap.get(RevColorSensorV3.class, "sensor_color");


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


        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot( //Mes swith me
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(parameters);


        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fl)
                .setFrontRightMotor(fr)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();



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

        odometer.resetTo(0,0,0);
    }

    public void closeClaw() {
        redServo.setPosition(0.67);//Bigger more close (Right servo)
        blueServo.setPosition(0.33); //less more close (Left servo)
        //telemetry.addLine("Red and Blue closed");
    }

    public void openClaw(){
        redServo.setPosition(0.4);
        blueServo.setPosition(0.6);
    }



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

}


