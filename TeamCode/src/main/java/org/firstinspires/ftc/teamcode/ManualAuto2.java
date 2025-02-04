package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Autonomous(name="This one!")
public class ManualAuto2 extends LinearOpMode {
    Config robot;

    @Override
    public void runOpMode(){
        robot = new Config(this);
        robot.init();

        robot.closeClaw(false);

        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart();

        robot.boxServo.setPosition(robot.boxStoragePosition);
        /*
        robot.setSlidePosition(robot.vertSlide, robot.aboveChamber,0.5);
        forward(20); //Move off of submersible
        left(-5);    //Align with clip spot
        robot.moveUntilSensor(robot.frontTouch, 0.3);
        robot.setSlidePosition(robot.vertSlide, robot.belowChamber, 0.3);
        */


        ///START OF TEST
        forward(5);
        sleep(1000);
        right(5);
        sleep(1000);
        left(0);
        sleep(1000);
        reverse(3);
        




    }

    public void forward(double desiredY){
        while(robot.odometer.getY() < desiredY && opModeIsActive()){
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(robot.fastSpeed);
            robot.bl.setPower(robot.fastSpeed);
            robot.br.setPower(robot.fastSpeed);
            while(desiredY > robot.odometer.getY() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void reverse(double desiredY){
        while(robot.odometer.getY() > desiredY && opModeIsActive()){
            robot.fl.setPower(-robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(-robot.fastSpeed);
            robot.br.setPower(-robot.fastSpeed);
            while(desiredY < robot.odometer.getY() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void left(double desiredX){
        while(robot.odometer.getX() > desiredX && opModeIsActive()){
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(-robot.fastSpeed);
            robot.br.setPower(robot.fastSpeed);
            while(desiredX < robot.odometer.getX() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void right(double desiredX){
        while(robot.odometer.getX() < desiredX && opModeIsActive()){
            robot.fl.setPower(-robot.fastSpeed);
            robot.fr.setPower(robot.fastSpeed);
            robot.bl.setPower(robot.fastSpeed);
            robot.br.setPower(-robot.fastSpeed);
            while(desiredX > robot.odometer.getX() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void moveOnY(double y){
        if(y > robot.odometer.getY()){ //Target is above
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(robot.fastSpeed);
            robot.bl.setPower(robot.fastSpeed);
            robot.br.setPower(robot.fastSpeed);
            while(y > robot.odometer.getY() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
        if(y < robot.odometer.getY()){ //Target is below
            robot.fl.setPower(-robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(-robot.fastSpeed);
            robot.br.setPower(-robot.fastSpeed);
            while(y < robot.odometer.getY() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void moveOnX(double x){
        if(x > robot.odometer.getX()){ //Target is right
            robot.fl.setPower(-robot.fastSpeed);
            robot.fr.setPower(robot.fastSpeed);
            robot.bl.setPower(robot.fastSpeed);
            robot.br.setPower(-robot.fastSpeed);
            while(x > robot.odometer.getX() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
        if(x < robot.odometer.getX()){ //Target is left
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(-robot.fastSpeed);
            robot.br.setPower(robot.fastSpeed);
            while(x < robot.odometer.getX() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

    public void moveOnZ(double z){
        if(Math.toRadians(z) > robot.odometer.getZ()){ //Target is clockwise
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(robot.fastSpeed);
            robot.br.setPower(-robot.fastSpeed);
            while(Math.toRadians(z) > robot.odometer.getZ() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
        if(Math.toRadians(z) < robot.odometer.getZ()){ //Target is counterclockwise
            robot.fl.setPower(robot.fastSpeed);
            robot.fr.setPower(-robot.fastSpeed);
            robot.bl.setPower(-robot.fastSpeed);
            robot.br.setPower(robot.fastSpeed);
            while(Math.toRadians(z) < robot.odometer.getZ() && opModeIsActive()){
                sleep(100);
            }
            robot.fl.setPower(0);
            robot.fr.setPower(0);
            robot.bl.setPower(0);
            robot.br.setPower(0);
        }
    }

}
