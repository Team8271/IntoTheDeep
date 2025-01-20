package org.firstinspires.ftc.teamcode.StateBot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@Disabled
@TeleOp(name="State Bot TeleOp")
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
            double axialControl = -gamepad1.left_stick_y;
            double lateralControl = gamepad1.left_stick_x;
            double yawControl = gamepad1.right_stick_x;
            double mainThrottle = .2+(gamepad1.right_trigger*0.8);
            boolean resetFCD = gamepad1.dpad_up;

            //Driver 2
            double verticalPower = gamepad2.left_stick_y;
            double horziontalPower = gamepad2.right_stick_x;
            boolean clipSpecimen = gamepad2.x;  //Clips the specimen
            boolean override = gamepad2.b;      //Override running code (ie clipping)
            boolean grabFromWall = gamepad2.a;  //Grab Specimen from wall
            boolean goToHighBasket = gamepad2.y;                //Go to High Basket
            boolean closeClaw = gamepad2.right_trigger < 0.5;   //Close the Claw
            boolean openClaw = gamepad2.right_trigger >= 0.5;   //Open the Claw




            ///Drivetrain Start
            double axial = axialControl;
            double lateral = lateralControl;

            double gamepadRadians = Math.atan2(lateralControl, axialControl);
            double gamepadHypot = Range.clip(Math.hypot(lateralControl, axialControl), 0, 1);
            double robotRadians = robot.odometer.getZ();
            double targetRadians = gamepadRadians + robotRadians;
            lateral = Math.sin(targetRadians)*gamepadHypot;
            axial = Math.cos(targetRadians)*gamepadHypot;

            double rightFrontPower = axial + lateral + yawControl;
            double leftFrontPower = axial - lateral - yawControl;
            double leftBackPower = axial - lateral + yawControl;
            double rightBackPower = axial + lateral - yawControl;

            if(resetFCD){
                robot.odometer.resetTo(0,0,0);
            }

            

            //Send slide power
            robot.leftHorz.setPower(horziontalPower);
            robot.rightHorz.setPower(horziontalPower);
            robot.vertMotor.setPower(verticalPower);

            //Send telemetry
            telemetry.addData("Runtime", runtime.toString());

        }
    }
}

