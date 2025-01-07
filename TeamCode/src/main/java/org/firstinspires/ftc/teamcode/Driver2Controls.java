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
        //Driver controls
        double verticalPower = opMode.gamepad2.left_stick_y;
        double horziontalPower = opMode.gamepad2.right_stick_x;
        boolean clipSpecimen = opMode.gamepad2.x;  //Clips the specimen
        boolean override = opMode.gamepad2.b;      //Override running code (ie clipping)
        boolean grabFromWall = opMode.gamepad2.a;  //Grab Specimen from wall
        boolean goToHighBasket = opMode.gamepad2.y;                //Go to High Basket
        boolean closeClaw = opMode.gamepad2.right_trigger < 0.5;   //Close the Claw
        boolean openClaw = opMode.gamepad2.right_trigger >= 0.5;   //Open the Claw

        //Send slide power
        robot.leftHorz.setPower(horziontalPower);
        robot.rightHorz.setPower(horziontalPower);
        robot.vertMotor.setPower(verticalPower);

        if(clipSpecimen){
            robot.clipSpecimen();
        }



    }
}
