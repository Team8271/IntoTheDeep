package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Main Teleop")
public class Teleop extends LinearOpMode {
    private Configuration robot;

    boolean pincherControl = false;
    private ElapsedTime time = new ElapsedTime();
    double startTime;

    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(false);


        telemetry.addLine("Initialized");
        telemetry.update();
        waitForStart();
        telemetry.clearAll();

        while (opModeIsActive()){

        }
    }
}

