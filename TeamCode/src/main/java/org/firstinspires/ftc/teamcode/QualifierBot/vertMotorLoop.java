package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class vertMotorLoop extends Thread{
    private final LinearOpMode opMode;
    public vertMotorLoop(LinearOpMode opMode){
        this.opMode = opMode;
    }

    @Override
    public void run(){
        Configuration robot = new Configuration(opMode);
        robot.init();

        robot.vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.vertMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.vertMotor.setPower(-0.8);

        while(opMode.opModeIsActive()){
            if(robot.verticalLimiter.isPressed()){
                robot.vertMotor.setPower(0);
                robot.vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.vertMotor.setTargetPosition(robot.vertMotor.getCurrentPosition());
                robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.vertMotor.setPower(0.8);
            }
        }
    }
}
