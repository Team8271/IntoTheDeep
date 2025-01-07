package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.HashMap;

@Autonomous (name="Jax Auto") //add preselectTeleOp="Jax TeleOp" to turn preselect on
public class Auto extends LinearOpMode {
    private Configuration robot;

    boolean swapDirection = false;




    double lowPower = 0.2;
    double normalPower = 0.4;
    double highPower = 0.6;
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot = new Configuration(this);
        robot.init(true);
        PIDControl thread1 = new PIDControl(this);


        //Wait for driver to press START
        waitForStart();

        thread1.targetPosition = 0;
        thread1.start();
        telemetry.addLine("PID Started");
        telemetry.update();

        while (opModeIsActive()){
            if (gamepad1.dpad_up){ //Go to above high chamber
                thread1.targetPosition = 4761;
                telemetry.addLine("Setting target position to 4761");
                telemetry.update();
            }
            if(gamepad1.dpad_down){ //GO to down
                thread1.targetPosition = 0;
            }
        }




        //tweetyAuto();
    }



    private void tweetyAuto(){
        robot.initTweatyBird();
        robot.tweetyBird.sendTargetPosition(5, 30, 0);
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.close();
    }

    //Main auto
    //Vertical Slide and Claw things have been commented out for testing while robot is disabled
    private void mainAuto(int delayInSeconds, int startPosition){
        robot.closeClaw(); //grab preloaded specimen
        sleep(1000 + (delayInSeconds * 1000));//Adjustable delay
        alignWithSubmersible(startPosition);//align with submersible
        setVerticalPosition(robot.vertAboveChamber);//Start moving Vertical Slide to high position
        sleep(2000);//Allow vertical slide to clear high chamber before moving
        driveForwardUntilBlocked(normalPower);//Move forward until Touch Sensor is pressed/odom stops changing
        clipSpecimen();//Clip the Specimen on the high chamber
        sleep(2000); //REMOVE ME, JUST FOR TESTING
        reverse(5, normalPower); //clear the submersible
        right(39, normalPower); //go to observation
        rotate180(); //face the human element
        driveForwardUntilBlocked(lowPower);//Slowly move into the wall
        //grabSpecimenFromWall();



        setVerticalPosition(robot.vertWall);//Start moving vert into position
        driveForwardUntilBlocked(normalPower); //Drive into wall
        robot.closeClaw();
    }

    //Work in Progress (Don't use)
    private void telemetrySelector(){
        HashMap<Integer, String> startingPositions = new HashMap<Integer, String>();
        startingPositions.put(0, "Centered with Submersible");
        startingPositions.put(1, "Left of Center Line");
        startingPositions.put(2, "Right of Center Line");
        int selection = 0;
        boolean confirm = false;

        //Starting Position Selector
        while(!confirm){
            telemetry.addLine("Select Starting Position (Dpad Up/Down and A to confirm)");
            if(gamepad1.dpad_up || gamepad2.dpad_up && selection<=2){
                selection++;
                sleep(1000);
            }
            if(gamepad1.dpad_down || gamepad2.dpad_down && selection>=0){
                selection--;
                sleep(1000);
            }
            if(gamepad1.a || gamepad2.a){
                confirm = true;
            }
            telemetry.addLine();
            telemetry.addData("Starting Position", startingPositions.get(selection));
            telemetry.update();
        }
        int startPos = selection;

        //Start Delay Selector
        confirm = false;
        selection = 0;
        while(!confirm){
            telemetry.addLine("Choose Start Delay (Dpad Up/Down and A to confirm)");
            if(gamepad1.dpad_up || gamepad2.dpad_up && selection<=30){
                selection++;
                sleep(500);
            }
            if(gamepad1.dpad_down || gamepad2.dpad_down && selection>=0){
                selection--;
                sleep(500);
            }
            if(gamepad1.a || gamepad2.a){
                confirm = true;
            }
            telemetry.addLine();
            telemetry.addData("Seconds to delay", selection);
            telemetry.update();
        }
        int startDelay = selection;

        telemetry.clearAll();
        telemetry.addData("Starting Position", startPos);
        telemetry.addData("Delay", startDelay);
        telemetry.update();

    }

    //Clip Specimen on the High Chamber (Vertical slide needs to be vertAboveChamber!)
    public void clipSpecimen(){
        if(robot.verticalMotor.getCurrentPosition() < robot.vertAboveChamber-200){//If not above chamber
            telemetry.addLine("Vertical Slide position is incorrect");//calm
            telemetry.addLine("EXITING SPECIMEN CLIP!");//panik
            telemetry.addLine("SCREAMING NO NO NO NO NO NO NO NO NO"); //panik
            telemetry.update();
        }
        else {
            reverse(0.5, lowPower);//Move back a little
            robot.verticalMotor.setTargetPosition(robot.vertBelowChamber);//set target below chamber
            robot.verticalMotor.setPower(0.4);//Set power
            robot.openClaw();//release the specimen
        }
    }

    //Align with the submersible from starting position
    public void alignWithSubmersible(int startPos){
        brakeAndReset();
        if(startPos == 1){
            forward(2, normalPower);
            left(4.5, normalPower);
        }
        else if(startPos == 2){
            forward(2, normalPower);
            right(4.5, normalPower);
        }
    }

    //Grab Specimen off of the Wall
    public void grabSpecimenFromWall(){
        setVerticalPosition(robot.vertWall);//Start moving Vertical slide into position
        driveForwardUntilBlocked(normalPower);//Drive into wall
        robot.closeClaw();//Close the Claw
        sleep(300);//Give Claw time to close
        setVerticalPosition(robot.vertWall+200);//Lift slide so Specimen clears wall
    }

    //!!! This is being moved to Configuration as a PID motor control
    public void setVerticalPosition(int position){
        robot.verticalMotor.setTargetPosition(position);
        robot.verticalMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.verticalMotor.setPower(1);
    }

    //Drive forward until button is pressed or odom change is little
    public void driveForwardUntilBlocked(double power){
        brakeAndReset();
        telemetry.addLine("Starting driveForwardUntilBlocked!");
        telemetry.update();
        while (true){
            double lastPos = -robot.odometer.getY();
            robot.fr.setPower(power);
            robot.fl.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
            sleep(300);
            telemetry.addData("Last pos:", lastPos);
            telemetry.addData("Current Pos", -robot.odometer.getY());
            telemetry.update();
            if(lastPos+0.25 > -robot.odometer.getY() || robot.frontSensor.isPressed()){
                brakeAndReset();
                telemetry.clearAll();
                telemetry.addLine("Stopping!");
                telemetry.update();
                break;
            }
        }
    }

    //Auto used at first qualifier in Butte
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
        robot.fr.setPower(normalPower);
        robot.fl.setPower(normalPower);
        robot.bl.setPower(normalPower);
        robot.br.setPower(normalPower);
        //Add telemetry
        while(!robot.frontSensor.isPressed() && opModeIsActive()){
            telemetry.addLine("Not Pressed");
            telemetry.update();
        }

        //back up a little
        robot.fr.setPower(-normalPower);
        robot.fl.setPower(-normalPower);
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
        robot.fr.setPower(-normalPower);
        robot.fl.setPower(-normalPower);
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
        robot.fr.setPower(normalPower);
        robot.fl.setPower(normalPower);
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
        robot.fr.setPower(-normalPower);
        robot.fl.setPower(-normalPower);
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

    private void brakeAndReset(){
        robot.fr.setPower(0);
        robot.fl.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
        robot.odometer.resetTo(0,0,0);
    }

    //Rotate def
    private void rotate180(){
        robot.odometer.resetTo(0,0,0);
        while(robot.odometer.getZ() > -3.07 && opModeIsActive()){
            robot.fr.setPower(normalPower);
            robot.fl.setPower(-normalPower);
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
            robot.fr.setPower(power);
            robot.fl.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
        }
        brakeAndReset();
    }

    private void reverse(double distance, double rawPower){
        double power = rawPower;
        if(swapDirection){
            power = -power;
        }
        while(robot.odometer.getY() < distance && opModeIsActive()){
            telemetry.addData("Going reverse:", distance);
            telemetry.addData("Current Position:", robot.odometer.getY());
            telemetry.update();
            robot.fr.setPower(-power);
            robot.fl.setPower(-power);
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
            robot.fr.setPower(power);
            robot.fl.setPower(-power);
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
            robot.fr.setPower(-power);
            robot.fl.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(-power);
        }
        brakeAndReset();
    }


}
