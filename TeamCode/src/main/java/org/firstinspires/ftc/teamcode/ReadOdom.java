package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="ReadOdom")
public class ReadOdom extends LinearOpMode {
    Config robot;
    @Override
    public void runOpMode(){
        robot = new Config(this);
        robot.init();

        waitForStart();

        while (opModeIsActive()){
            telemetry.addLine("Left " + robot.bl.getCurrentPosition());
            telemetry.addLine("Right " + robot.fr.getCurrentPosition());
            telemetry.addLine("Middle " + robot.br.getCurrentPosition());
            telemetry.update();
        }
    }
}
