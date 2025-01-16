package org.firstinspires.ftc.teamcode.ZSchoolBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name="Zigzag")
@Disabled
public class AutoZigzag extends LinearOpMode {
    private HardwareMap robot = null;
    @Override
    public void runOpMode () {
        robot = new HardwareMap(this);
        robot.init();

        for(int i = 0; i<5; i++) {
            waitForStart();

            setMotorPower(.5,.5,0);

            sleep(500);

            setMotorPower(.5,-.5,0);

            sleep(500);
        }

    }

    private void setMotorPower(double axial, double lateral, double yaw) {
        robot.fl.setPower(axial+lateral+yaw);
        robot.fr.setPower(axial-lateral-yaw);
        robot.bl.setPower(axial-lateral+yaw);
        robot.br.setPower(axial+lateral-yaw);
    }
}
