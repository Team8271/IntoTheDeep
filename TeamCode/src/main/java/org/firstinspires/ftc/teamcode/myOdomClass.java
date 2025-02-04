package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class myOdomClass extends Thread{
    DcMotor leftEncoder;
    DcMotor rightEncoder;
    DcMotor middleEncoder;

    public void setLeftEncoder(DcMotor leftEncoder){
        this.leftEncoder = leftEncoder;
    }
    public void setRightEncoder(DcMotor rightEncoder){
        this.rightEncoder = rightEncoder;
    }
    public void setMiddleEncoder(DcMotor middleEncoder){
        this.middleEncoder = middleEncoder;
    }
    public void setEncoderTicksPerRotation(double encoderTicksPerRotation){
        //this. you got it
    }


    @Override
    public void run(){

        double encoderTicksPerRoation = 2000;
        double encoderWheelRadius = 0.944882;
        boolean flipLeftEncoder = true;
        boolean flipRightEncoder = true;
        boolean flipMiddleEncoder = true;

        double leftEncoderInches = leftEncoder.getCurrentPosition()/encoderTicksPerRoation * encoderWheelRadius;
        double rightEncoderInches = rightEncoder.getCurrentPosition()/encoderTicksPerRoation * encoderWheelRadius;
        double middleEncoderInches = middleEncoder.getCurrentPosition()/encoderTicksPerRoation * encoderWheelRadius;

        if(flipLeftEncoder){
            leftEncoderInches = -leftEncoderInches;
        }
        if(flipRightEncoder){
            rightEncoderInches = -rightEncoderInches;
        }
        if(flipMiddleEncoder){
            middleEncoderInches = -middleEncoderInches;
        }
    }
}
