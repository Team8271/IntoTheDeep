package org.firstinspires.ftc.teamcode.Testing_Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.QualifierBot.Configuration;
@Disabled
@TeleOp(name="Telemetry Testing")
public class TelemetryTesting extends LinearOpMode {
    private Configuration robot;




    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(false);


        telemetry.addLine("Initialized Config");

        int target = robot.vertMotor.getCurrentPosition();

        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.update();




        //PIDControl thread1 = new PIDControl(this);



        waitForStart();

        telemetry.addLine("Starting PIDControl thread");
        //thread1.start();
        telemetry.addLine("SUCCESS");


        while(opModeIsActive()) {
            telemetry.addLine("DpadUp for up\nDpadDown for down\n");
            //telemetry.addData("Alive?", thread1.isAlive());

            if(gamepad1.dpad_up){
                target+=100;
            }
            if(gamepad1.dpad_down){
                target+=100;
            }
            if(target < 0){
                target = 0;
            }
            //thread1.setTargetPosition(target);
            robot.vertMotor.setTargetPosition(target);
            robot.vertMotor.setPower(0.5);

            telemetry.addData("Target position", target);
            telemetry.addData("Current position", robot.vertMotor.getCurrentPosition());
            telemetry.update();



        }
    }
}
/* New Robot
Below High Chamber
Above High Chamber
At Wall
At High Basket


 */