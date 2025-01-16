package org.firstinspires.ftc.teamcode.TweetyBirdExample;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;
import dev.narlyx.tweetybird.TweetyBird;

//This defines a basic configuration of TweetyBird
public class ConfigurationExample {

    private LinearOpMode opMode;
    public ThreeWheeled odometer; //Used to define three odom pods
    public Mecanum mecanum; //Used to define wheel setup
    public TweetyBird tweetyBird; //Used to construct TweetyBird
    public ConfigurationExample(LinearOpMode opMode){
        this.opMode = opMode;
    }

    //Define your drive motors
    public DcMotor fr, fl, bl, br;

    public void init(){
        //Define HardwareMap
        HardwareMap hwMap=opMode.hardwareMap;

        //Standard DcMotor Setup
        fl = hwMap.get(DcMotor.class,"FL");
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fr = hwMap.get(DcMotor.class,"FR");
        fr.setDirection(DcMotor.Direction.REVERSE);
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

        //Define your wheels
        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fr)
                .setFrontRightMotor(fl)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();

        //Define the odometer pods
        odometer = new ThreeWheeled.Builder()
                .setLeftEncoder(bl)
                .setRightEncoder(fr)
                .setMiddleEncoder(br)

                //Define a rotation of your odometer pod
                //Currently set for GoBilda Swingarm (48mm)
                .setEncoderTicksPerRotation(2000)
                .setEncoderWheelRadius(0.944882)

                //Flips the Encoder direction
                //Can test directions using robot.odometer.getX(), getY(), and getZ() in an opMode
                .setFlipLeftEncoder(false)
                .setFlipRightEncoder(true)
                .setFlipMiddleEncoder(false)

                //Distance between left and right encoders (Ours is in inches)
                .setSideEncoderDistance(12.75)
                //Distance from center of rotation to middle encoder
                .setMiddleEncoderOffset(9.75)
                .build();

        //I like to reset the robot position when it starts
        odometer.resetTo(0,0,0);
    }

    //The init for TweetyBird
    public void initTweetyBird(){
        tweetyBird = new TweetyBird.Builder()
                // Your configuration options here
                .setDistanceBuffer(1) //Defines how close TweetyBird needs to be to consider itself okay to stop moving (Default is 1)
                .setDriver(mecanum)
                .setLinearOpMode(opMode)
                .setMaximumSpeed(0.8) //Defines how fast TweetyBird is allowed to move
                .setMinimumSpeed(0.2) //Defines the minimum speed TweetyBird is allowed to move
                .setOdometer(odometer)
                .setRotationBuffer(1) //Like DistanceBuffer but for Rotation (Default is 1)
                .setDebuggingEnabled(false) //If true it will flood console with debugging info
                .build();
    }

}
