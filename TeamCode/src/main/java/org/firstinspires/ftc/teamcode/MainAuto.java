package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;

// How to add another auto program:
// create new class ie. 'MyAutoProgram'
// create init and run methods within
// make class available to parent class
//  ie MyAutoProgram myAutoProgram;
// add program into autoPrograms hashmap
//  go to ProgramSelector.run
// add a case statement in ProgramRunner.init and
//  ProgramRunner.run

/// Class that contains all auto programs.
@Autonomous(name = "Main Autonomous")
public class MainAuto extends LinearOpMode {
    Config robot;
    HashMap<Integer,String> autoPrograms;
    private static final Logger log = LoggerFactory.getLogger(MainAuto.class);
    protected BufferedWriter logWriter = null;

    // make classes available to MainAuto
    RoadRunnerAuto rRAuto;
    TweetyAutoWheels tweetyAutoWheels;
    TweetyAuto tweetyAuto;
    ProgramSelector ps;
    ProgramRunner pr;

    // components (must init them before use)
    Config.RobotClaw claw;
    Config.RobotClaw.RoadRunner rRClaw;
    Config.RobotSlide slide;
    Config.RobotBox box;
    Config.RobotBox.RoadRunner rRBox;
    Config.RobotLift lift;
    Config.RobotLift.RoadRunner rRLift;

    @Override
    public void runOpMode(){
        robot = new Config(this);
        ElapsedTime programRunTime = new ElapsedTime();

        autoPrograms = new HashMap<>();
        autoPrograms.put(0,"RoadRunnerAuto");
        rRAuto = new RoadRunnerAuto();
        autoPrograms.put(1,"ClipThreeParkSometimesJustDrive");
        tweetyAutoWheels = new TweetyAutoWheels();
        autoPrograms.put(2,"ClipThreeParkSometimes");
        tweetyAuto = new TweetyAuto();

        pr = new ProgramRunner();

        pr.init(1);

        waitForStart();

        pr.run(1);

//        // log stuff
//        String logFileName = "AutoLogs.txt";
//        File logFile;
//        File logDirectory = Environment.getExternalStorageDirectory();
//        logFile = new File(logDirectory, logFileName);
//        try {
//            logWriter = new BufferedWriter(new FileWriter(logFile, true));
//        } catch (IOException e) {
//            log("Failed to initialize to logWriter " +e);
//        }
//        log("Initial setup complete!\n");
//
//
//        pr = new ProgramRunner();
//        ps = new ProgramSelector();
//        ps.run();
//
//        waitForStart();
//        log("START pressed");
//
//        programRunTime.reset();
//        pr.run(ps.selectedAuto);
//
//        // log
//        log("Program completed.\nProgram Name: " + autoPrograms.get(ps.selectedAuto) +
//                "Program RunTime: " + programRunTime);
    }

    /// Class containing a program selector.
    public class ProgramSelector {
        int selectedAuto = 0;

        /// Runs the program selector.
        public void run(){
            log("Starting Program Selector");
            ElapsedTime programSelectorRuntime = new ElapsedTime();
            autoPrograms = new HashMap<>();
            autoPrograms.put(0,"RoadRunnerAuto");
            rRAuto = new RoadRunnerAuto();
            autoPrograms.put(1,"ClipThreeParkSometimesJustDrive");
            tweetyAutoWheels = new TweetyAutoWheels();
            autoPrograms.put(2,"ClipThreeParkSometimes");
            tweetyAuto = new TweetyAuto();

            boolean debounce = false;
            boolean runInitialization = false;
            selectedAuto = 0;
            while(opModeInInit()){
                telemetry.addLine("Program Selector RunTime: " + programSelectorRuntime.seconds());
                telemetry.addLine("Use 'A' or 'Cross' to select an auto.");
                telemetry.addLine("Use 'Y' or 'Triangle' to initialize that auto early.");
                // incrementing of programs
                if((gamepad1.a || gamepad2.a) && !debounce) {
                    debounce = true;
                    selectedAuto++;
                }
                else if((gamepad1.y || gamepad2.y) && !debounce) {
                    while(opModeInInit()) {
                        telemetry.addLine("Program Selector RunTime: " + programSelectorRuntime.seconds());
                        telemetry.addLine("Press 'X' or 'Square' to confirm early init " + autoPrograms.get(selectedAuto));
                        telemetry.addLine("Press 'B' or 'Circle' to cancel.");
                        if(gamepad1.x || gamepad2.x) {
                            runInitialization = true;
                            break;
                        }
                        if(gamepad1.b || gamepad2.b) {
                            break;
                        }
                    }
                    if(runInitialization){
                        break;
                    }

                }
                // locking in program and starting initialization phase
                else if(!gamepad1.a && !gamepad2.a && !gamepad1.y && !gamepad2.y && debounce){
                    debounce = false;
                }
                // reset back to zero
                if(selectedAuto > autoPrograms.size()-1){
                    selectedAuto = 0;
                }
                for(Integer key : autoPrograms.keySet()){
                    if(key == selectedAuto){
                        telemetry.addLine("    -> " + autoPrograms.get(key));
                    }
                    else{
                        telemetry.addLine("       " + autoPrograms.get(key));
                    }
                }
                telemetry.update();
            }

            pr.init(selectedAuto);
            telemetry.addLine(autoPrograms.get(selectedAuto) + " initialized!");

            log("Program Selector Closing");
            telemetry.addLine("Auto Program Selector closing...");
            telemetry.update();
        }

    }

