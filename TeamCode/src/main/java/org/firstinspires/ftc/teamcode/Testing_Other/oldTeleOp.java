package org.firstinspires.ftc.teamcode.Testing_Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.QualifierBot.Configuration;

@Disabled
@TeleOp(name="Old Robot TeleOp")
public class oldTeleOp extends LinearOpMode {
    private Configuration robot;


    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init();
        OldDriver2Controls thread1 = new OldDriver2Controls(this);

        telemetry.addLine("Robot Initialized");
        telemetry.update();

        waitForStart();

        thread1.start();
        while(opModeIsActive()){
            telemetry.addLine("Ooh baby when you talk like that");
            telemetry.update();
        }



    }
}