package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//      Driver1Controls thread1 = new Driver1Controls(this);

public class Driver1Controls extends Thread{
    private final LinearOpMode opMode;
    public Driver1Controls(LinearOpMode opMode){
        this.opMode = opMode;
    }
    private NewRobotConfig robot;

    @Override
    public void run(){
        robot = new NewRobotConfig(opMode);
        robot.init(false);

        while(opMode.opModeIsActive()){
            //Get Driver input
            double axial = -opMode.gamepad1.left_stick_y;
            double lateral = opMode.gamepad1.left_stick_x;
            double yaw = opMode.gamepad1.right_stick_x;
            double mainThrottle = .2 + (opMode.gamepad1.right_trigger * 0.8);

            //Calculate wheel power
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            //Send wheel power with throttle multiplier
            robot.fl.setPower(leftFrontPower * mainThrottle);
            robot.fr.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            
        }

    }
}
