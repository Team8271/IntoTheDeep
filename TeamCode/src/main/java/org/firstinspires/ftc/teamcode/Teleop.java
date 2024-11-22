package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Main Teleop")
public class Teleop extends LinearOpMode {
    private Configuration robot;

    boolean pincherControl = false;

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
                double horzControl = gamepad2.right_stick_x *1; //I know the 1 isn't word I can't spell
                double vertControl = -gamepad2.left_stick_y;
                boolean boxControl = gamepad2.left_trigger >.25;
                boolean reverseIntake = gamepad2.right_trigger>.25;

                if(gamepad2.a){
                    pincherControl = true; //true is closed
                }
                else if(gamepad2.b){
                    pincherControl = false;
                }


                //Drivetrain
                double axial = axialControl;
                double lateral = lateralControl;

                if (resetFCD){
                    robot.odometer.resetTo(0,0,0);
                }


                double gamepadRadians = Math.atan2(lateralControl, axialControl);
                double gamepadHypot = Range.clip(Math.hypot(lateralControl, axialControl), 0, 1);
                double robotRadians = robot.odometer.getZ();
                double targetRadians = gamepadRadians + robotRadians;
                lateral = Math.sin(targetRadians)*gamepadHypot;
                axial = Math.cos(targetRadians)*gamepadHypot;

            telemetry.addData("getZ", robot.odometer.getZ());
            telemetry.update();

                telemetry.addData("gamepadRadians",String.valueOf(gamepadRadians));
                telemetry.addData("gamepadHypot",String.valueOf(gamepadHypot));
                telemetry.addData("robotRadians",String.valueOf(robotRadians));
                telemetry.addData("targetRadians",String.valueOf(targetRadians));



                double leftFrontPower = axial + lateral + yawControl;
                double rightFrontPower = axial - lateral - yawControl;
                double leftBackPower = axial - lateral + yawControl;
                double rightBackPower = axial + lateral - yawControl;


                robot.fl.setPower(leftFrontPower * mainThrottle);
                robot.fr.setPower(rightFrontPower * mainThrottle);
                robot.bl.setPower(leftBackPower * mainThrottle);
                robot.br.setPower(rightBackPower * mainThrottle);

                telemetry.addLine();


                //Dpad down make vertical go down
                if(gamepad1.dpad_down){
                    while(!robot.verticalLimiter.isPressed() && opModeIsActive()){
                        //vertControl
                    }
                }

                //Horizontal Slide
                telemetry.addData("Horizontal Slide Pos",robot.horizontalMotor.getCurrentPosition());
                telemetry.addData("Horizontal Slide Limiter",robot.horizontalLimiter.isPressed());

                if(robot.horizontalLimiter.isPressed()) { // Slide bottomed out
                    if (horzControl<0) {
                        horzControl = 0;
                    }
                    robot.horizontalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    telemetry.addLine("Horizontal slide bottomed out!");
                }
                else if(robot.horizontalMotor.getCurrentPosition()>=robot.vertMax) { // Slide topped out
                    if(horzControl>0){
                        horzControl = 0;
                    }
                }


