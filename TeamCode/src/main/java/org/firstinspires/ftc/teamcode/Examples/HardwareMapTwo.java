package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;



public class HardwareMapTwo {
    private LinearOpMode opMode = null;

    public DcMotor fl;
    //public DcMotor intake;
    public DcMotor bl;
    public DcMotor fr;
    public DcMotor br;

    public HardwareMapTwo(LinearOpMode opMode) {
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



       // intake = hardwareMap.get(DcMotor.class, "Intake");
       // intake.setDirection(DcMotorSimple.Direction.REVERSE);

    }
}
