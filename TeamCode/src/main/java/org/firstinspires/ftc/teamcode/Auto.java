package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Configuration;

@Autonomous (name="Auto")
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean x = true;
    double power = 0.2;

    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init();
        robot.odometer.resetTo(0,0,0);

        robot.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
/*
        while (x) {
            if(robot.odometer.getY() >= 0){
                telemetry.addData("Forward:", -robot.odometer.getY());
            }
            else{
                telemetry.addData("Reverse:", -robot.odometer.getY());
            }
            if(robot.odometer.getX() >= 0){
                telemetry.addData("Right:", robot.odometer.getX());
            }
            else {
                telemetry.addData("Left:", robot.odometer.getX());
            }
            //telemetry.addData("getX:", robot.odometer.getX()); //right/left     positive/negative
            //telemetry.addData("Forward:", -robot.odometer.getY()); //forward/backward  positive/negative

            telemetry.update();
            if (gamepad1.a) {
                x = false;
            }
        }
*/
        //forward(5);
        //reverse(5);
        left(5);
        right(5);
        sleep(3000);

        forward(5); //28.6
        sleep(2000);
        reverse(5); //26.7
        sleep(2000);
        left(28.7);

        forward(23.4);
        left(9.7);
        reverse(44);

        forward(44);
        left(10.3);
        reverse(44);

        forward(44);
        left(5);
        reverse(36);

        right(105);

    }

    private void brake(){
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

    private void forward(double distance){
        while(-robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going forward:", distance);
            telemetry.addData("Current Position:", -robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
        }
        brake();
    }

    private void reverse(double distance){
        while(robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going reverse:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(-power);
            robot.fr.setPower(-power);
            robot.bl.setPower(-power);
            robot.br.setPower(-power);
        }
        brake();
    }

    private void right(double distance){
        while(robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going right:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(power);
            robot.fr.setPower(-power);
            robot.bl.setPower(-power);
            robot.br.setPower(power);
        }
        brake();
    }

    private void left(double distance){
        while(robot.odometer.getY() > distance && opModeIsActive()){
            telemetry.addData("Going left:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(-power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
        }
        brake();
    }


}