                if(horzControl != 0){ //Moving
                    if(robot.horizontalMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                        robot.horizontalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    if(horzControl < 0){
                        robot.horizontalMotor.setPower(horzControl*.4);
                    }
                    else{
                        robot.horizontalMotor.setPower(horzControl);
                    }
                    telemetry.addLine("Horizontal Slide moving..");
                    telemetry.addData("horzControl:", horzControl);

                }
                else { //stop and hold
                    if(robot.horizontalMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                        int targetPosToHold = robot.horizontalMotor.getCurrentPosition();
                        if (targetPosToHold>robot.horzMax){
                            targetPosToHold = 0;
                        }
                        robot.horizontalMotor.setTargetPosition(targetPosToHold);
                        robot.horizontalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.horizontalMotor.setPower(0.5);
                    }
                    telemetry.addLine("Horizontal Slide holding..");
                }

                telemetry.addLine();

                // Intake
                if (robot.verticalMotor.getCurrentPosition()<=15 && //Retracted
                        robot.horizontalMotor.getCurrentPosition()<=15) {
                    robot.flipServo.setPosition(.8);
                    robot.intakeMotor.setPower(0);
                    if(reverseIntake){//reverseIntake?-1:1 was here
                        robot.intakeMotor.setPower(-0.8);

                    }
                    telemetry.addLine("Intake retracted");
                }
                else if(robot.horizontalMotor.getCurrentPosition()>=robot.intakeOnDistance){ // Extended
                    robot.flipServo.setPosition(0);
                    robot.intakeMotor.setPower(1);//reverseIntake?-1:1 was here
                    if(reverseIntake){//reverseIntake?-1:1 was here
                        robot.intakeMotor.setPower(-0.8);
                    }
                    telemetry.addLine("Intake Extended");
                }
                else { //Gray zone
                    robot.flipServo.setPosition(.6);
                    robot.intakeMotor.setPower(0); //reverseIntake?-1:.5   //aslo 0.5 power
                    if(reverseIntake){//reverseIntake?-1:1 was here
                        robot.intakeMotor.setPower(-0.8);
                    }
                    telemetry.addLine("Intake waiting");
                }
                telemetry.addLine();

                //Vertical Slide
                telemetry.addData("Vertical Slide Position",robot.verticalMotor.getCurrentPosition());
                telemetry.addData("Vertical Slide Limiter",robot.verticalLimiter.isPressed());

                if(robot.verticalLimiter.isPressed()){ //slide bottomed out
                    if(vertControl<0){
                        vertControl = 0;
                    }
                    robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    telemetry.addLine("Vertical slide bottomed out");
                }
                else if(robot.verticalMotor.getCurrentPosition()>=robot.vertMax){ // Slide topped out
                    if(vertControl>0){
                        vertControl = 0;
                    }
                    telemetry.addLine("Vertical slide topped out");
                }

                if(vertControl != 0) { //Moving
                    if(robot.verticalMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    robot.verticalMotor.setPower(vertControl);
                    telemetry.addLine("Vertical slide moving");
                }
                else { //Stop and hold
                    if(robot.verticalMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                        int targetPosToHold = robot.verticalMotor.getCurrentPosition();
                        if(targetPosToHold>robot.vertMax){
                            targetPosToHold = robot.vertMax;
                        }
                        robot.verticalMotor.setTargetPosition(targetPosToHold);
                        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.verticalMotor.setPower(.5);
                    }
                    telemetry.addLine("Vertical slide holding...");
                }

                telemetry.addLine();

                //Pincher
                if(pincherControl){ //closed
                    robot.redServo.setPosition(0.65);//Bigger more close
                    robot.blueServo.setPosition(0.35); //less more close
                    telemetry.addLine("Red and Blue closed");
                }
                else{ //open
                    robot.redServo.setPosition(0.5);
                    robot.blueServo.setPosition(0.5);
                    telemetry.addLine("Red and Blue open");
                }

                telemetry.addLine();

                //Box
                if(boxControl && robot.verticalMotor.getCurrentPosition()>=100){
                    robot.boxServo.setPosition(-.99);
                    telemetry.addLine("Box flipped");
                }
                else{
                    robot.boxServo.setPosition(.6);
                    telemetry.addLine("Box retracted");
                }

                //Telemetry
                telemetry.update();





            /*
            robot.fl.setPower(leftFrontPower * mainThrottle);
            robot.fr.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            //Intake control
                //A button runs motor in
                if(gamepad2.a) {
                    robot.intakeMotor.setPower(0.5);
                }
                //Y runs motor out
                if(gamepad2.y) {
                    robot.intakeMotor.setPower(-0.5);
                }
                //x stops motor
                if(gamepad2.x) {
                   robot.intakeMotor.setPower(0.0);
               }

            //Horizontal motor
                //right joystick forward = forward
                robot.horizontalMotor.setPower(gamepad2.right_stick_x);

            //Vertical motor
                //left joystick forward = up


                if (!robot.verticalLimiter.isPressed() && !robot.horizontalLimiter.isPressed())
                {
                    robot.verticalMotor.setPower(gamepad2.left_stick_y);
                }
                else {
                    if (robot.verticalLimiter.isPressed()) {
                    telemetry.addData("DETECTED", "Max - Reverse Direction");

                    if (gamepad2.right_stick_y > 0) {
                        telemetry.addData("Joystick Y", gamepad2.left_stick_y);
                        robot.verticalMotor.setPower(gamepad2.left_stick_y);

                    } else {

                        robot.verticalMotor.setPower(0);
                    }
                } else if (robot.horizontalLimiter.isPressed()) {
                    telemetry.addData("DETECTED", "Min - Reverse Direction");

                    if (gamepad2.right_stick_y < 0) {

                        telemetry.addData("Vert", gamepad2.left_stick_y);

                    } else {

                        robot.verticalMotor.setPower(0);
                    }
                }
            }//End of Vert control

            //Red and Blue servo control left trigger
                //position servos to close when trigger pressed, open when released
                if (gamepad2.left_trigger > 0.25) { //close
                    robot.redServo.setPosition(0.5);
                    robot.blueServo.setPosition(0.5);
                    //tbd values

                } else { //open
                    robot.redServo.setPosition(0.5);
                    robot.blueServo.setPosition(0.5);
                    //tbd values
                }


            //Orb servo right bumper
                //bumper pressed, flip out and start intake motor (In)
                if (gamepad2.right_bumper) {
                    robot.boxServo.setPosition(0.5); //tbd value
                    robot.intakeMotor.setPower(1); //tbd value for direction
                } else { //when released, stop intake and flip in
                    robot.boxServo.setPosition(0.5); //tbd value
                    robot.intakeMotor.setPower(0);
                }

                //determine what starts output
                //ex. use retracted arm touch sensor to initiate intake motor

            //Flip servo left bumper

                //when bumper pressed, deliver
                //when released, return to intake position

            */
            }
        }
    }

