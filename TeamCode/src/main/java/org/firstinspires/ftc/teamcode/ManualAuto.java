package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Manual Auto", preselectTeleOp = "Main TeleOp")
public class ManualAuto extends LinearOpMode {
    Config robot;
    @Override
    public void runOpMode(){
        robot = new Config(this);
        robot.init();
        telemetry.setAutoClear(false);

        robot.closeClaw();

        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart(); // - - - - - - - - - - - - - - - - - Wait for START

        telemetry.addLine("Started");
        telemetry.update();

        robot.boxServo.setPosition(robot.boxStoragePosition); //Set box servo out of the way

        //Sleeping between each to show stopping point per move
        moveTo(0,5,0);
        sleep(1000);
        moveTo(0,-5,0);
        sleep(1000);
        moveTo(-5,-5,0);
        sleep(1000);
        moveTo(5,-5,0);
        sleep(1000);
        moveTo(5,-5,180);
        sleep(1000);
        moveTo(0,0,0);

        telemetry.addLine("End of Testing");
        telemetry.update();

        //Keep telemetry on the screen
        sleep(100000);
    }

    //This uses a linear slowdown; may switch to quadratic for better slowdown
    //I'm so tired right now, if this doesn't work you can implement similar logic to your teleop
    //Basically the same thing don't know why I didn't do it
    public void moveTo(double targetX, double targetY, double targetDegreeZ){
        double xAxisPower; //Positive moves right
        double yAxisPower; //Positive moves up
        double zAxisPower; //Positive rotates right
        int slowDownDistance = 5; //Distance to start slowing at
        double maxSpeed = 0.8; //Max speed robot can move
        double xPower;
        double yPower;
        double zPower;
        boolean targetReached = false;
        int distanceBuffer = 1;
        int rotationBuffer = 1;

        //While position is not reached
        while(opModeIsActive() && !targetReached){
            //Define positions for easy use
            double currentX = robot.odometer.getX();
            double currentY = robot.odometer.getY();
            double currentZ = robot.odometer.getZ();

            ///Moving on the X-axis
            double xDistanceFromTarget = Math.abs(currentX-targetX);
            if(xDistanceFromTarget <= slowDownDistance){
                xPower = xDistanceFromTarget/10;
            }
            else{
                xPower = maxSpeed;
            }

            if(currentX >= targetX){ //Robot is too far right
                xAxisPower = -xPower;
                //Make it send less power the closer it gets
            }
            else{   //if(currentX < targetX){ //Robot is too far left
                xAxisPower = xPower;
                //Make it send less power the closer it gets
            }


            ///Moving on the Y-axis
            double yDistanceFromTarget = Math.abs(currentY-targetY);
            if(yDistanceFromTarget <= slowDownDistance){
                yPower = yDistanceFromTarget/10;
            }
            else{
                yPower = maxSpeed;
            }

            if(currentY >= targetY){ //Robot is too far forward
                yAxisPower = -yPower;
            }
            else{   //if(currentY < targetY){ //Robot is too far backward
                yAxisPower = yPower;
            }

            ///Moving on the Z-axis
            double zDistanceFromTarget = Math.abs(currentZ-Math.toRadians(targetDegreeZ));
            if(zDistanceFromTarget <= slowDownDistance){
                zPower = zDistanceFromTarget/10;
            }
            else{
                zPower = maxSpeed;
            }

            if(currentZ >= Math.toRadians(targetDegreeZ)){ //Robot needs to rotate counterclockwise
                zAxisPower = -zPower;
            }
            else{   //if(currentZ < targetZ){ //Robot needs to rotate clockwise
                zAxisPower = zPower;
            }




            //Calculate power for each wheel
            /* Old one
            double flPower = yAxisPower -xAxisPower +zAxisPower;
            double frPower = yAxisPower +xAxisPower -zAxisPower;
            double blPower = yAxisPower +xAxisPower +zAxisPower;
            double brPower = yAxisPower -xAxisPower -zAxisPower;
            */

            double flPower = yAxisPower -xAxisPower +zAxisPower;
            double frPower = yAxisPower +xAxisPower -zAxisPower;
            double blPower = yAxisPower +xAxisPower +zAxisPower;
            double brPower = yAxisPower -xAxisPower -zAxisPower;

            //Ensure power does not exceed limits
            flPower = Range.clip(flPower, -maxSpeed, maxSpeed);
            frPower = Range.clip(frPower, -maxSpeed, maxSpeed);
            blPower = Range.clip(blPower, -maxSpeed, maxSpeed);
            brPower = Range.clip(brPower, -maxSpeed, maxSpeed);

            robot.fl.setPower(flPower);
            robot.fr.setPower(frPower);
            robot.bl.setPower(blPower);
            robot.br.setPower(brPower);


            //Check if x,y,z distance from targets are close enough
            //If so then mark move as complete
            if(xDistanceFromTarget < distanceBuffer &&
                yDistanceFromTarget < distanceBuffer &&
                zDistanceFromTarget < rotationBuffer){
                robot.fl.setPower(0);
                robot.fr.setPower(0);
                robot.bl.setPower(0);
                robot.br.setPower(0);
                targetReached = true;
                telemetry.addLine("Successfully moved to " + targetX + ", " + targetY + ", " + targetDegreeZ);
                telemetry.update();
            }

        }
    }



}
