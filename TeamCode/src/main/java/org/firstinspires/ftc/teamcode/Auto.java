package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="Auto")
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean x = true;
    double power = 0.4;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(true);

        robot.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        //set the box servo so it doesn't stop vertical slide from going all the way down
        robot.boxServo.setPosition(.6);
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


    //Reset odometry values
        robot.odometer.resetTo(0,0,0);
/*
    //Move off of wall and to the right
        robot.tweetyBird.sendTargetPosition(0,10,0);
        robot.tweetyBird.waitWhileBusy();
        sleep(1000);
        robot.tweetyBird.sendTargetPosition(10,10,0);

        robot.tweetyBird.close();
*/

        //close claw
        robot.redServo.setPosition(0.65);//Bigger more close
        robot.blueServo.setPosition(0.35); //less more close
        telemetry.addLine("Red and Blue closed");
        sleep(1000); //wait for servos to respond


        //align with chamber
        forward(2);
        left(4.5);



        sleep(300);

        //Lift arm
        robot.verticalMotor.setTargetPosition(6100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);
        sleep(1800);

        //Drive to the submersible until sensor is pressed
        robot.fl.setPower(power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(power);
        //Add telemetry
        while(!robot.frontSensor.isPressed() && opModeIsActive()){
            telemetry.addLine("Not Pressed");
            telemetry.update();
        }

        //back up a little
        robot.fl.setPower(-power);
        robot.fr.setPower(-power);
        robot.bl.setPower(-power);
        robot.br.setPower(-power);
        sleep(25);
        brakeAndReset();



        //arm down
        robot.verticalMotor.setTargetPosition(-100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);

        //reverse ahalf inch
        reverse(0.5);

        //let the arm go down a little
        sleep(700);



        //open the claw
        robot.redServo.setPosition(0.5);
        robot.blueServo.setPosition(0.5);
        telemetry.addLine("Red and Blue open");

        //back up a bit
        robot.fl.setPower(-power);
        robot.fr.setPower(-power);
        robot.bl.setPower(-power);
        robot.br.setPower(-power);
        sleep(300);
        brakeAndReset();

        //wait until Vertical Slide bottoms out
        while(!robot.verticalLimiter.isPressed());
        robot.verticalMotor.setPower(0);

        //go left 37 inches
        left(37);

        //go forward 25in
        forward(25);

        //go left
        left(6);

        //push sample into net zone
        reverse(45);
        forward(.25);
        right(10);
        //keep going back for a second
        robot.fl.setPower(-power);
        robot.fr.setPower(-power);
        robot.bl.setPower(-power);
        robot.br.setPower(-power);
        sleep(500);
        brakeAndReset();


        //push sample left
        forward(0.25);
        left(10);

        //go forward
        forward(50);

        //go left
        left(6);

        //push sample into net zone
        reverse(41);

        //go forward
        forward(41);

        //go left into wall
        robot.fl.setPower(-power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(-power);
        sleep(2);
        brakeAndReset();

        //right off the wall a little
        right(0.5);

        //push sample into net zone
        reverse(38);

        //drive right to the observation zone for parking
        forward(0.3);
        right(120);


    }



    private void writeTelemetry(){
        telemetry.addData("Runtime", runtime.toString());
        telemetry.addLine();
        telemetry.addData("posX", robot.odometer.getX());
        telemetry.addData("posY", robot.odometer.getY());
        telemetry.addData("posZ", robot.odometer.getZ());
        telemetry.addLine();
        telemetry.update();
    }


    private void brakeAndReset(){
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
        robot.odometer.resetTo(0,0,0);
    }


    //Directions may be incorrect for Odom pods (fix in Config)
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

    //Dont use more or you die die I borken
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
        while(robot.odometer.getX() < distance && opModeIsActive()){
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
        while(-robot.odometer.getX() < distance && opModeIsActive()){
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
