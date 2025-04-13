package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

    // make classes available to MainAuto
    ExampleAuto exampleAuto;
    ClipThreeParkSometimes clipThreeParkSometimes;
    ProgramSelector ps;
    ProgramRunner pr;

    @Override
    public void runOpMode(){
        robot = new Config(this);

        ps = new ProgramSelector();
        ps.run();

        waitForStart();

        pr = new ProgramRunner();
        pr.run(ps.selectedAuto);

        telemetry.addLine("Program completed.");
        telemetry.update();

    }

    /// Class containing a program selector.
    public class ProgramSelector {
        int selectedAuto;

        /// Runs the program selector.
        public void run(){
            autoPrograms = new HashMap<>();
            autoPrograms.put(0,"ExampleAuto");
            exampleAuto = new ExampleAuto();
            autoPrograms.put(1,"ClipThreeParkSometimes");
            clipThreeParkSometimes = new ClipThreeParkSometimes();

            boolean debounce = false;
            boolean runInitialization = false;
            selectedAuto = 0;
            while(opModeInInit()){
                telemetry.addLine("Use 'A' or 'Cross' to select an auto.");
                // incrementing of programs
                if(gamepad1.a || gamepad2.a) {
                    debounce = true;
                    selectedAuto++;
                }
                // locking in program and starting initialization phase
                if(!gamepad1.a && !gamepad2.a && !gamepad1.y && !gamepad2.y && debounce){
                    runInitialization = true;
                    break;
                }
                // reset back to zero
                if(selectedAuto > autoPrograms.size()){
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
            if(runInitialization){
                pr.init(selectedAuto);
            }
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
            boolean notFail = true;
            telemetry.addLine("Attempting to init auto program: '"
                    + autoPrograms.get(selectedAuto) + "'");
            telemetry.update();
            switch (selectedAuto) {
                case 0:
                    exampleAuto.init();
                    break;
                case 1:
                    clipThreeParkSometimes.init();
                    break;
                default:
                    notFail = false;
                    telemetry.addLine("Failed to find autonomous program: '"
                            + autoPrograms.get(selectedAuto) + "'");
                    telemetry.update();
            }
            if(notFail){
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
                    exampleAuto.run();
                    break;
                case 1:
                    clipThreeParkSometimes.run();
                    break;
                default:
                    telemetry.addLine("Failed to find autonomous program: '"
                            + autoPrograms.get(selectedAuto) + "'");
                    telemetry.update();
            }
        }

    }

    /// Example auto structure.
    public class ExampleAuto {
        public void init(){
            telemetry.addLine("exampleAuto init called.");
            telemetry.update();
        }
        public void run(){
            telemetry.addLine("exampleAuto run called.");
            telemetry.update();
        }

    }

    /// Old auto program transferred here for reference.
    public class ClipThreeParkSometimes{
        Config.RobotClaw claw = robot.claw;
        Config.RobotLift lift = robot.lift;
        Config.RobotSlide slide = robot.slide;
        Config.RobotBox box = robot.box;

        public void init(){
            robot.init();
            robot.initTweetyBird();

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

}