    /// Class containing a program runner/initializer.
    public class ProgramRunner{

        /** Initializes the program associated with input in autoPrograms hashmap.
         *
         * @param selectedAuto key value for auto program.
         */
        public void init(int selectedAuto){
            boolean validAuto = true;
            telemetry.addLine("Attempting to init auto program: '"
                    + autoPrograms.get(selectedAuto) + "'");
            telemetry.update();
            log("Attempting to init auto program: " + autoPrograms.get(selectedAuto));
            switch (selectedAuto) {
                case 0:
                    rRAuto.init();
                    break;
                case 1:
                    tweetyAutoWheels.init();
                    break;
                case 2:
                    tweetyAuto.init();
                    break;
                default:
                    validAuto = false;
                    telemetry.addLine("Failed to find autonomous program: '"
                            + autoPrograms.get(selectedAuto) + "'");
                    telemetry.update();
            }
            if(validAuto){
                telemetry.addLine("'" + autoPrograms.get(selectedAuto)
                        + "' finished initialization!");
                telemetry.update();
            }
        }
        /** Runs the program associated with input in autoPrograms hashmap.
         *
         * @param selectedAuto key value for auto program.
         */
        public void run(int selectedAuto){
            telemetry.addLine("Attempting to run auto program: '"
                    + autoPrograms.get(selectedAuto) + "'");
            telemetry.update();
            switch (selectedAuto){
                case 0:
                    rRAuto.run();
                    break;
                case 1:
                    tweetyAutoWheels.run();
                    break;
                case 2:
                    tweetyAuto.run();
                default:
                    telemetry.addLine("Failed to find autonomous program: '"
                            + autoPrograms.get(selectedAuto) + "'");
                    telemetry.update();
            }
        }

    }

    /// Old auto program transferred here.
    public class TweetyAuto {


