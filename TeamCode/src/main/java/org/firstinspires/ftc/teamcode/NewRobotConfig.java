package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;
import dev.narlyx.tweetybird.TweetyBird;


public class NewRobotConfig {
    private LinearOpMode opMode;

    //Global Variables
    public DcMotor fl, fr, bl, br, leftHorizontal, rightHorizontal, verticalMotor;

    public IMU imu;

    boolean flipLeftEncoder;
    boolean flipRightEncoder;
    boolean flipMiddleEncoder;

    public ThreeWheeled odometer;

    public Mecanum mecanum;
    public TweetyBird tweetyBird;

    public NewRobotConfig(LinearOpMode opMode) {
        this.opMode = opMode;
    }


    public void init(boolean autoConfig) {
        HardwareMap hwMap = opMode.hardwareMap;

        fl = hwMap.get(DcMotor.class, "FL");
        fl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fr = hwMap.get(DcMotor.class, "FR");
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bl = hwMap.get(DcMotor.class, "BL");
        bl.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        br = hwMap.get(DcMotor.class, "BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftHorizontal = hwMap.get(DcMotor.class,"HorzLeft");
        leftHorizontal.setDirection(DcMotor.Direction.REVERSE);
        leftHorizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightHorizontal = hwMap.get(DcMotor.class,"HorzRight");
        rightHorizontal.setDirection(DcMotor.Direction.FORWARD);
        rightHorizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        verticalMotor = hwMap.get(DcMotor.class,"Vert");
        verticalMotor.setDirection(DcMotor.Direction.FORWARD);
        verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*
        IMU = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot( //Mes swith me
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        IMU.initialize(parameters);*/


        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fl)
                .setFrontRightMotor(fr)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();


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
}
