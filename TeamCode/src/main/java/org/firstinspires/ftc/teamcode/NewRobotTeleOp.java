package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

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
            boolean clipSpecimen = gamepad2.x;  //Clips the specimen
            boolean override = gamepad2.b;      //Override running code (ie clipping)
            boolean grabFromWall = gamepad2.a;  //Grab Specimen from wall
            boolean goToHighBasket = gamepad2.y;                //Go to High Basket
            boolean closeClaw = gamepad2.right_trigger < 0.5;   //Close the Claw
            boolean openClaw = gamepad2.right_trigger >= 0.5;   //Open the Claw


            //Calculate wheel power
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            //Send wheel power with throttle multiplier
            robot.fl.setPower(leftFrontPower * mainThrottle);
            robot.fr.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            //Send slide power
            robot.leftHorz.setPower(horziontalPower);
            robot.rightHorz.setPower(horziontalPower);
            robot.vertMotor.setPower(verticalPower);

            //Send telemetry
            telemetry.addData("Runtime", runtime.toString());

        }
    }
}

