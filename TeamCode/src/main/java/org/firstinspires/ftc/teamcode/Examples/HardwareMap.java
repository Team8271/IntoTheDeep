package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareMap {
    private LinearOpMode opMode = null;

    public DcMotor fl;
    //public DcMotor intake;
    public DcMotor bl;
    public DcMotor fr;
    public DcMotor br;
    public DcMotor leftArm, rightArm;

    public Servo claw, armS;





    public HardwareMap(LinearOpMode opMode) {
        this.opMode = opMode;
    }
    public void init() {
        com.qualcomm.robotcore.hardware.HardwareMap hardwareMap = opMode.hardwareMap;

        fl = hardwareMap.get(DcMotor.class, "FL");
        fl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hardwareMap.get(DcMotor.class, "BL");
        bl.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hardwareMap.get(DcMotor.class, "FR");
        fr.setDirection(DcMotor.Direction.FORWARD);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hardwareMap.get(DcMotor.class, "BR");
        br.setDirection(DcMotor.Direction.FORWARD);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftArm = hardwareMap.get(DcMotor.class, "leftArm");
        leftArm.setDirection(DcMotor.Direction.FORWARD);
        leftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightArm = hardwareMap.get(DcMotor.class, "rightArm");
        rightArm.setDirection(DcMotor.Direction.FORWARD);
        rightArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        claw = hardwareMap.get(Servo.class, "claw");
        claw.setDirection(Servo.Direction.REVERSE);
        //school bot
        armS = hardwareMap.get(Servo.class, "armS");
        armS.setDirection(Servo.Direction.REVERSE);
        //school bot

       // intake = hardwareMap.get(DcMotor.class, "Intake");
       // intake.setDirection(DcMotorSimple.Direction.REVERSE);

    }
}
