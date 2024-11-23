package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Telemetry Testing")
public class TelemetryTesting extends LinearOpMode {
    private Configuration robot;

    //2760 for wall

    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(false);

        waitForStart();

        robot.verticalMotor.setTargetPosition(2760);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(0.5);


        robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(opModeIsActive()){
            telemetry.addData("Vertical Motor:", robot.verticalMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
