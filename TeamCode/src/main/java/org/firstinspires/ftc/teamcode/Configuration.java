package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Configuration {
    private LinearOpMode opMode;

    //Global Variables
    public int vertMax = 1300, horzMax = 400;
    public int intakeOnDistance = 200;

    public DcMotor fl, fr, bl, br, horizontalMotor, verticalMotor, intakeMotor;

    public Servo flipServo, redServo, blueServo, boxServo;

    public TouchSensor verticalLimiter, horizontalLimiter;

    public IMU imu;

    public Configuration(LinearOpMode opMode){
        this.opMode=opMode;
    }


    public void init(){
        HardwareMap hwMap=opMode.hardwareMap;

        fl = hwMap.get(DcMotor.class,"FL");
        fl.setDirection(DcMotor.Direction.FORWARD);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hwMap.get(DcMotor.class,"FR");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hwMap.get(DcMotor.class,"BL");
        bl.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hwMap.get(DcMotor.class,"BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        horizontalMotor = hwMap.get(DcMotor.class,"Horz");
        horizontalMotor.setDirection(DcMotor.Direction.FORWARD);
        horizontalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        verticalMotor = hwMap.get(DcMotor.class,"Vert");
        verticalMotor.setDirection(DcMotor.Direction.REVERSE);
        verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotor = hwMap.get(DcMotor.class, "In");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        verticalLimiter = hwMap.get(TouchSensor.class, "VertSens");
        horizontalLimiter = hwMap.get(TouchSensor.class, "HorzSens");

        flipServo = hwMap.get(Servo.class, "Flip");
        redServo = hwMap.get(Servo.class, "Red");
        blueServo = hwMap.get(Servo.class, "Blue");
        boxServo = hwMap.get(Servo.class, "Box");

        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot( //Mes swith me
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );
        imu.initialize(parameters);

    }
}
