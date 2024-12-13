package org.firstinspires.ftc.teamcode;

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
    public int vertMax = 6100, horzMax = 400;
    public int intakeOnDistance = 150;

    public DcMotor fl, fr, bl, br, horizontalMotor, verticalMotor, intakeMotor;

    public Servo flipServo, redServo, blueServo, boxServo; //flipservo

    public TouchSensor verticalLimiter, horizontalLimiter, frontSensor;

    public IMU imu;

    public ThreeWheeled odometer;

    private boolean leftEncoder, rightEncoder, middleEncoder;



    public Configuration(LinearOpMode opMode){
        this.opMode=opMode;
    }


    public void init(boolean autoConfig){
        HardwareMap hwMap=opMode.hardwareMap;

        fl = hwMap.get(DcMotor.class,"FR");
        fl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fr = hwMap.get(DcMotor.class,"FL");
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
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


        horizontalMotor = hwMap.get(DcMotor.class,"Horz");
        horizontalMotor.setDirection(DcMotor.Direction.FORWARD);
        horizontalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        verticalMotor = hwMap.get(DcMotor.class,"Vert");
        verticalMotor.setDirection(DcMotor.Direction.REVERSE);
        verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


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


        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot( //Mes swith me
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(parameters);

        /*
        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fl)
                .setFrontRightMotor(fr)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();

         */


        if(autoConfig){
            leftEncoder = false;
            rightEncoder = true;
            middleEncoder = false;
        }
        else{
            leftEncoder = true; //was true
            rightEncoder = false; //was false
            middleEncoder = false;
        }

        odometer = new ThreeWheeled.Builder()
                .setLeftEncoder(bl)
                .setRightEncoder(fl)
                .setMiddleEncoder(br)

                .setEncoderTicksPerRotation(2000)
                .setEncoderWheelRadius(0.944882)

                //Change the true/false values to correct directions
                .setFlipLeftEncoder(true)  //false for auto
                .setFlipRightEncoder(false) //true for auto
                .setFlipMiddleEncoder(false) //false for auto

                .setSideEncoderDistance(12.75)
                .setMiddleEncoderOffset(9.75)
                .build();

        odometer.resetTo(0,0,0);
    }

    public void closeClaw() {
        redServo.setPosition(0.625);//Bigger more close
        blueServo.setPosition(0.325); //less more close
        //telemetry.addLine("Red and Blue closed");
    }

    public void openClaw(){
        redServo.setPosition(0.5);
        blueServo.setPosition(0.5);
    }

    public void setVerticalPosition(int targetPosition){

        //outside of while
        verticalMotor.setPower(0);
        verticalMotor.setTargetPosition(targetPosition);

        //Set motor in RunToPosition mode
        if(verticalMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        double startTime = opMode.getRuntime(); //(out of While)
        double changeInTime = opMode.getRuntime() - startTime; //Change in time (In While)

        double kP = 0; //Proportional Gain  (out of while
        double kI = 0; //Integral Gain      (out of while
        double kD = 0; //Derivative Gain    (out of while

        double currentEncoderPosition = verticalMotor.getCurrentPosition(); //Within while
        double error = targetPosition - currentEncoderPosition;             //Within while
        double sumOfErrors = error * changeInTime; //Error value * change in time //Within while
        double rateOfChangeOfError; //  Within while

        double motorPower = kP * error + kI * sumOfErrors + kD * rateOfChangeOfError; //within while


        verticalMotor.setPower(motorPower);
    }













/*
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

 */


}
