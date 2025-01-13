package org.firstinspires.ftc.teamcode.OpenHouse;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import dev.narlyx.tweetybird.Drivers.Mecanum;
import dev.narlyx.tweetybird.Odometers.ThreeWheeled;
import dev.narlyx.tweetybird.TweetyBird;

public class OpenHouseConfig {
    private final LinearOpMode opMode;

    //Global Variables
    public int vertAboveChamber = 4070, vertWall = 1635,
            vertBelowChamber = 3811, intakeOnDistance = 270,
            horzMax = 400;
    double clawClosedValue = 0.625; //Larger # = More Closed (0-1)
    double clawOpenValue = 0.5; //Smaller # = More Open (0-1)

    public DcMotor fl, fr, bl, br, horzMotor, vertMotor, intakeMotor;
    public Servo flipServo, redServo, blueServo, boxServo;
    public TouchSensor vertLimiter, horzLimiter, frontSensor;

    public ThreeWheeled odometer;
    public Mecanum mecanum;
    public TweetyBird tweetyBird;

    boolean flipLeftEncoder;
    boolean flipRightEncoder;
    boolean flipMiddleEncoder;

    public OpenHouseConfig(LinearOpMode opMode){this.opMode=opMode;}

    public void init(boolean autoConfig){
        HardwareMap hwMap=opMode.hardwareMap;

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

        vertLimiter = hwMap.get(TouchSensor.class, "VerticalLimiter");
        horzLimiter = hwMap.get(TouchSensor.class, "HorizontalLimiter");
        frontSensor = hwMap.get(TouchSensor.class, "frontSensor");

        flipServo = hwMap.get(Servo.class, "Flip");
        redServo = hwMap.get(Servo.class, "Red");
        blueServo = hwMap.get(Servo.class, "Blue");
        boxServo = hwMap.get(Servo.class, "Box");


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

        mecanum = new Mecanum.Builder()
                .setFrontLeftMotor(fr)
                .setFrontRightMotor(fl)
                .setBackLeftMotor(bl)
                .setBackRightMotor(br)
                .build();



        odometer = new ThreeWheeled.Builder()
                .setLeftEncoder(bl)
                .setRightEncoder(fr)
                .setMiddleEncoder(br)

                .setEncoderTicksPerRotation(2000)
                .setEncoderWheelRadius(0.944882)

                //Change the true/false values to correct directions
                .setFlipLeftEncoder(flipLeftEncoder)
                .setFlipRightEncoder(flipRightEncoder)
                .setFlipMiddleEncoder(flipMiddleEncoder)

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

    public void resetVert(){ //Bring Vert all the way down and reset encoder
        DcMotor.RunMode runMode = vertMotor.getMode(); //Store runMode
        if(vertMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        vertMotor.setPower(-0.6);
        while(!vertLimiter.isPressed() && opMode.opModeIsActive()){//May cause problems
            opMode.sleep(100); //wait for vertLimiter
        }
        vertMotor.setPower(0);
        vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertMotor.setMode(runMode);
    }//Bring vertMotor down to Lim and reset Encoder

    public void initTweatyBird(){
        tweetyBird = new TweetyBird.Builder()
                // Your configuration options here
                .setDistanceBuffer(1)
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
