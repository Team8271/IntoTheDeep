package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name="Main Teleop")
public class Teleop extends LinearOpMode {
    private Configuration robot;
    @Override
    public void runOpMode() {
        robot=new Configuration(this);
        robot.init();
        waitForStart();

        while (opModeIsActive()){
            //Main Drive Control
            double axial = -gamepad1.left_stick_y;
            double lateral = -gamepad1.right_stick_x;
            double yaw = -gamepad1.left_stick_x;

            double power = gamepad1.right_trigger;

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            robot.FL.setPower(leftFrontPower * power);
            robot.FR.setPower(rightFrontPower * power);
            robot.BL.setPower(leftBackPower * power);
            robot.BR.setPower(rightBackPower * power);

            //Intake control
                //A button runs motor in
                if(gamepad2.a) {
                    robot.In.setPower(0.5);
                }
                //Y runs motor out
                if(gamepad2.y) {
                    robot.In.setPower(-0.5);
                }
                //x stops motor
                if(gamepad2.x) {
                   robot.In.setPower(0.0);
               }

            //Horizontal motor
                //right joystick forward = forward
                robot.Horz.setPower(gamepad2.right_stick_x);

            //Vertical motor
                //left joystick forward = up


                if (!robot.Max.isPressed() && !robot.Min.isPressed())
                {
                    robot.Vert.setPower(gamepad2.left_stick_y);
                }
                else {
                    if (robot.Max.isPressed()) {
                    telemetry.addData("DETECTED", "Max - Reverse Direction");

                    if (gamepad2.right_stick_y > 0) {
                        telemetry.addData("Joystick Y", gamepad2.left_stick_y);
                        robot.Vert.setPower(gamepad2.left_stick_y);

                    } else {

                        robot.Vert.setPower(0);
                    }
                } else if (robot.Min.isPressed()) {
                    telemetry.addData("DETECTED", "Min - Reverse Direction");

                    if (gamepad2.right_stick_y < 0) {

                        telemetry.addData("Vert", gamepad2.left_stick_y);

                    } else {

                        robot.Vert.setPower(0);
                    }
                }
            }//End of Vert control

            //Red and Blue servo control left trigger
                //position servos to close when trigger pressed, open when released
                if (gamepad2.left_trigger > 0.25) { //close
                    robot.Red.setPosition(0.5);
                    robot.Blue.setPosition(0.5);
                    //tbd values

                } else { //open
                    robot.Red.setPosition(0.5);
                    robot.Blue.setPosition(0.5);
                    //tbd values
                }


            //Orb servo right bumper
                //bumper pressed, flip out and start intake motor (In)
                if (gamepad2.right_bumper) {
                    robot.Org.setPosition(0.5); //tbd value
                    robot.In.setPower(1); //tbd value for direction
                } else { //when released, stop intake and flip in
                    robot.Org.setPosition(0.5); //tbd value
                    robot.In.setPower(0);
                }

                //determine what starts output
                //ex. use retracted arm touch sensor to initiate intake motor

            //Flip servo left bumper
            
                //when bumper pressed, deliver
                //when released, return to intake position


        }
    }
}

