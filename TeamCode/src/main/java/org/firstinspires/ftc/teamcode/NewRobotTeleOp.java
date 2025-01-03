package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="New Robot TeleOp")
public class NewRobotTeleOp extends LinearOpMode {
    private NewRobotConfig robot;

    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new NewRobotConfig(this);
        robot.init(false);
        ElapsedTime runtime = new ElapsedTime();


        telemetry.addLine("Initialized");

        waitForStart();
        runtime.reset();

        telemetry.clearAll();

        while (opModeIsActive()) {
            //Driver 1
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;
            double mainThrottle = .2 + (gamepad1.right_trigger * 0.8);

            //Driver 2
            double verticalPower = gamepad2.left_stick_y;
            double horziontalPower = gamepad2.right_stick_x;

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            robot.fl.setPower(leftFrontPower * mainThrottle);
            robot.fr.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            robot.leftHorizontal.setPower(horziontalPower);
            robot.rightHorizontal.setPower(horziontalPower);
            robot.verticalMotor.setPower(verticalPower);

            telemetry.addData("Runtime", runtime.toString());

        }
    }
}

