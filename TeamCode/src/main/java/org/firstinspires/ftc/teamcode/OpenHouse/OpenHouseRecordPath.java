package org.firstinspires.ftc.teamcode.OpenHouse;

import android.system.Os;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;

@Disabled
@TeleOp(name="Record Path")
public class OpenHouseRecordPath extends LinearOpMode {
    private OpenHouseConfig robot;

    boolean debounce = false;
    int posNumber = 0;
    ArrayList<String> positionList = new ArrayList<>();

    @Override
    public void runOpMode(){
        robot = new OpenHouseConfig(this);
        robot.init(false);

        robot.odometer.resetTo(0,0,0);

        telemetry.addLine("Initialized\n\n" +
                "Set bot up in desired starting position\n" +
                "!! NO FCD RESET !!");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            ///Driver Controls
            double axialControl = -gamepad1.left_stick_y;
            double lateralControl = gamepad1.left_stick_x;
            double yawControl = gamepad1.right_stick_x;
            double mainThrottle = .2+(gamepad1.right_trigger*.6);
            boolean savePosition = gamepad1.a;

            //Driver 2
            double vertControl = gamepad2.left_stick_y;

            if(gamepad2.a){
                robot.closeClaw();
            }
            else if(gamepad2.b){
                robot.openClaw();
            }


            //Save a Position
            if(savePosition&&!debounce){
                debounce = true;
                posNumber++;
                //do what you want here

                double x = Math.rint(robot.odometer.getY());
                double y = Math.rint(robot.odometer.getX());
                double z = Math.rint(robot.odometer.getZ());


                String position = posNumber + ": " + x + ", " + y + ", " + z; //1: x, y, z
                positionList.add(position);
            }

            if(!savePosition&&debounce){
                debounce = false;
            }



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


            for (String str : positionList) {
                telemetry.addLine(str);
            }

            double x = Math.round(robot.odometer.getX() * 10.0)/10.0;
            double y = Math.round(robot.odometer.getY() * 10.0)/10.0;
            double z = Math.round(robot.odometer.getZ() * 10.0)/10.0;

            String currentPosition = x + ", " + y + ", " + z;
            telemetry.addLine("Current Position: " + currentPosition);

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

            telemetry.update();
        }

    }
}
