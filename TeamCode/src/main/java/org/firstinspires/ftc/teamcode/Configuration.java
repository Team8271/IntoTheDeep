package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.hardware.motors.GoBILDA5202Series;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Configuration {
    private LinearOpMode opMode;

    public DcMotor FL, FR, BL, BR, Horz, Vert, In;

    public Servo Flip, Red, Blue, Org;

    public TouchSensor Max = null, Min = null;


    public Configuration(LinearOpMode opMode){
        this.opMode=opMode;
    }
    public void init(){
        HardwareMap hwMap=opMode.hardwareMap;

        FL = hwMap.get(DcMotor.class,"FL");
        FL.setDirection(DcMotor.Direction.FORWARD);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FR = hwMap.get(DcMotor.class,"FR");
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BL = hwMap.get(DcMotor.class,"BL");
        BL.setDirection(DcMotor.Direction.REVERSE);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BR = hwMap.get(DcMotor.class,"BR");
        BR.setDirection(DcMotor.Direction.FORWARD);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Horz = hwMap.get(DcMotor.class,"Horz");
        Horz.setDirection(DcMotor.Direction.FORWARD);
        Horz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Vert = hwMap.get(DcMotor.class,"Vert");
        Vert.setDirection(DcMotor.Direction.FORWARD);
        Vert.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        In =hwMap.get(DcMotor.class, "In");
        In.setDirection(DcMotorSimple.Direction.FORWARD);
        In.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        Max = hwMap.get(TouchSensor.class, "Max");

        Min = hwMap.get(TouchSensor.class, "Min");



        Flip = hwMap.get(Servo.class, "Flip");

        Red = hwMap.get(Servo.class, "Red");

        Blue = hwMap.get(Servo.class, "Blue");

        Org = hwMap.get(Servo.class, "Org");



    }
}
