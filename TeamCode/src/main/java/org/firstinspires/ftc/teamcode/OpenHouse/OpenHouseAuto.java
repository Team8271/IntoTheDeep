package org.firstinspires.ftc.teamcode.OpenHouse;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
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

        ///Start of Auto
        robot.closeClaw();
        sleep(500); //Wait half a second for claw to close
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Start moving vert above chamber
        //moveTo(); //Move in-front of Chambers (Claw Clearance)
        waitForMove();

        //while vertMotor is more than ten away from target
        while(Math.abs(robot.vertAboveChamber - robot.vertMotor.getCurrentPosition()) > 10){
            sleep(100); //Wait for vertMotor
        }

        //moveTo(); //Move into clipping position
        waitForMove();

        //Set vertical motor beneath the chamber to clip
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber);
        //Wait for vertMotor to reach below chamber (within 5)
        while(Math.abs(robot.vertBelowChamber - robot.vertMotor.getCurrentPosition()) > 5){
            sleep(100);
        }
        //Open the claw
        robot.openClaw();
        sleep(500);

        //moveTo(); //pickup here
        //Bring vertMotor down until vertLim is pressed (reset inaccurate values)




    }

    public void moveTo(int x, int y, int z){
        robot.tweetyBird.sendTargetPosition(x,y,z);
    }
    public void waitForMove(){
        robot.tweetyBird.waitWhileBusy();
    }

}
