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

        //set all the wheels to brake on no power (remove drifting)
        robot.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Wait for driver to press START
        waitForStart();

        //initialize the boxServo
        robot.boxServo.setPosition(.6);



        robot.verticalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(true && opModeIsActive()){
            telemetry.addData("Arm thing", robot.verticalMotor.getCurrentPosition());
            telemetry.update();
            //specimin middle is 9 inches from ground
        }



    //Reset odom values
        robot.odometer.resetTo(0,0,0);

        ///Start of 2


/*
        robot.odometer.resetTo(0,0,0);
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

        //go right
        right(30);*/




        //END OF 2

        /* What is sampleNetZone?
         *
         * Starts with a preloaded specimen and clips it on high chamber
         * Goes left and pushes all three samples into net zone
         */
       /* private void sampleNetZone() {
            robot.odometer.resetTo(0,0,0);
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
            ////while(!robot.verticalLimiter.isPressed());
            robot.verticalMotor.setPower(0);

            //go left 37 inches
            left(37);

            //go forward 25in
            forward(16); //was 25

            //new stuff
            left(6.5);
            right(0.5);
            forward(3);
            left(1.5);

            //go left
            left(6);

            //push sample into net zone
            reverse(45);

            //go forward
            forward(50);

            //go left
            left(6);

            //push second sample into net zone
            reverse(46);

            //go forward
            forward(40);

            //go left into wall
            robot.fl.setPower(-power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
            sleep(1500);
            brakeAndReset();

            //right off the wall a little
            right(0.5);

            //push sample into net zone
            reverse(38);

            //drive right to the observation zone for parking
            forward(0.3);
            right(120);
        }*/



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

    //Rotate def
    private void rotate180(){
        robot.odometer.resetTo(0,0,0);
        while(robot.odometer.getZ() > -3.07 && opModeIsActive()){
            robot.fl.setPower(power);
            robot.fr.setPower(-power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
        }
        brakeAndReset();
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
