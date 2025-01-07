package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Telemetry Testing")
public class TelemetryTesting extends LinearOpMode {
    private NewRobotConfig robot;
        //everything in here needs scrapped,,, very bad
    //just copy paste from teleOp and add telemetry!

    private int targetPos = 0;


    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new NewRobotConfig(this);
        robot.init(false);


        telemetry.addLine("Initialized Config");
        robot.PIDRun();
        telemetry.update();
        int target = robot.vertMotor.getCurrentPosition();

        waitForStart();


        while(opModeIsActive()) {
            if(gamepad1.dpad_up){
                target++;
            }
            if(gamepad1.dpad_down){
                target--;
            }
            telemetry.addData("Target position", target);
            telemetry.addData("Current position", robot.vertMotor.getCurrentPosition());
            telemetry.update();
            //5050 is top
            //Try 4761 for above high chamber
        }
    }
}
