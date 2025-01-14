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



        ///Start of real stuff
        //Grab and raise arm here
        moveTo(0,0,0);
        waitForMove();

        moveTo(-23,22,0);
        waitForMove();
        moveTo(-23,51,0);
        waitForMove();
        moveTo(-34,52,0);
        waitForMove();
        moveTo(-38,11,0);
        waitForMove();
        moveTo(-34,54,0);
        waitForMove();
        moveTo(-44,54,0);
        waitForMove();
        moveTo(-47,10,0);
        waitForMove();
        moveTo(-43,54,0);
        waitForMove();
        moveTo(-50,54,0);
        waitForMove();
        moveTo(-53,12,0);
        waitForMove();
        moveTo(-63,42,2);
        waitForMove();
        sleep(1000);
        telemetry.addLine("End of program");
        telemetry.update();
        sleep(2000);




    }

    public void moveTo(int x, int y, int z){
        robot.tweetyBird.sendTargetPosition(y,x,z);
    }
    public void waitForMove(){
        robot.tweetyBird.waitWhileBusy();
        sleep(500);
    }

}
