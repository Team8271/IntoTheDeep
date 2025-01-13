package org.firstinspires.ftc.teamcode.OpenHouse;

import android.system.Os;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;


@TeleOp(name="Record Path")
public class OpenHouseRecordPath extends LinearOpMode {
    private OpenHouseConfig robot;

    boolean debounce = false;
    int posNumber = 0;
    ArrayList<String> positionList = new ArrayList<>();

    @Override
    public void runOpMode(){
        robot = new OpenHouseConfig(this);
        robot.init();

        telemetry.addLine("Initialized\n\n" +
                "Set bot up in desired starting position\n" +
                "!! NO FCD RESET !!");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            ///Driver Controls
            double axialControl = gamepad1.left_stick_y;
            double lateralControl = gamepad1.left_stick_x;
            double yawControl = gamepad1.right_stick_x;
            double mainThrottle = .2+(gamepad1.right_trigger*.6);
            boolean savePosition = gamepad1.a;
            boolean removeLastPosition = gamepad1.b;

            //Save a Position
            if(savePosition&&!debounce){
                debounce = true;
                posNumber++;
                //do what you want here
                double x = Math.round(robot.odometer.getX() * 10.0)/10.0;
                double y = Math.round(robot.odometer.getY() * 10.0)/10.0;
                double z = Math.round(robot.odometer.getZ() * 10.0)/10.0;
                String position = posNumber + ": " + x + ", " + y + ", " + z; //1: x, y, z
                positionList.add(position);
            }



            ///Drivetrain
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


            for (String str : positionList) {
                telemetry.addLine(str);
            }

            double x = Math.round(robot.odometer.getX() * 10.0)/10.0;
            double y = Math.round(robot.odometer.getY() * 10.0)/10.0;
            double z = Math.round(robot.odometer.getZ() * 10.0)/10.0;

            String currentPosition = x + ", " + y + ", " + z;
            telemetry.addLine("Current Position: " + currentPosition);

            telemetry.update();
        }

    }
}
