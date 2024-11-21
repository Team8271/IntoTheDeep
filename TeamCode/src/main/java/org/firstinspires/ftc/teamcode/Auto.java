package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous (name="Auto")
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean x = true;
    double power = 0.4;

    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init();
        robot.initTweatyBird();
/*
        robot.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        waitForStart();
/*
        while (x&&opModeIsActive()) {
            telemetry.addData("Forward/Reverse:", -robot.odometer.getY());
            telemetry.addData("Left/Right:", robot.odometer.getX());

            //telemetry.addData("getX:", robot.odometer.getX()); //right/left     positive/negative
            //telemetry.addData("Forward:", -robot.odometer.getY()); //forward/backward  positive/negative

            telemetry.update();
            if(gamepad1.dpad_up){
                brakeAndReset();
            }
            if (gamepad1.a) {
                x = false;
            }
        }

        int y=0;

        sleep(10000);


        robot.odometer.getX();*/



        //robot.tweetyBird.sendTargetPosition();
        //robot.tweetyBird.waitWhileBusy();

        

        /*
        move forward 1 inch
        move right 3 inch
        raise arm
        move to touch submersible
        reverse 3 inch

         */
        robot.odometer.resetTo(0,0,0);

        robot.tweetyBird.sendTargetPosition(0,10,0);
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(10,10,0);

        while (opModeIsActive());

        robot.tweetyBird.close();

        /*
        //drive to submersible until sensor pressed
        robot.fl.setPower(power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(power);
        while(!robot.frontSensor.isPressed() && opModeIsActive()){
            telemetry.addLine("Not Pressed");
            telemetry.update();
        }
        telemetry.addLine("Pressed!");
        brakeAndReset();
        telemetry.addLine("LOOK MA I DID IT!");
        telemetry.update();*/
















        /*
        sleep(5000);
        forward(5);//testing
        sleep(5000);
        reverse(5);
        sleep(5000);
        left(5);
        sleep(5000);
        right(5);
        sleep(10000);//end testing

        forward(28.6);
        sleep(2000);
        reverse(26.7);
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
*/

    }


    private void brakeAndReset(){
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
        robot.odometer.resetTo(0,0,0);
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
        brakeAndReset();
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
        brakeAndReset();
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
        brakeAndReset();
    }

    private void left(double distance){
        while(-robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going left:", distance);
            telemetry.addData("Current Position:", -robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(-power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
        }
        brakeAndReset();
    }


}
