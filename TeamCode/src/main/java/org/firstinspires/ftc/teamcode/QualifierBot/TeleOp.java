package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

//Weird because name is also TeleOp
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Jax TeleOp")
public class TeleOp extends LinearOpMode {
    private Configuration robot;


    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(false);


        telemetry.addLine("Initialized");

        waitForStart();

        telemetry.clearAll();


        while (opModeIsActive()){
                //Driver 1
                double axialControl = -gamepad1.left_stick_y;
                double lateralControl = gamepad1.left_stick_x;
                double yawControl = gamepad1.right_stick_x;
                double mainThrottle = .2+(gamepad1.right_trigger*0.8);
                boolean resetFCD = gamepad1.dpad_up;

                //Driver 2
                double horzControl = gamepad2.right_stick_x;
                double vertControl = -gamepad2.left_stick_y;
                boolean boxControl = gamepad2.left_trigger >.25;
                boolean reverseIntake = gamepad2.right_trigger>.25;
                boolean intakeTransferMode = gamepad2.dpad_down;


                if(gamepad2.a){
                    robot.closeClaw();
                }
                else if(gamepad2.b){
                    robot.openClaw();
                }





                if (resetFCD){
                    robot.odometer.resetTo(0,0,0);
                }




                telemetry.addData("getX", robot.odometer.getX());
                telemetry.addData("getY", robot.odometer.getY());
                telemetry.addData("getZ", robot.odometer.getZ());


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


                robot.fl.setPower(leftFrontPower * mainThrottle);
                robot.fr.setPower(rightFrontPower * mainThrottle);
                robot.bl.setPower(leftBackPower * mainThrottle);
                robot.br.setPower(rightBackPower * mainThrottle);

                telemetry.addLine();





                //Horizontal Slide
                telemetry.addData("Horizontal Slide Pos",robot.horzMotor.getCurrentPosition());
                telemetry.addData("Horizontal Slide Limiter",robot.horizontalLimiter.isPressed());

                if(robot.horizontalLimiter.isPressed()) { // Slide bottomed out
                    if (horzControl<0) {
                        horzControl = 0;
                    }
                    robot.horzMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    telemetry.addLine("Horizontal slide bottomed out!");
                }
                else if(robot.horzMotor.getCurrentPosition()>=robot.horzMax) { // Slide topped out
                    if(horzControl>0){
                        horzControl = 0;
                    }
                }


                if(horzControl != 0){ //Moving
                    if(robot.horzMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                        robot.horzMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    if(horzControl < 0){
                        robot.horzMotor.setPower(horzControl*.4);
                    }
                    else{
                        robot.horzMotor.setPower(horzControl);
                    }
                    telemetry.addLine("Horizontal Slide moving..");
                    telemetry.addData("horzControl:", horzControl);

                }
                else { //stop and hold
                    if(robot.horzMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                        int targetPosToHold = robot.horzMotor.getCurrentPosition();
                        if (targetPosToHold>robot.horzMax){
                            targetPosToHold = 0;
                        }
                        robot.horzMotor.setTargetPosition(targetPosToHold);
                        robot.horzMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.horzMotor.setPower(0.5);
                    }
                    telemetry.addLine("Horizontal Slide holding..");
                }

                telemetry.addLine();

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


                ///Vertical Slide
                telemetry.addData("Vertical Slide Position",robot.vertMotor.getCurrentPosition());
                telemetry.addData("Vertical Slide Limiter",robot.verticalLimiter.isPressed());

                if(robot.verticalLimiter.isPressed()){ //slide bottomed out
                    if(vertControl<0){
                        vertControl = 0;
                    }
                    robot.vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    telemetry.addLine("Vertical slide bottomed out");
                }
                /*
                else if(robot.vertMotor.getCurrentPosition()>=robot.vertMax){ // Slide topped out
                    if(vertControl>0){
                        vertControl = 0;
                    }
                    telemetry.addLine("Vertical slide topped out");
                }*/

                if(vertControl != 0) { //Moving
                    if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                        robot.vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    robot.vertMotor.setPower(vertControl);
                    telemetry.addLine("Vertical slide moving");

                }
                else { //Stop and hold NO INPUT
                    if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                        int targetPosToHold = robot.vertMotor.getCurrentPosition();
                        /*if(targetPosToHold>robot.vertMax){
                            targetPosToHold = robot.vertMax;
                        }*/
                        robot.vertMotor.setTargetPosition(targetPosToHold);
                        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.vertMotor.setPower(.5);
                    }
                    telemetry.addLine("Vertical slide holding...");
/*
                    //If button while thingy thing
                    if(gamepad2.dpad_down && !robot.verticalLimiter.isPressed()){
                        robot.vertMotor.setTargetPosition(0);
                        robot.vertMotor.setPower(0.6);
                    }

                    if(gamepad2.dpad_right){
                        robot.vertMotor.setTargetPosition(robot.vertWall);
                        robot.vertMotor.setPower(0.6);
                    }

                    if(gamepad2.dpad_up){
                        robot.vertMotor.setTargetPosition(robot.vertAboveChamber);
                        robot.vertMotor.setPower(0.6);
                    }
*/
                }

                telemetry.addLine();


                //Box
                if(boxControl && robot.vertMotor.getCurrentPosition()>=100){
                    robot.boxServo.setPosition(-.99);
                    telemetry.addLine("Box flipped");
                }
                else{
                    robot.boxServo.setPosition(.6);
                    telemetry.addLine("Box retracted");
                }

                //Telemetry
                telemetry.update();
        }
    }
}
