package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Autonomous(name="Tweety Auto")
public class TweetyAuto extends LinearOpMode {
    Config robot;
    @Override
    public void runOpMode(){
        robot = new Config(this);
        robot.init();
        robot.initTweetyBird();

        closeClaw();

        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart(); //WAIT FOR USER TO PRESS START

        robot.boxServo.setPosition(robot.boxStoragePosition); //Set box servo
        setSlidePosition(robot.vertSlide, robot.aboveChamber, 0.8); //Set slide above chamber


        ///Clip preload
        robot.tweetyBird.engage();
        moveTo(-6,29,0);
        waitForMove();
        robot.tweetyBird.disengage();

        moveUntilSensor(robot.frontTouch, 0.3); //Move into submersible

        setSlidePosition(robot.vertSlide, robot.wallHeight,0.3); //Clip specimen
        sleep(1000);
        robot.openClaw(); //Open claw


        ///Pushing samples into observation
        robot.tweetyBird.engage();
        moveTo(0,-6,0); //Move back from submersible
        moveTo(35,-6,0); //Move to left/below sample 1
        moveTo(36,20,0); //Move to left/above sample 1
        moveTo(43,19,0); //Move to above sample 1
        moveTo(43,-21,0); //Push sample 1 into observation
        moveTo(43,18,0); //Move left/above sample 2
        moveTo(52,18,0); //Move above sample 2
        moveTo(51,-20,0); //Push sample 2 into observation
        moveTo(52,18,0); //Move left/above sample 3
        moveTo(58,18,10); //Move above sample 3
        moveTo(55,-20,10); //Push sample 3 into observation

        ///Grab and Clip 2nd specimen
        moveTo(45,-15,0); //Exit observation
        waitForMove();
        moveTo(45,-15,180); //Rotate 180
        waitForMove();
        stopTweetyBird();
        moveUntilSensor(robot.topTouch,0.3); //Top touch isn't touchin (More power?)
        closeClaw(); //Grab 2nd specimen
        setSlidePosition(robot.vertSlide, robot.wallHeight+400,0.4);
        robot.tweetyBird.engage();
        moveTo(0,-18,0);
        waitForMove();

        //flies backward
        clipCycle(0); //2nd
        clipCycle(-2); //3rd
        clipCycle(-4); //4th
        clipCycle(-6); //5th

    }

    //Start in position after grabbing clip
    public void clipCycle(double offset){
        //Sitting in observation and grabbed specimen
        robot.tweetyBird.engage();
        telemetry.addLine(robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ());
        telemetry.update();

        moveTo(0,-18,0); //Back out of observation
        waitForMove(); //Added to prevent backing into other team observation?? Might work
        setSlidePosition(robot.vertSlide, robot.aboveChamber,0.4);
        moveTo(50+offset,-25,180); //Rotate and move to submersible
        waitForMove();
        stopTweetyBird();
        moveUntilSensor(robot.frontTouch, 0.3);
        setSlidePosition(robot.vertSlide, robot.belowChamber,0.3);
        sleep(1000);
        robot.openClaw(); //Open the claw


        //Robot is sitting against submersible with specimen clipped and claw open
        robot.tweetyBird.engage();
        moveTo(0,-18,0); //Back off of submersible
        setSlidePosition(robot.vertSlide, robot.wallHeight, 0.3);
        moveTo(50-offset,-19,180); //Move to observation with rotation
        waitForMove();
        stopTweetyBird();
        moveUntilSensor(robot.topTouch, 0.3);
        closeClaw(); //Grab specimen
        //Sitting in observation grabbed specimen
    }

    public void closeClaw(){
        robot.closeClaw();
        sleep(500);
    }
    public void waitForMove(){
        robot.tweetyBird.waitWhileBusy();
    }
    public void moveTo(double x, double y, double z){
        robot.tweetyBird.addWaypoint(x,y,z);
    }
    public void setSlidePosition(DcMotor slide, int target, double power){
        slide.setTargetPosition(target);
        if(slide.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        slide.setPower(power);
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


//        setSlidePosition(robot.vertMotor, robot.aboveBasket, 0.5)

    public void bucketAuto(){
        robot.boxServo.setPosition(robot.boxStoragePosition); //Set box servo

        robot.tweetyBird.engage();
        //moveTo();
    }
}
