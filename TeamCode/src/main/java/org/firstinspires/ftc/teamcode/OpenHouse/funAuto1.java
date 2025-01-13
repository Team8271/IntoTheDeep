package org.firstinspires.ftc.teamcode.OpenHouse;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
@Autonomous(name="Auto - Open House") //add preselectTeleOp="Jax TeleOp" to turn preselect on
public class funAuto1 extends LinearOpMode {
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

        ///Start of Auto
        moveTo(1,-3,0);




    }

    public void moveTo(int x, int y, int z){
        robot.tweetyBird.sendTargetPosition(x,y,z);
    }
    public void waitForMove(){
        robot.tweetyBird.waitWhileBusy();
    }

}
