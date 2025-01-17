package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.StateBot.PIDControl;

import java.util.HashMap;

@Autonomous (name="Jax Auto") //add preselectTeleOp="Jax TeleOp" to turn preselect on
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean swapDirection = false;



    double lowPower = 0.2;
    double normalPower = 0.4;
    double highPower = 0.6;
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init();
        robot.initTweatyBird();



        robot.vertMotor.setTargetPosition(robot.vertMotor.getCurrentPosition());
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(0.8);

        telemetry.addLine("Initialized");
        String position = robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ();
        telemetry.addData("Current Pos:", position);
        telemetry.update();

        //Wait for driver to press START
        waitForStart();

        runtime.reset();


        telemetry.clear();

        helenaAutoChamber();






    }


    //Remove wait and it curve thing


    //Claw takes about a second to close, maybe put them preemptively
    //Time save could be flip around as discussed w/ Mr.V
    private void helenaAutoChamber(){
        vertMotorLoop vertResetAndWall = new vertMotorLoop(this,robot,robot.vertWall);


        //Clipping preloaded specimen
        robot.closeClaw();//close claw
        robot.boxServo.setPosition(0); //Move boxServo out of way
        robot.flipServo.setPosition(.6);
        sleep(1000); //wait for clawServos
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Start moving vertMotor up
        sleep(1000); //Wait for vertMotor
        robot.tweetyBird.sendTargetPosition(-8,30,0); //Go in-front of chambers
        robot.tweetyBird.waitWhileBusy(); //Wait until robot reaches front of chambers
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip the Specimen
        sleep(1000); //Wait for vertMotor
        robot.openClaw(); //Release the Specimen
        sleep(600); //Wait for clawServos
        vertResetAndWall.start();//Lowers the vert to reset then sets to wall height

        //Start of moving to first sample
        robot.tweetyBird.sendTargetPosition(-8,15,0); //Clear Submersible before rotation
        robot.tweetyBird.sendTargetPosition(27,20,-180); //Move to clear truss of submersible
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(27,48,-180); //Move to the left of sample and above
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(38,48,-180); //Move above the sample
        robot.tweetyBird.waitWhileBusy(); //Wait for robot to be above sample completely





        //Pushing first sample into Observation and grab 2nd Specimen
        robot.tweetyBird.sendTargetPosition(38,-5,-180); //Push sample in observation and go to grab position
        robot.tweetyBird.waitWhileBusy();
        robot.closeClaw();
        sleep(600); //Wait for claw to close
        robot.vertMotor.setTargetPosition(robot.vertWall+400);
        sleep(100);

        //Moving to Submersible to clip 2nd Specimen
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Position vertMotor to clip
        robot.tweetyBird.sendTargetPosition(38, 15,-180); //Back off wall so don't hit it when rotating
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(-10,25,0); //Move in-front of submersible
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(-10,31,0); //Move into clipping position
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip specimen
        sleep(1000); //Wait for vertMotor to clip
        robot.openClaw();
        sleep(600); //Wait for claw to open
        vertResetAndWall.start(); //Reset vertMotor

        //Moving to Observation and Grabbing 3rd Specimen
        robot.vertMotor.setTargetPosition(robot.vertWall); //Move vertMotor to wall height
        robot.tweetyBird.sendTargetPosition(38,20,-180); //Rotate and align with Observation Zone
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(38,-6,-180); //Go into Grabbing position
        robot.tweetyBird.waitWhileBusy();
        robot.closeClaw(); //Close claw
        sleep(600); //Wait for claw to close
        robot.vertMotor.setTargetPosition(robot.vertWall+400); //Remove Specimen from wall

        //Moving to Submersible and Clipping 3rd Specimen
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Position vertMotor to clip
        robot.tweetyBird.sendTargetPosition(-10,25,0); //Move in-front of submersible
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(-10,32,0); //Move into clipping position
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip specimen
        sleep(1000); //Wait for vertMotor to clip
        robot.openClaw();
        sleep(600); //Wait for claw to open
        vertResetAndWall.start(); //Reset vertMotor

        telemetry.addData("Time to complete", runtime);


        //Moving to grab 3rd specimen
        robot.tweetyBird.sendTargetPosition(0,25,0); //Move to clear trusses
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(37,20,180); //Align with observation
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertWall); //Move vertMotor to wall
        robot.tweetyBird.sendTargetPosition(37,-6,180); //Go to grab Specimen second time
        robot.tweetyBird.waitWhileBusy();
        robot.closeClaw(); //Close the claw
        sleep(300);
        robot.vertMotor.setTargetPosition(robot.vertWall+300); //Remove specimen from wall
        robot.tweetyBird.sendTargetPosition(37,20,180); //move off of wall
        robot.tweetyBird.waitWhileBusy();
        vertResetAndWall.start(); //Bring vertMotor to reset
        robot.tweetyBird.sendTargetPosition(-12,20,0); //Align with submersible
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Raise vertMotor
        robot.tweetyBird.sendTargetPosition(-12,30,0); //Move to submersible
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip the specimen
        robot.openClaw(); //open claw

        robot.tweetyBird.close();
        

        
        

    }


}
