package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//      Driver2Controls thread1 = new Driver2Controls(this);

public class Driver2Controls extends Thread{
    private final LinearOpMode opMode;
    public Driver2Controls(LinearOpMode opMode){
        this.opMode = opMode;
    }
    private NewRobotConfig robot;



    @Override
    public void run(){
        //Define PIDControl Thread
        PIDControl thread1 = new PIDControl(opMode);
        thread1.start();
        //Driver controls
        double verticalPower = opMode.gamepad2.left_stick_y;
        double horziontalPower = opMode.gamepad2.right_stick_x;
        boolean clipSpecimen = opMode.gamepad2.x;  //Clips the specimen
        boolean override = opMode.gamepad2.b;      //Override running code (ie clipping)
        boolean vertToWall = opMode.gamepad2.a;  //Grab Specimen from wall
        boolean vertToHighBasket = opMode.gamepad2.y;                //Go to High Basket
        boolean closeClaw = opMode.gamepad2.right_trigger < 0.5;   //Close the Claw
        boolean openClaw = opMode.gamepad2.right_trigger >= 0.5;   //Open the Claw

        //Send slide power
        robot.leftHorz.setPower(horziontalPower);
        robot.rightHorz.setPower(horziontalPower);
        robot.vertMotor.setPower(verticalPower);

        //Clip Specimen
        if(clipSpecimen){
            clipSpecimen(thread1);
        }

        //Bring Vertical slide to wall grabbing position
        if(vertToWall){
            thread1.setTargetPosition(robot.wallHeight);
        }

        if(vertToHighBasket){
            thread1.setTargetPosition(robot.highBasket);
        }

        //Close the claw
        if(closeClaw){
            robot.closeClaw();
        }

        //Open the claw
        if(openClaw){
            robot.openClaw();
        }





    }

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
}
