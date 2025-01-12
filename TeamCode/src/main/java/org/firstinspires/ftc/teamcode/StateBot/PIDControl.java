package org.firstinspires.ftc.teamcode.StateBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.QualifierBot.Configuration;

//        PIDControl thread1 = new PIDControl(this);
//        YOU MUST START THE THREAD AFTER WAIT FOR START!(thread1.start)

public class PIDControl extends Thread{
    private final LinearOpMode opMode;
    public PIDControl(LinearOpMode opMode){
        this.opMode = opMode;
    }

    public boolean isBusy;

    private int targetPosition;


    public void setTargetPosition(int target){
        targetPosition = target;
    }





    @Override
    public void run(){

        Configuration robot = new Configuration(opMode);
        robot.init(false);

        targetPosition = robot.vertMotor.getCurrentPosition();

        //Set target position
        robot.vertMotor.setPower(0);
        robot.vertMotor.setTargetPosition(targetPosition);

        //Set motor in RunToPosition mode
        if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            robot.vertMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        double startTime = opMode.getRuntime();
        double changeInTime;

        double kP = 0.8; //Proportional Gain
        double kI = 0.5; //Integral Gain
        double kD = 0.5; //Derivative Gain

        double currentEncoderPosition;
        double error;
        double previousError = 0;
        double sumOfErrors;
        double rateOfChangeOfError;



        while(opMode.opModeIsActive()) {
            robot.vertMotor.setTargetPosition(targetPosition);

        /*if(targetPosition == 0 && robot.verticalMotor.getCurrentPosition() >= 30 && !robot.verticalLimiter.isPressed()){ //if think all the way down but not
            oldTargetPosition = targetPosition;
            robot.verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.verticalMotor.setPower(-0.8);
            while(!robot.verticalLimiter.isPressed() && oldTargetPosition == targetPosition){ //While not press and target doesn't change
                opMode.telemetry.addLine("Vertical Slide moving to 0, (Think at 0 but not)");
            }

        }*/
            changeInTime = opMode.getRuntime() - startTime; //Change in time
            currentEncoderPosition = robot.vertMotor.getCurrentPosition(); //Get the Current Position
            error = targetPosition - currentEncoderPosition;             //The Distance from target
            sumOfErrors = error * changeInTime; //Error value * change in time //Within while
            rateOfChangeOfError = previousError - error; //prev error subtract current error

            double motorPower = kP * error + kI * sumOfErrors + kD * rateOfChangeOfError; //Calculate power
            robot.vertMotor.setPower(motorPower); //Send power
            opMode.telemetry.addData("PID Motor Power", motorPower);
            opMode.telemetry.update();


            if (motorPower > 0.2) {
                isBusy = false;
            } else {
                isBusy = true;
            }

            previousError = error; //Get the previous Error

        }
    }
}
