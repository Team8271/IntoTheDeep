package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name="auto")
public class Auto extends LinearOpMode {
    private HardwareMap robot = null;
    @Override
    public void runOpMode () {
        robot = new HardwareMap(this);
        robot.init();


        waitForStart();

        setMotorPower(0.35,0,0);

        sleep(1500);

        setMotorPower(0,-.5,0);

        sleep(500);

        setMotorPower(0,0,0);

        robot.intake.setPower(1);

        sleep(500);

        setMotorPower(0,0,1);

        sleep(1000);

        setMotorPower(0,0,0);

        robot.intake.setPower(0);
    }

    private void setMotorPower(double axial, double lateral, double yaw) {
        robot.fl.setPower(axial+lateral+yaw);
        robot.fr.setPower(axial-lateral-yaw);
        robot.bl.setPower(axial-lateral+yaw);
        robot.br.setPower(axial+lateral-yaw);
    }
}
