package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="testTeleop")

@Disabled

public class TestTeleop extends LinearOpMode {

    private HardwareMap robot = null;

    @Override
    public void runOpMode() {
        robot = new HardwareMap(this);
        robot.init();

        waitForStart();

        while (opModeIsActive()) {
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            double power = gamepad1.right_trigger;

            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            robot.fl.setPower(leftFrontPower*power);
            robot.fr.setPower(rightFrontPower*power);
            robot.bl.setPower(leftBackPower*power);
            robot.br.setPower(rightBackPower*power);

           /* if (gamepad1.left_bumper) {
                robot.intake.setPower(-gamepad1.left_trigger);
            } else {
                robot.intake.setPower(gamepad1.left_trigger);
            }

            */


            telemetry.addData("Trigger: ",gamepad1.right_trigger);
            telemetry.addData("Bumper: ",gamepad1.left_bumper);
            telemetry.addData("LF: ",leftFrontPower*power);
            telemetry.addData("FR: ",rightFrontPower*power);
            telemetry.addData("BL: ",leftBackPower*power);
            telemetry.addData("BR: ",rightBackPower*power);

            telemetry.update();

        }

    }
}
