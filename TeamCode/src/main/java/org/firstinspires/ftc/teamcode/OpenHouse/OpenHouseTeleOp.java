package org.firstinspires.ftc.teamcode.OpenHouse;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.QualifierBot.Configuration;

@TeleOp(name="TeleOp - Open House")
public class OpenHouseTeleOp extends LinearOpMode {
    private OpenHouseConfig robot;

    @Override
    public void runOpMode(){
        robot = new OpenHouseConfig(this);
        robot.init();

        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            ///Driver 1
            double axialControl = gamepad1.left_stick_y;
            double lateralControl = gamepad1.left_stick_x;
            double yawControl = gamepad1.right_stick_x;
            double mainThrottle = .2+(gamepad1.right_trigger*.8);
            boolean resetFCD = gamepad1.dpad_up;

            ///Driver 2
            double horzControl = gamepad2.right_stick_x;
            double vertControl = gamepad2.left_stick_y;
            boolean boxControl = gamepad2.left_trigger >.25;
            boolean reverseIntake = gamepad2.right_trigger >.25;
            boolean intakeTransferMode = gamepad2.dpad_down;

            ///Drivetrain Start
            double axial = axialControl;
            double lateral = lateralControl;

            double gamepadRadians = Math.atan2(lateralControl, axialControl);
            double gamepadHypot = Range.clip(Math.hypot(lateralControl, axialControl), 0, 1);
            double robotRadians = robot.odometer.getZ();
            double targetRadians = gamepadRadians + robotRadians;
            lateral = Math.sin(targetRadians)*gamepadHypot;
            axial = Math.cos(targetRadians)*gamepadHypot;

            double leftFrontPower = axial + lateral + yawControl;
            double rightFrontPower = axial - lateral - yawControl;
            double leftBackPower = axial - lateral + yawControl;
            double rightBackPower = axial + lateral - yawControl;


            robot.fr.setPower(leftFrontPower * mainThrottle);
            robot.fl.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            if(resetFCD){
                robot.odometer.resetTo(0,0,0);
            }

            ///Horizontal Slide Start
            //Fully Retracted
            if(robot.horzLimiter.isPressed()){
                    horzControl = 0;
                    robot.horzMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    telemetry.addLine("HorzLimiter is pressed.");
            }
            //Fully extended
            else if(robot.horzMotor.getCurrentPosition()>=robot.horzMax){
                horzControl = 0;
                telemetry.addLine("HorzMotor Fully Extended");
            }

            if(horzControl != 0){ //Slide is Moving
                if(robot.horzMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                    robot.horzMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                if(horzControl < 0){ //Going back into robot at .5 speed
                    robot.horzMotor.setPower(horzControl*.5);
                }
                else{ //Extending out
                    robot.horzMotor.setPower(horzControl);
                }
                telemetry.addLine("HorzMotor Moving");
            }
            else{ //Slide not moving
                if(robot.horzMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                    int targetPosToHold = robot.horzMotor.getCurrentPosition();
                    robot.horzMotor.setTargetPosition(targetPosToHold);
                    robot.horzMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.horzMotor.setPower(0.5);
                }
                telemetry.addLine("HorzMotor Holding");
            }

            ///Intake Start
            //If everything is retracted for boxTransfer
            if(robot.vertMotor.getCurrentPosition()<=15 &&
                    robot.vertMotor.getCurrentPosition() >=0 &&
                    robot.horzMotor.getCurrentPosition()<=15){
                if(intakeTransferMode){
                    robot.flipServo.setPosition(.9);
                }
                robot.intakeMotor.setPower(0);
                if(reverseIntake){
                    robot.intakeMotor.setPower(-.8);
                }
                telemetry.addLine("Intake Retracted");
            }
            //Where the intake goes down into collect mode
            else if(robot.horzMotor.getCurrentPosition()>= robot.intakeOnDistance){
                robot.flipServo.setPosition(0.07);
                robot.intakeMotor.setPower(1);
                if(reverseIntake){
                    robot.intakeMotor.setPower(-.8);
                }
                telemetry.addLine("Intake in Collect Mode");
            }
            else{ //Gray Zone, not retracted or extended
                robot.flipServo.setPosition(.6);
                robot.intakeMotor.setPower(0);
                if(reverseIntake){
                    robot.intakeMotor.setPower(-.8);
                }
                telemetry.addLine("Intake Gray Zone");
            }

            ///Vertical Motor Start
        if(robot.vertLimiter.isPressed()){ //Slide at bottom
            vertControl = 0;
            robot.vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addLine("Vert Bottomed Out");
        }
        //An else if can go here if you want a limit to the slides max

        if(vertControl != 0){ //Moving
            if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                robot.vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            robot.vertMotor.setPower(vertControl);
            telemetry.addLine("Vertical Slide Moving");
        }
        else{ //No input (Stop and Hold)
            if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                int targetPosToHold = robot.vertMotor.getCurrentPosition();
                robot.vertMotor.setTargetPosition(targetPosToHold);
                robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.vertMotor.setPower(.5);
            }
            telemetry.addLine("VertMotor Moving");
        }

        ///Box Start
        if(boxControl && robot.vertMotor.getCurrentPosition()>=100){
            robot.boxServo.setPosition(-.99);
            telemetry.addLine("Box Dumping");
        }
        else{
            robot.boxServo.setPosition(.6);
            telemetry.addLine("Box Retracted");
        }

        ///Updating the Telemetry
        telemetry.update();

        }
    }
}
