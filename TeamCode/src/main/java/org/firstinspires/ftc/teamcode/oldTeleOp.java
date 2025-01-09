package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Old Robot TeleOp")
public class oldTeleOp extends LinearOpMode {
    private Configuration robot;


    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(false);
        //OldDriver2Controls thread1 = new OldDriver2Controls(this);

        telemetry.addLine("Robot Initialized");
        telemetry.update();

        waitForStart();

        //thread1.start();
        while(opModeIsActive()){
            telemetry.addLine("Ooh baby when you talk like that");
            telemetry.update();
        }



    }
}