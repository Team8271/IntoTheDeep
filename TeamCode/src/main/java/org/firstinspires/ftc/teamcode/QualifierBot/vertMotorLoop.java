package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class vertMotorLoop extends Thread{
    private final LinearOpMode opMode;
    private final Configuration robot;
    private final int nextPosition;

    public vertMotorLoop(LinearOpMode opMode, Configuration robot, int nextPosition){
        this.opMode = opMode;
        this.robot = robot;
        this.nextPosition = nextPosition;
    }

    @Override
    public void run(){

        robot.vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.vertMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.vertMotor.setPower(-0.8);

        while(opMode.opModeIsActive()){
            if(robot.verticalLimiter.isPressed()) {
                robot.vertMotor.setPower(0);
                robot.vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.vertMotor.setTargetPosition(nextPosition);
                robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.vertMotor.setPower(0.8);

                break;
            }
        }
    }
}
