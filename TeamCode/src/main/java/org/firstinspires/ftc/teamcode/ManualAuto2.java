package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;


@Autonomous(name = "NoTweet")
public class ManualAuto2 extends LinearOpMode {
    Config robot;
    @Override
    public void runOpMode(){
        robot = new Config(this);
        robot.init();

        robot.closeClaw();
        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart();



        robot.boxServo.setPosition(robot.boxStoragePosition); //Set box servo
        setSlidePosition(robot.vertSlide, robot.aboveChamber, 0.8); //Set slide above chamber

        ///Clip preload
        //moveTo(-6,29,0);
        forward(5,0.5);
        left(-6,0.5);
        forward(29,0.5);

        moveUntilSensor(robot.frontTouch, 0.3); //Move into submersible

        setSlidePosition(robot.vertSlide, robot.wallHeight,0.3); //Clip specimen
        sleep(1000);
        robot.openClaw(); //Open claw


        ///Pushing samples into observation
        //Restart cause tweety was here
        robot.odometer.resetTo(0,0,0);

        //moveTo(0,-6,0); //Move back from submersible
        backward(robot.odometer.getY()-6,0.5);

        //moveTo(35,-6,0); //Move to left/below sample 1
        right(35,0.5);

        //moveTo(36,20,0); //Move to left/above sample 1
        forward(18,0.5);

        //moveTo(43,19,0); //Move to above sample 1
        right(43,0.5);


        //moveTo(43,-21,0); //Push sample 1 into observation
        backward(-21,0.5);

        //moveTo(43,18,0); //Move left/above sample 2
        forward(16,0.5);

        //moveTo(52,18,0); //Move above sample 2
        right(52,0.5);
        rotateTo(0,0.2);

        //moveTo(51,-20,0); //Push sample 2 into observation
        backward(-20,0.5);

        rotateTo(0,0.2);
        setSlidePosition(robot.vertSlide, robot.wallHeight,0.4);

        //Move out of observation
        forward(-5,0.4);
        left(50,0.5);

        //Rotate
        rotateTo(180,0.4);
        //sleep(1);
        //rotateTo(180,0.);

        //Graby graby the second one ya know?
        moveUntilSensor(robot.topTouch, 0.3);
        robot.closeClaw();

        //Move to chambers
        robot.odometer.resetTo(0,0,0);

        //exit observation
        backward(-18,0.4);
        setSlidePosition(robot.vertSlide, robot.aboveChamber,0.4);
        right(50,0.5);
        rotateTo(180,0.5);
        rotateTo(180,0.2);
        moveUntilSensor(robot.frontTouch, 0.3);
        setSlidePosition(robot.vertSlide, robot.belowChamber, 0.3);
        sleep(1000);
        robot.openClaw();
        setSlidePosition(robot.vertSlide, robot.belowChamber, 0.3);

        robot.odometer.resetTo(0,0,0);

        backward(-18,0.4);
        right(50,0.5);
        rotateTo(180,0.4);
        rotateTo(180,0.2);


        /* Nobody likes this stuff go away we gonna get 4
        //moveTo(52,18,0); //Move left/above sample 3
        forward(18,0.5);

        //moveTo(58,18,10); //Move above sample 3
        right(57,0.5);

        //moveTo(55,-20,10); //Push sample 3 into observation
        backward(-20,0.5);

         */
/*
        ///Grab and Clip 2nd specimen
        moveTo(45,-15,0); //Exit observation
        waitForMove();
        moveTo(45,-15,180); //Rotate 180
        waitForMove();
        stopTweetyBird();
        moveUntilSensor(robot.topTouch,0.3); //Top touch isn't touchin (More power?)
        closeClaw(); //Grab 2nd specimen
        setSlidePosition(robot.vertSlide, robot.wallHeight+400,0.4);
        startTweetyBird();
        */

    }

    public void setSlidePosition(DcMotor slide, int target, double power){
        slide.setTargetPosition(target);
        if(slide.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        slide.setPower(power);
    }

    public void rotateTo(int degree, double power){
        if(Math.toDegrees(robot.odometer.getZ()) > degree){ //Spin left
            robot.fl.setPower(-power);
            robot.bl.setPower(-power);
            robot.fr.setPower(power);
            robot.br.setPower(power);
            while(Math.toDegrees(robot.odometer.getZ()) > degree && opModeIsActive()){
                telemetry.addLine("Spinny left");
                telemetry.update();
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
        if(Math.toDegrees(robot.odometer.getZ()) < degree){ //Spin right
            robot.fl.setPower(power);
            robot.bl.setPower(power);
            robot.fr.setPower(-power);
            robot.br.setPower(-power);
            while(Math.toDegrees(robot.odometer.getZ()) < degree && opModeIsActive()){
                telemetry.addLine("Spinny right");
                telemetry.addLine("getZ" + Math.toDegrees(robot.odometer.getZ()));
                telemetry.addLine("target" + degree);
                telemetry.update();
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void forward(double yPosition, double power){
        robot.fl.setPower(power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(power);
        while(robot.odometer.getY() < yPosition && opModeIsActive()){
            telemetry.addLine("Going forward");
            telemetry.update();
        }
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

    public void backward(double yPosition, double power){
        robot.fl.setPower(-power);
        robot.fr.setPower(-power);
        robot.bl.setPower(-power);
        robot.br.setPower(-power);
        while(robot.odometer.getY() > yPosition && opModeIsActive()){
            telemetry.addLine("Going backward");
            telemetry.update();
        }
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

    public void left(double xPosition, double power){
        robot.fl.setPower(-power);
        robot.bl.setPower(power);
        robot.fr.setPower(power);
        robot.br.setPower(-power);
        while(robot.odometer.getX() > xPosition && opModeIsActive()){
            telemetry.addLine("Going left");
            telemetry.update();
        }
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

    public void right(double xPosition, double power){
        robot.fl.setPower(power);
        robot.bl.setPower(-power);
        robot.fr.setPower(-power);
        robot.br.setPower(power);
        while(robot.odometer.getX() < xPosition && opModeIsActive()){
            telemetry.addLine("Going right");
            telemetry.update();
        }
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }
    public void moveUntilSensor(@NonNull TouchSensor sensor, double speed){
        setAllWheelPower(speed);
        while(!sensor.isPressed()){
            telemetry.addLine("Waiting for " + sensor.getDeviceName());
        }
        telemetry.update();
        setAllWheelPower(0);

    }
    public void setAllWheelPower(double power){
        robot.fl.setPower(power);
        robot.fr.setPower(power);
        robot.bl.setPower(power);
        robot.br.setPower(power);
    }
}