package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Telemetry Testing")
public class TelemetryTesting extends LinearOpMode {
    private Configuration robot;
        //everything in here needs scrapped,,, very bad
    //just copy paste from teleOp and add telemetry!

    private int targetPos = 0;


    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init();

        robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addLine("i reset :)");
        telemetry.update();



        waitForStart();


        while(opModeIsActive()) {
            telemetry.addData("Current position", robot.verticalMotor.getCurrentPosition());
            telemetry.update();
            //5050 is top
        }
    }
}
