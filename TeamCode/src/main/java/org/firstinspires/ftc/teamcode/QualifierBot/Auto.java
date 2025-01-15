package org.firstinspires.ftc.teamcode.QualifierBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.StateBot.PIDControl;

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
        robot.init();
        robot.initTweatyBird();
        robot.vertMotor.setTargetPosition(robot.vertMotor.getCurrentPosition());
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(0.8);

        telemetry.addLine("Initialized");
        String position = robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ();
        telemetry.addData("Current Pos:", position);
        telemetry.update();

        //Wait for driver to press START
        waitForStart();



        helenaAuto();




        //tweetyAuto();
    }

    private void helenaAuto(){
        robot.closeClaw();
        sleep(1000); //wait for clawServos
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Start moving vertMotor up
        sleep(1000); //Wait for vertMotor
        robot.tweetyBird.sendTargetPosition(-6,29,0); //Go infront of chambers
        robot.tweetyBird.waitWhileBusy(); //Wait until robot reaches front of chambers
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip the Specimen
        sleep(1000); //Wait for vertMotor
        robot.openClaw(); //Release the Specimen
        sleep(1000); //Wait for clawServos
        //RUN VERTMOTOR DOWN UNTIL TOUCHSENSOR =TRUE
        /*robot.tweetyBird.sendTargetPosition(); //Move to clear truss of submersible
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move to the left of sample
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move to the left of sample and above
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move above the sample
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Push sample in observation
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move left and above sample
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move above the sample
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Push sample into observation
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Exit observation and align for specimen grab
        robot.vertMotor.setTargetPosition(robot.vertWall); //Raise vertMotor for wall grab
        robot.tweetyBird.waitWhileBusy();
        //wait for the vertMotor to raise
        robot.tweetyBird.sendTargetPosition(); //Go into wall for grab 
        robot.tweetyBird.waitWhileBusy();
        robot.closeClaw(); //Close the clawServos
        sleep(500); //Wait for clawServos to close
        robot.vertMotor.setTargetPosition(robot.vertWall+200);
        //wait for vertMotor to raise
        //Maybe reset with vertLimiter
        robot.tweetyBird.sendTargetPosition(); //Align with submersible
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertAboveChamber); //Raise vertMotor above chamber
        //wait for vertMotor
        robot.tweetyBird.sendTargetPosition(); //Drive to front of chambers
        robot.tweetyBird.waitWhileBusy();
        robot.vertMotor.setTargetPosition(robot.vertBelowChamber); //Clip the Specimen
        sleep(1000); //Wait for vertMotor
        robot.openClaw(); //Release the Specimen
        sleep(500); //Wait for clawServos
        //bring vertMotor to reset
        robot.tweetyBird.sendTargetPosition(); //Move to clear truss of submersible
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move to align with observation specimen
        robot.vertMotor.setTargetPosition(robot.vertWall); //Move vertMotor to align with specimen
        robot.tweetyBird.waitWhileBusy();
        robot.tweetyBird.sendTargetPosition(); //Move to grab specimen
        robot.tweetyBird.waitWhileBusy();

        */
        
        

    }



    private void tweetyAuto(){
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
        if(robot.vertMotor.getCurrentPosition() < robot.vertAboveChamber-200){//If not above chamber
            telemetry.addLine("Vertical Slide position is incorrect");//calm
            telemetry.addLine("EXITING SPECIMEN CLIP!");//panik
            telemetry.addLine("SCREAMING NO NO NO NO NO NO NO NO NO"); //panik
            telemetry.update();
        }
        else {
            reverse(0.5, lowPower);//Move back a little
            robot.vertMotor.setTargetPosition(robot.vertBelowChamber);//set target below chamber
            robot.vertMotor.setPower(0.4);//Set power
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
        robot.vertMotor.setTargetPosition(position);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);
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
        robot.vertMotor.setTargetPosition(6100);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);
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
        robot.vertMotor.setTargetPosition(-100);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);

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
        robot.vertMotor.setTargetPosition(-100);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);


        //go right 39 and whip a 180 WHOOOOHOOOO
        right(39, normalPower);
        rotate180();

        //move arm to grab spec
        robot.vertMotor.setTargetPosition(2760);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);

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

        telemetry.addData("Arm pos: ", robot.vertMotor.getCurrentPosition());
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
        robot.vertMotor.setTargetPosition(3500);
        robot.vertMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.vertMotor.setPower(1);
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
