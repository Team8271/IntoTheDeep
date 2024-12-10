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
        robot.init(false);
        robot.verticalMotor.setTargetPosition(0);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(0);
        telemetry.addLine("I move when you start me!");
        telemetry.update();


        waitForStart();
        robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.horizontalMotor.setTargetPosition(40);
        robot.horizontalMotor.setPower(0);

        while(opModeIsActive()){
            while(gamepad2.x && opModeIsActive() && robot.verticalMotor.getCurrentPosition() < 6000){
                targetPos+=2;
                robot.verticalMotor.setTargetPosition(targetPos);
                if(robot.verticalMotor.getPower() != 1){
                    robot.verticalMotor.setPower(1);
                }
            }
            if(gamepad2.a && !robot.verticalLimiter.isPressed()){
                robot.verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.verticalMotor.setPower(-0.6);
            }
            else {
                robot.verticalMotor.setPower(0);
            }
            if(robot.verticalLimiter.isPressed()){
                robot.verticalMotor.setPower(0);
                robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.verticalMotor.setTargetPosition(0);
                robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if(robot.verticalMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION){
                robot.verticalMotor.setTargetPosition(targetPos);
            }


            telemetry.addData("Vertical Motor:", robot.verticalMotor.getCurrentPosition());
            telemetry.update();
        }

    }
}
