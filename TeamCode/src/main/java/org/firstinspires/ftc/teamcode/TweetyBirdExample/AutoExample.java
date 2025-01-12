package org.firstinspires.ftc.teamcode.TweetyBirdExample;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Auto Example")
public class AutoExample extends LinearOpMode {
    private ConfigurationExample robot;

    @Override
    public void runOpMode(){
        robot = new ConfigurationExample(this);
        robot.init(); //Run basic init
        robot.initTweetyBird(); //Run TweetyBird init

        telemetry.addLine("Successfully Initialized");
        telemetry.update();

        //Wait for driver to press START
        waitForStart();


        /* sendTargetPosition(x,y,z)
         * your robot starts at 0,0,0
         * it moves along the x and y axis accordingly
         * the z axis is rotation
         */

        /* waitWhileBusy()
         * waits until TweetyBird has finished moving to desired position
         */

        /* close()
         * exits TweetyBird
         * If using as a macro in teleOp it will take your control from defined mecanum motors until closed
         * Recommended to put at end of OpMode
         */



        //Tell TweetyBird to go to 15,10,0 (Moves to the right 15 inches and forward 10, no rotation)
        robot.tweetyBird.sendTargetPosition(15,10,0);
        //Wait for TweetyBird to reach the target
        robot.tweetyBird.waitWhileBusy();

        robot.tweetyBird.close();

        //Whether TweetyBird is running or not you can get your x,y,z coordinates using:
        robot.odometer.getX();
        robot.odometer.getY();
        robot.odometer.getZ();
        //You can put this in a while loop to constantly read the values for testing
        //  ie: Testing if you need to flip encoders in init



    }
}
