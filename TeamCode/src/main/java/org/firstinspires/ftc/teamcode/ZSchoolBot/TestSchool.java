package org.firstinspires.ftc.teamcode.ZSchoolBot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="testSchool")

@Disabled

public class TestSchool extends LinearOpMode {

    private HardwareMap robot = null;

    @Override
    public void runOpMode() {
        robot = new HardwareMap(this);
        robot.init();

        robot.claw.setPosition(.25);
        robot.armS.setPosition(.75);


        waitForStart();

        while (opModeIsActive()) {

            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            double power = gamepad1.right_trigger;

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;


            robot.fr.setPower(rightFrontPower * power);
            robot.bl.setPower(leftBackPower * power);
            robot.br.setPower(rightBackPower * power);


            


                telemetry.addData("Trigger: ", gamepad1.right_trigger);
                telemetry.addData("Bumper: ", gamepad1.left_bumper);
                telemetry.addData("LF: ", leftFrontPower * power);
                telemetry.addData("FR: ", rightFrontPower * power);
                telemetry.addData("BL: ", leftBackPower * power);
                telemetry.addData("BR: ", rightBackPower * power);
                telemetry.addData(">", "Done");

                telemetry.update();


        }

    }
}