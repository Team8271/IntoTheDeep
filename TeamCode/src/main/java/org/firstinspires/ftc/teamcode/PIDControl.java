package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class PIDControl extends Thread{
    private final LinearOpMode opMode;
    public PIDControl(LinearOpMode opMode){
        this.opMode = opMode;
    }
    private Configuration robot;

    public boolean isBusy;


    public int targetPosition;



    public void setVerticalTargetPosition(int targetPosition){
        this.targetPosition = targetPosition;
    }



    @Override
    public void run(){

        robot = new Configuration(opMode);
        robot.init(false);

        //Set target position
        robot.verticalMotor.setPower(0);
        robot.verticalMotor.setTargetPosition(targetPosition);

        //Set motor in RunToPosition mode
        if(robot.verticalMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        int oldTargetPosition;

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

        opMode.telemetry.addData("Position", robot.verticalMotor.getCurrentPosition());



        while(opMode.opModeIsActive()){
            robot.verticalMotor.setTargetPosition(targetPosition);
            if(targetPosition == 0 && robot.verticalMotor.getCurrentPosition() >= 30 && !robot.verticalLimiter.isPressed()){ //if think all the way down but not
                oldTargetPosition = targetPosition;
                robot.verticalMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.verticalMotor.setPower(-0.8);
                while(!robot.verticalLimiter.isPressed() && oldTargetPosition == targetPosition){ //While not press and target doesn't change
                    opMode.telemetry.addLine("Vertical Slide moving to 0, (Think at 0 but not)");
                }

            }
            changeInTime = opMode.getRuntime() - startTime; //Change in time
            currentEncoderPosition = robot.verticalMotor.getCurrentPosition(); //Get the Current Position
            error = targetPosition - currentEncoderPosition;             //The Distance from target
            sumOfErrors = error * changeInTime; //Error value * change in time //Within while
            rateOfChangeOfError = previousError - error; //prev error subtract current error

            double motorPower = kP * error + kI * sumOfErrors + kD * rateOfChangeOfError; //Calculate power
            robot.verticalMotor.setPower(motorPower); //Send power

            if(motorPower > 0.2){
                isBusy = false;
            }
            else{
                isBusy = true;
            }

            previousError = error; //Get the previous Error
        }
    }
}
