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
    double lowPower = 0.2;
    double normalPower = 0.4;
    double highPower = 0.6;
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
       //private void sampleNetZone() {

        //close claw
        robot.redServo.setPosition(0.65);//Bigger more close
        robot.blueServo.setPosition(0.35); //less more close
        telemetry.addLine("Red and Blue closed");
        sleep(1000); //wait for servos to respond


        //Additional wait
        //sleep(6000);


        //align with chamber starting at right
        //forward(2, normalPower);
        //left(4.5, normalPower);

        //align w/ chambers
        forward(2, normalPower);
        left(4.5, normalPower);



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
        sleep(20);
        brakeAndReset();



        //arm down
        robot.verticalMotor.setTargetPosition(-100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);

        //reverse a half inch
        reverse(0.5, normalPower);

        //let the arm go down a little
        sleep(800);



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
        //move arm to grab spec
        robot.verticalMotor.setTargetPosition(2760);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);



        //go right 39
        right(39, normalPower);
        rotate180();

        //forward broken right now
        telemetry.addLine("Going forward");
        telemetry.update();
        robot.fl.setPower(normalPower);
        robot.fr.setPower(normalPower);
        robot.bl.setPower(normalPower);
        robot.br.setPower(normalPower);
        sleep(2000);
        brakeAndReset();








/*
        //go left 37 inches
        left(37, normalPower); //modified

        //go forward 25in
        forward(16, normalPower); //was 25

        //new stuff
        left(6.5, normalPower);
        right(0.5, normalPower);
        forward(3, normalPower);
        left(1.5, normalPower);

        //go left
        left(6, normalPower);

        //push sample into net zone
        reverse(45, normalPower);

        //go forward
        forward(50, normalPower);

        //go left
        left(6, normalPower);

        //push second sample into net zone
        reverse(46, normalPower);

        //go forward
        forward(40, normalPower);

        //go left into wall
        robot.fl.setPower(-power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(-power);
        sleep(1500);
        brakeAndReset();

        //right off the wall a little
        right(0.5, normalPower);

        //push sample into net zone
        reverse(38, normalPower);

        //drive right to the observation zone for parking
        forward(0.3, normalPower);
        right(120, normalPower);
        //}


*/
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
    private void forward(double distance, double power){
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

    //Fixed
    private void reverse(double distance, double power){
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

    private void right(double distance, double power){
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

    private void left(double distance, double power){
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

    private void move(double distance, double speed){

    }


}
