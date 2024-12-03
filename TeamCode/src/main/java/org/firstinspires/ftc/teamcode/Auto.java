package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="Auto")
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean swapDirection = false;

    double lowPower = 0.2;
    double normalPower = 0.4;
    double highPower = 0.6;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(true);

        //set all the wheels to brake on no power (remove drifting)
        robot.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Wait for driver to press START
        waitForStart();










        //run main auto
        //runAuto1();

    }


    private void mainAuto(int delay, int startPosition){
        robot.closeClaw(); //grab preloaded specimen
        sleep(delay);//Adjustable delay
        alignWithSubmersible(startPosition);//align with submersible
        setVerticalPosition(vertChamber);//Start moving Vertical Slide to high position
        driveToSubmersible();//Move forward until Touch Sensor is pressed
        clipSpecimin();//Clip the Specimen on the high chamber
        reverse(5, normalPower); //clear the submersible
        right(39, normalPower); //go to observation
        rotate180(); // face the human element
        grabSpecimenFromWall();




        setVerticalPosition(vertWall);//Start moving vert into position
        driveUntilBlocked("forward"); //Drive into wall
        robot.closeClaw();
    }

    public void grabSpecimenFromWall(){
        setVerticalPosition(vertWall);//Start moving Vertical slide into position
        driveUntilBlocked("forward");//Drive into wall
        robot.closeClaw();//Close the Claw
        sleep(300);//Give Claw time to close
        setVerticalPosition(vertWall+200);//Lift slide so Specimen clears wall
    }

    public void setVerticalPosition(int position){
        robot.verticalMotor.setTargetPosition(position);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);
    }

    public void driveUntilBlocked(String direction){
        //magic goes here
    }





    private void runAuto1(){
        //initialize the boxServo
        robot.boxServo.setPosition(.6);




        //Reset odom values
        robot.odometer.resetTo(0,0,0);



        //close claw
        robot.closeClaw();
        telemetry.addLine("Red and Blue closed");
        sleep(1000); //wait for servos to respond




        //align w/ chambers
        forward(2, normalPower);
        left(4.5, normalPower);



        sleep(300);

        //Lift arm
        robot.verticalMotor.setTargetPosition(6100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);
        sleep(1800);

        //Drive to the submersible until sensor is pressed
        robot.fl.setPower(normalPower);
        robot.fr.setPower(normalPower);
        robot.bl.setPower(normalPower);
        robot.br.setPower(normalPower);
        //Add telemetry
        while(!robot.frontSensor.isPressed() && opModeIsActive()){
            telemetry.addLine("Not Pressed");
            telemetry.update();
        }

        //back up a little
        robot.fl.setPower(-normalPower);
        robot.fr.setPower(-normalPower);
        robot.bl.setPower(-normalPower);
        robot.br.setPower(-normalPower);
        sleep(20);
        brakeAndReset();



        //arm down
        robot.verticalMotor.setTargetPosition(-100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);

        //reverse a half inch
        reverse(0.5, normalPower);

        //let the arm go down a little
        sleep(800);



        //open the claw
        robot.openClaw();
        telemetry.addLine("Red and Blue open");

        //back up a bit
        robot.fl.setPower(-normalPower);
        robot.fr.setPower(-normalPower);
        robot.bl.setPower(-normalPower);
        robot.br.setPower(-normalPower);
        sleep(300);
        brakeAndReset();

        //wait until Vertical Slide bottoms out
        ////while(!robot.verticalLimiter.isPressed());



        //Lower arm to reset
        robot.verticalMotor.setTargetPosition(-100);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);


        //go right 39 and whip a 180 WHOOOOHOOOO
        right(39, normalPower);
        rotate180();

        //move arm to grab spec
        robot.verticalMotor.setTargetPosition(2760);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);

        //Go forward into wall
        telemetry.addLine("Going forward");
        telemetry.update();
        robot.fl.setPower(normalPower);
        robot.fr.setPower(normalPower);
        robot.bl.setPower(normalPower);
        robot.br.setPower(normalPower);
        sleep(2000);
        brakeAndReset();

        sleep(500);

        //close claw
        robot.closeClaw();

        telemetry.addData("Arm pos: ", robot.verticalMotor.getCurrentPosition());
        telemetry.update();

        sleep(500);
        //back up a bit
        robot.fl.setPower(-normalPower);
        robot.fr.setPower(-normalPower);
        robot.bl.setPower(-normalPower);
        robot.br.setPower(-normalPower);
        sleep(300);
        brakeAndReset();

        sleep(2000);

        //arm up a little
        robot.verticalMotor.setTargetPosition(3500);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);
    }


    private void writeTelemetry(){
        telemetry.addData("Runtime", runtime.toString());
        telemetry.addLine();
        telemetry.addData("posX", robot.odometer.getX());
        telemetry.addData("posY", robot.odometer.getY());
        telemetry.addData("posZ", robot.odometer.getZ());
        telemetry.addLine();
        telemetry.update();
    }


    private void brakeAndReset(){
        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
        robot.odometer.resetTo(0,0,0);
    }

    //Rotate def
    private void rotate180(){
        robot.odometer.resetTo(0,0,0);
        while(robot.odometer.getZ() > -3.07 && opModeIsActive()){
            robot.fl.setPower(normalPower);
            robot.fr.setPower(-normalPower);
            robot.bl.setPower(normalPower);
            robot.br.setPower(-normalPower);
        }
        brakeAndReset();
        //swap direction (for other stuff)
        swapDirection = !swapDirection;
    }


    //Directions may be incorrect for Odom pods (fix in Config)
    private void forward(double distance, double rawPower){
        double power = rawPower;
        if(swapDirection){
            power = -power;
        }
        while(-robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going forward:", distance);
            telemetry.addData("Current Position:", -robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
        }
        brakeAndReset();
    }

    //Fixed
    private void reverse(double distance, double rawPower){
        double power = rawPower;
        if(swapDirection){
            power = -power;
        }
        while(robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going reverse:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(-power);
            robot.fr.setPower(-power);
            robot.bl.setPower(-power);
            robot.br.setPower(-power);
        }
        brakeAndReset();
    }

    private void right(double distance, double rawPower){
        double power = rawPower;
        if(swapDirection){
            power = -power;
        }
        while(robot.odometer.getX() < distance && opModeIsActive()){
            telemetry.addData("Going right:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(power);
            robot.fr.setPower(-power);
            robot.bl.setPower(-power);
            robot.br.setPower(power);
        }
        brakeAndReset();
    }

    private void left(double distance, double rawPower){
        double power = rawPower;
        if(swapDirection){
            power = -power;
        }
        while(-robot.odometer.getX() < distance && opModeIsActive()){
            telemetry.addData("Going left:", distance);
            telemetry.addData("Current Position:", -robot.odometer.getY());
            telemetry.update();
            robot.fl.setPower(-power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
        }
        brakeAndReset();
    }

    private void move(double distance, double speed){

    }


}
