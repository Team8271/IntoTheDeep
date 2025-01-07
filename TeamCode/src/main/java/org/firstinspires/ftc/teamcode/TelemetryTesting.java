package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Telemetry Testing")
public class TelemetryTesting extends LinearOpMode {
    private NewRobotConfig robot;




    //Main OpMode
    @Override
    public void runOpMode() {
        robot = new NewRobotConfig(this);
        robot.init(false);


        telemetry.addLine("Initialized Config");

        telemetry.update();



        PIDControl thread1 = new PIDControl(this);


        telemetry.addLine("Starting PIDControl thread");
        thread1.start();
        telemetry.addLine("SUCCESS");

        telemetry.update();
        int target = robot.vertMotor.getCurrentPosition();

        waitForStart();


        while(opModeIsActive()) {
            telemetry.addLine("DpadUp for up\nDpadDown for down\n");
            telemetry.addData("active?", thread1.isAlive());
            telemetry.addData("madehere1?", thread1.madeHere1);

            if(gamepad1.dpad_up){
                target++;
            }
            if(gamepad1.dpad_down){
                target--;
            }
            if(target < 0){
                target = 0;
            }
            thread1.targetPosition = target;
            telemetry.addData("PID target", thread1.targetPosition);
            telemetry.addData("PID power", thread1.motorPower);
            telemetry.addData("kP", thread1.kP);
            telemetry.addData("error", thread1.error);
            telemetry.addData("kI", thread1.kI);
            telemetry.addData("sumOfErrors", thread1.sumOfErrors);
            telemetry.addData("kD", thread1.kD);
            telemetry.addData("rateOfChangeOfError", thread1.rateOfChangeOfError);
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