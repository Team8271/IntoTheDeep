package org.firstinspires.ftc.teamcode.Testing_Other;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.QualifierBot.Configuration;

//      OldDriver2Controls thread1 = new OldDriver2Controls(this);
//      thread1.run() MUST BE AFTER WAIT FOR START

public class OldDriver2Controls extends Thread {
    private final LinearOpMode opMode;
    public OldDriver2Controls(LinearOpMode opMode) {
        this.opMode = opMode;
    }
    private Configuration robot;


    @Override
    public void run() {

        robot.init();
        ///Define PIDControl Thread
        //PIDControl thread1 = new PIDControl(opMode);
        //thread1.start();
        ///Driver controls
        double vertControl = opMode.gamepad2.left_stick_y;
        double horziontalPower = opMode.gamepad2.right_stick_x;
        //boolean clipSpecimen = opMode.gamepad2.x;  //Clips the specimen
        boolean override = opMode.gamepad2.b;      //Override running code (ie clipping)
        //boolean vertToWall = opMode.gamepad2.a;  //Grab Specimen from wall
        //boolean vertToHighBasket = opMode.gamepad2.y;                //Go to High Basket
        boolean closeClaw = opMode.gamepad2.right_trigger < 0.5;   //Close the Claw
        boolean openClaw = opMode.gamepad2.right_trigger >= 0.5;   //Open the Claw

        while(opMode.opModeIsActive()){
            ///Vertical Slide Start

            if (robot.verticalLimiter.isPressed()) { //slide bottomed out
                if (vertControl < 0) {
                    vertControl = 0;
                }
                robot.vertMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                opMode.telemetry.addLine("Vertical slide bottomed out");
            } else if (robot.vertMotor.getCurrentPosition() >= robot.vertMax) { // Slide topped out
                if (vertControl > 0) {
                    vertControl = 0;
                }
                opMode.telemetry.addLine("Vertical slide topped out");
            }

            if (vertControl != 0) { //Moving
                if (robot.vertMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                    robot.vertMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                robot.vertMotor.setPower(vertControl);
                opMode.telemetry.addLine("Vertical slide moving");
            }
            else { //Stop and hold            /////Put an else if here when you want to make robot preset
                if (robot.vertMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                    int targetPosToHold = robot.vertMotor.getCurrentPosition();
                    if (targetPosToHold > robot.vertMax) {
                        targetPosToHold = robot.vertMax;
                    }
                    robot.vertMotor.setTargetPosition(targetPosToHold);
                    robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.vertMotor.setPower(.5);
                }
                opMode.telemetry.addLine("Vertical slide holding...");
            }
            ///Vertical Slide End

            ///Claw stuff
            //Close the claw
            if (closeClaw) {
                robot.closeClaw();
            }

            //Open the claw
            if (openClaw) {
                robot.openClaw();
            }

        }

    }

    /*
    public void clipSpecimen(PIDControl thread1){
        //clips the specimen on high chamber
        if(!thread1.isAlive()){
            thread1.start();
        }
        thread1.setTargetPosition(robot.aboveChamber); //ensure its in correct position
        while(thread1.isBusy){
            opMode.telemetry.addLine("Waiting for thread1");
            opMode.telemetry.update();
        }
        thread1.setTargetPosition(robot.belowChamber);
        robot.openClaw();
        //thread1.interrupt();//add some de-buggin stuff here but not now lol
    }

     */
}