        public void init(){
            robot.init();
            robot.initTweetyBird();

            Config.RobotClaw claw = robot.claw;
            Config.RobotLift lift = robot.lift;
            Config.RobotSlide slide = robot.slide;
            Config.RobotBox box = robot.box;

            slide.setZPB(DcMotor.ZeroPowerBehavior.BRAKE);

            claw.close();
        }
        public void run(){
            box.store();
            lift.aboveChamber();

            /// Clip preload
            robot.tweetyBird.engage();
            moveTo(-6,29,0);
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();

            allWheelPower(0.2,700); // move into submersible

            lift.wall(); // clip specimen
            sleep(1000);
            claw.open();


            /// Pushing samples into observation
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            waitForMove();
            moveTo(-6,18,0); // move back from submersible

            moveTo(28,18,-180); // move to left/below sample 1
            waitForMove();

            moveTo(29,42,-180); // move to left/above sample 1
            waitForMove();

            moveTo(40,42,-180); // move to above sample 1
            waitForMove();

            moveTo(37,11,-180); // push sample 1 into observation
            waitForMove();

            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            claw.close(); // grab 2nd specimen
            lift.aboveWall();

            clipCycle(0); // 2nd
            //clipCycle(-2); // 3rd
            //clipCycle(-4); // 4th
            //clipCycle(-6); // 5th

            // in observation with specimen
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            telemetry.addLine(robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ());
            telemetry.update();

            moveTo(34,15,-180); // exit observation zone
            waitForMove();
            lift.aboveChamber();
            moveTo(-10,15,0); // move to submersible
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            lift.belowChamber();
            sleep(1000);
            claw.open();

            // robot is against submersible with specimen clipped and claw open
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            moveTo(-10,25,0); // get off of submersible
            waitForMove();
            lift.wall();
            moveTo(33,0,0); // to observation with no rotation
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();

            robot.tweetyBird.close();
        }
        // start in position after grabbing clip
        private void clipCycle(double offset){
            // sitting in observation and grabbed specimen
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            telemetry.addLine(robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ());
            telemetry.update();

            moveTo(34,15,-180); // back out of observation
            waitForMove();
            lift.aboveChamber();
            moveTo(-8+offset,15,0); // move to submersible
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            lift.belowChamber();
            sleep(1000);
            claw.open();


            // robot is sitting against submersible with specimen clipped and claw open
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            moveTo(-8,25,0); //Back off of submersible
            waitForMove();
            lift.wall();
            moveTo(33,0,-180); //Move to observation with rotation
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            claw.close(); //Grab specimen
            lift.aboveWall();
            sleep(500);
        }
        private void moveTo(double x, double y, double z){
            robot.tweetyBird.addWaypoint(x,y,z);
        }
        private void waitForMove(){
            robot.tweetyBird.waitWhileBusy();
        }
        private void allWheelPower(double speed, int milliseconds){
            setAllWheelPower(speed);
            sleep(milliseconds);
            setAllWheelPower(0);
        }
        private void setAllWheelPower(double power){
            robot.fl.setPower(power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
        }

    }

    /// Just runs the four drive motors, otherwise same as TweetyAuto
    public class TweetyAutoWheels {
        public void init(){
            robot.init();
            robot.initTweetyBird();

            Config.RobotClaw claw = robot.claw;
            Config.RobotLift lift = robot.lift;
            Config.RobotSlide slide = robot.slide;
            Config.RobotBox box = robot.box;

            slide.setZPB(DcMotor.ZeroPowerBehavior.BRAKE);

            //claw.close();
        }
        public void run(){
            //box.store();
            //lift.aboveChamber();

            /// Clip preload
            robot.tweetyBird.engage();
            moveTo(-6,29,0);
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();

            allWheelPower(0.2,700); // move into submersible

            //lift.wall(); // clip specimen
            sleep(1000);
            //claw.open();


            /// Pushing samples into observation
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            waitForMove();
            moveTo(-6,18,0); // move back from submersible

            moveTo(28,18,-180); // move to left/below sample 1
            waitForMove();

            moveTo(29,42,-180); // move to left/above sample 1
            waitForMove();

            moveTo(40,42,-180); // move to above sample 1
            waitForMove();

            moveTo(37,11,-180); // push sample 1 into observation
            waitForMove();

            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            //claw.close(); // grab 2nd specimen
            //lift.aboveWall();

            clipCycle(0); // 2nd
            //clipCycle(-2); // 3rd
            //clipCycle(-4); // 4th
            //clipCycle(-6); // 5th

            // in observation with specimen
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            telemetry.addLine(robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ());
            telemetry.update();

            moveTo(34,15,-180); // exit observation zone
            waitForMove();
            //lift.aboveChamber();
            moveTo(-10,15,0); // move to submersible
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            //lift.belowChamber();
            sleep(1000);
            //claw.open();

            // robot is against submersible with specimen clipped and claw open
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            moveTo(-10,25,0); // get off of submersible
            waitForMove();
            //lift.wall();
            moveTo(33,0,0); // to observation with no rotation
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();

            robot.tweetyBird.close();
        }
        // start in position after grabbing clip
        private void clipCycle(double offset){
            // sitting in observation and grabbed specimen
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            telemetry.addLine(robot.odometer.getX() + ", " + robot.odometer.getY() + ", " + robot.odometer.getZ());
            telemetry.update();

            moveTo(34,15,-180); // back out of observation
            waitForMove();
            //lift.aboveChamber();
            moveTo(-8+offset,15,0); // move to submersible
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            //lift.belowChamber();
            sleep(1000);
            //claw.open();


            // robot is sitting against submersible with specimen clipped and claw open
            robot.tweetyBird.engage();
            robot.tweetyBird.skipWaypoint();
            moveTo(-8,25,0); //Back off of submersible
            waitForMove();
            //lift.wall();
            moveTo(33,0,-180); //Move to observation with rotation
            waitForMove();
            robot.tweetyBird.clearWaypoints();
            robot.tweetyBird.skipWaypoint();
            robot.tweetyBird.disengage();
            allWheelPower(0.4,700);
            //claw.close(); //Grab specimen
            //lift.aboveWall();
            sleep(500);
        }
        private void moveTo(double x, double y, double z){
            robot.tweetyBird.addWaypoint(x,y,z);
        }
        private void waitForMove(){
            robot.tweetyBird.waitWhileBusy();
        }
        private void allWheelPower(double speed, int milliseconds){
            setAllWheelPower(speed);
            sleep(milliseconds);
            setAllWheelPower(0);
        }
        private void setAllWheelPower(double power){
            robot.fl.setPower(power);
            robot.fr.setPower(power);
            robot.bl.setPower(power);
            robot.br.setPower(power);
        }

    }

