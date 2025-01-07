package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//        PIDControl thread1 = new PIDControl(this);

public class PIDControl extends Thread{
    private final LinearOpMode opMode;
    public PIDControl(LinearOpMode opMode){
        this.opMode = opMode;
    }
    private NewRobotConfig robot;

    public boolean isBusy;

    public int targetPosition;

    public double motorPower;


    public double kP = 0.8; //Proportional Gain
    public double kI = 0.5; //Integral Gain
    public double kD = 0.5; //Derivative Gain

    public double currentEncoderPosition;
    public double error;
    public double previousError = 0;
    public double sumOfErrors;
    public double rateOfChangeOfError;
    public boolean madeHere1 = false;



    @Override
    public void run(){

        robot = new NewRobotConfig(opMode);
        robot.init(false);

        targetPosition = robot.vertMotor.getCurrentPosition();

        //Set target position
        robot.vertMotor.setPower(0);
        robot.vertMotor.setTargetPosition(targetPosition);

        //Set motor in RunToPosition mode
        if(robot.vertMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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




        while(opMode.opModeIsActive()){
            madeHere1 = true;
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

            motorPower = kP * error + kI * sumOfErrors + kD * rateOfChangeOfError; //Calculate power
            robot.vertMotor.setPower(motorPower); //Send power
            opMode.telemetry.update();


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
