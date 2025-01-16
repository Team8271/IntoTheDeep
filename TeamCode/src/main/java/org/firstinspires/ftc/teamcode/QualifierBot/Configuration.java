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
    public int vertAboveChamber = 4153, vertWall = 1258, vertBelowChamber = 3100;

    double clawClosedValue = 0.67; //Larger # = More Closed (0-1)
    double clawOpenValue = 0.4; //Smaller # = More Open (0-1)


    public DcMotor fr, fl, bl, br, horzMotor, vertMotor, intakeMotor;

    public Servo flipServo, redServo, blueServo, boxServo;

    public TouchSensor verticalLimiter, horizontalLimiter, frontSensor;


    public RevColorSensorV3 blonker;


    //The light on color sensor is controlled via a switch on the device



    public ThreeWheeled odometer;
    private boolean leftEncoder, rightEncoder, middleEncoder;

    public Mecanum mecanum;
    public TweetyBird tweetyBird;


    public Configuration(LinearOpMode opMode){
        this.opMode=opMode;
    }


    public void init(){
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
        vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


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




        /*
        need to not init imu :checkmark:
        need to init box servo on auto :checkmark:
        need dpad down to always bring intake for high basket
         */


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
                .setFlipLeftEncoder(false)  //false for auto
                .setFlipRightEncoder(true) //true for auto
                .setFlipMiddleEncoder(false) //false for auto

                .setSideEncoderDistance(12.75)
                .setMiddleEncoderOffset(9.75)
                .build();

        odometer.resetTo(0,0,0);
    }

    public void closeClaw(){
        double redServoClosed = clawClosedValue;
        double blueServoClosed = 1-clawClosedValue;
        redServo.setPosition(redServoClosed);
        blueServo.setPosition(blueServoClosed);
        opMode.telemetry.addLine("Claw Closed");
    }

    public void openClaw(){
        double redServoOpen = clawOpenValue;
        double blueServoOpen = 1-clawOpenValue;
        redServo.setPosition(redServoOpen);
        blueServo.setPosition(blueServoOpen);
        opMode.telemetry.addLine("Claw Opened");
    }



    public void initTweatyBird(){
        tweetyBird = new TweetyBird.Builder()
                // Your configuration options here
                .setDistanceBuffer(1) //inches
                .setDriver(mecanum)
                .setLinearOpMode(opMode)
                .setMaximumSpeed(1)
                .setMinimumSpeed(0.3)
                .setOdometer(odometer)
                .setRotationBuffer(5)
                .setDebuggingEnabled(false)
                .build();
    }

}