    /// Auto program created using RoadRunner.
    public class RoadRunnerAuto{
        Pose2d initialPose;
        MecanumDrive drive;

        public void init(){
            robot.init();

            claw = robot.claw;
            rRClaw = robot.rRClaw;
            slide = robot.slide;
            box = robot.box;
            rRBox = robot.rRBox;
            lift = robot.lift;
            rRLift = robot.rRLift;

            // instantiate your MecanumDrive at a particular pose.
            initialPose = new Pose2d(8,-63,Math.toRadians(90));
            drive = new MecanumDrive(hardwareMap, initialPose);

            slide.setZPB(DcMotor.ZeroPowerBehavior.BRAKE);

            Actions.runBlocking(rRClaw.rRClose());
        }
        public void run(){
            rRBox.rRStore();
            rRLift.aboveChamber();

            TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                    .stopAndAdd(rRClaw.rRClose())
                    .stopAndAdd(rRLift.aboveChamber())
                    .splineToConstantHeading(new Vector2d(-4,-31),Math.toRadians(90))
                    .stopAndAdd(rRLift.belowChamber(true))
                    .stopAndAdd(rRClaw.rROpen())
                    .strafeToConstantHeading(new Vector2d(4,-40))
                    .stopAndAdd(rRLift.wall())
                    .strafeToConstantHeading(new Vector2d(38,-35))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(38,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(45,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(45,-58))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(48,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(54,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(54,-58))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(57,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(61,-15))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(61,-55))
                    //.turnTo(Math.toRadians(90))
                    .strafeToConstantHeading(new Vector2d(55,-55))
                    .strafeToLinearHeading(new Vector2d(48,-55),Math.toRadians(270))
                    .strafeTo(new Vector2d(48,-61))
                    .stopAndAdd(rRClaw.rRClose())
                    .stopAndAdd(rRLift.aboveWall(true))
                    .strafeTo(new Vector2d(-2,-35))
                    .stopAndAdd(rRLift.aboveChamber())
                    .turn(Math.toRadians(-180))
                    .stopAndAdd(rRLift.aboveChamber(true))
                    .strafeTo(new Vector2d(-2,-31))
                    .stopAndAdd(rRLift.belowChamber(true))
                    .stopAndAdd(rRClaw.rROpen());

            if (isStopRequested()) return;

            Actions.runBlocking(
                    new SequentialAction(
                            tab1.build()
                    )
            );

        }
    }

    protected void log(String message) {
        // get current time
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY hh:mm:ss:SSS");
        String date = sdf.format(now);

        // processing string
        String outputString = "["+date+" Auto]: "+message;

        // logFile
        if(logWriter != null) {
            try {
                logWriter.write(outputString);
                logWriter.newLine();
            } catch (IOException e) {}
        }
    }

}
