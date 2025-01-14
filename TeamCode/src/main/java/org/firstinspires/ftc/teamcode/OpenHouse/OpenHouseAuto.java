package org.firstinspires.ftc.teamcode.OpenHouse;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Auto - Open House") //add preselectTeleOp="Jax TeleOp" to turn preselect on
public class OpenHouseAuto extends LinearOpMode {
    private OpenHouseConfig robot;



    @Override
    public void runOpMode(){
        ///Start of Initialization
        robot = new OpenHouseConfig(this);
        robot.init(true);
        robot.initTweatyBird();

        //Set vertMotor into runToPosition Mode
        robot.vertMotor.setPower(0);
        robot.vertMotor.setTargetPosition(robot.vertMotor.getCurrentPosition());
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Telemetry
        telemetry.addLine("Initialization Successful");
        telemetry.update();
        ///End of Initialization
        waitForStart(); //Wait for driver to press Start


        //z should be in degrees

        ///Start of real stuff
        //Grab and raise arm here
        moveTo(10,0,0);
        waitForMove();
        telemetry.addLine("End of program");
        telemetry.update();
        sleep(2000);




    }

    public void moveTo(double x, double y, double z){
        robot.tweetyBird.sendTargetPosition(x,y,Math.toRadians(z));
    }
    public void waitForMove(){
        robot.tweetyBird.waitWhileBusy();
        sleep(500);
    }

}
