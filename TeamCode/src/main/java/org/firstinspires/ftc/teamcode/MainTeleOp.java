package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@SuppressWarnings("static-access")
@TeleOp(name="Main TeleOp")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode(){
        Config robot = new Config(this);
        robot.init();

        // used for toggle buttons
        boolean debounce = false;
        boolean clawClosed = false;
        boolean slideMacroActive = false;
        boolean intakeOverride = false;
        boolean intakeTransferMode = false;

        // classes from Config
        Config.RobotClaw claw = robot.claw;
        Config.RobotLift lift = robot.lift;
        Config.RobotSlide slide = robot.slide;
        Config.RobotBox box = robot.box;
        Config.RobotWrist wrist = robot.wrist;
        Config.RobotIntake intake = robot.intake;

        // lift hold in position
        int positionToHold = -99;

        telemetry.addLine("Initialized");
        telemetry.update();


        waitForStart();


        while(opModeIsActive()){

            /// Driver One Controls
            double axialControl = -gamepad1.left_stick_y;  // y axis
            double lateralControl = gamepad1.left_stick_x; // x axis
            double yawControl = gamepad1.right_stick_x;    // z axis
            double mainThrottle = .2+(gamepad1.right_trigger*0.8); // throttle
            boolean resetFCD = gamepad1.dpad_up; // z axis reset

            // FCD reset
            if(resetFCD){
                robot.odometer.resetTo(0,0,0);
            }

            // drive train start
            // calculate power for field centric movement
            double gamepadRadians = Math.atan2(lateralControl, axialControl);
            double gamepadHypot = Range.clip(Math.hypot(lateralControl, axialControl), 0, 1);
            double robotRadians = -robot.odometer.getZ();
            double targetRadians = gamepadRadians + robotRadians;
            double lateral = Math.sin(targetRadians)*gamepadHypot;
            double axial = Math.cos(targetRadians)*gamepadHypot;

            double leftFrontPower = axial + lateral + yawControl;
            double rightFrontPower = axial - lateral - yawControl;
            double leftBackPower = axial - lateral + yawControl;
            double rightBackPower = axial + lateral - yawControl;

            // send calculated power to wheels
            robot.fl.setPower(leftFrontPower * mainThrottle);
            robot.fr.setPower(rightFrontPower * mainThrottle);
            robot.bl.setPower(leftBackPower * mainThrottle);
            robot.br.setPower(rightBackPower * mainThrottle);

            // display robot position on field
            String pos = Math.round(robot.odometer.getX()) + ", " + Math.round(robot.odometer.getY()) + ", " + Math.round(Math.toDegrees(robot.odometer.getZ()));
            telemetry.addLine(pos);


            /// Driver Two Controls
            double slideInput = gamepad2.right_stick_x;
            double liftInput = -gamepad2.left_stick_y;
            boolean boxInput = gamepad2.left_trigger > .25;
            boolean clawToggleButton = gamepad2.a;

            boolean intakeOutInput = gamepad2.right_bumper;
            boolean intakeInInput = gamepad2.right_trigger > .25;
            boolean intakeOverrideInput = gamepad2.y;
            boolean intakeTransferInput = gamepad2.x;

            boolean aboveChamberInput = gamepad2.dpad_up;
            boolean belowChamberInput = gamepad2.dpad_right || gamepad2.dpad_left;
            boolean wallInput = gamepad2.dpad_down;

            /// Toggle Button Logic
            // claw toggle
            if(clawToggleButton && !debounce){
                debounce = true; //Debounce for toggle function
                clawClosed = !clawClosed; //Toggle claw
            }
            // intake override
            if(intakeOverrideInput && !debounce){
                intakeOverride = !intakeOverride;
                debounce = true; //Debounce for toggle function
            }
            // intake transfer
            if(intakeTransferInput && !debounce){
                intakeTransferMode = !intakeTransferMode;
                debounce = true;
            }
            // reset debounce for toggle functionality
            if(!clawToggleButton && !intakeOverrideInput && !intakeTransferMode && debounce){
                debounce = false;
            }

            /// Claw
            if(clawClosed){
                claw.close();
                telemetry.addLine("Claw: Closed");
            }
            else{
                claw.open();
                telemetry.addLine("Claw: Open");
            }

            /// Slide
            // set ZPB based on slide position
            if(slide.getPosition() <= robot.slideBrakePos){ // in braking zone
                slide.setZPB(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            else{ // non braking zone
                slide.setZPB(DcMotor.ZeroPowerBehavior.FLOAT);
            }

            // slide fully retracted
            if(robot.slideSensor.isPressed()){
                telemetry.addLine("Slide Fully Retracted!");
                if(slide.getPosition() != 0){
                    slide.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
                if(slideInput >= 0){ // positive controller input
                    slide.setPower(slideInput);
                }
            }
            // slide fully extended
            else if(slide.getPosition() >= robot.slideMaxPos){
                telemetry.addLine("Slide Fully Extended!");
                if(slideInput <= 0){ // negative controller input
                    slide.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    slide.setPower(slideInput);
                }
            }
            //Slide gray zone
            else{
                telemetry.addLine("Slide Gray Zone.");
                slide.setPower(slideInput);
            }


            ///Intake Start
            boolean intakeAutoOn = false;
            if(intakeOverride){ // override mode
                wrist.up();
            }
            else if(slide.getPosition() >= robot.intakeOnPos){ // auto collect mode
                intakeAutoOn = true;
                wrist.collect();
                intake.in();
            }
            else if(intakeTransferMode || slide.getPosition() <= 50){ // transfer
                wrist.transfer();
            }
            else{ // gray zone
                wrist.up();
            }

            if(intakeInInput){ // controller intake forward
                intake.in();
            }
            else if(intakeOutInput){ // controller intake reverse
                intake.out();
            }
            else if(!intakeAutoOn){ // turn off intake
                intake.stop();
            }


            /// Lift
            // lift macros
            if(aboveChamberInput){
                slideMacroActive = true;
                lift.aboveChamber();
            }
            if(belowChamberInput){
                slideMacroActive = true;
                lift.belowChamber();
            }
            if(wallInput){
                slideMacroActive = true;
                lift.wall();
            }

            // lift bottomed out
            if(robot.liftBottomSensor.isPressed()){
                if(lift.getPosition() != 0){
                    lift.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
                if(liftInput < 0){ // don't allow lift to be lowered further
                    liftInput = 0;
                    lift.setPower(0);
                }
                telemetry.addLine("Lift Bottomed Out!");
            }

            // lift topped out
            else if(lift.getPosition() >= robot.liftLimitPos){
                if(liftInput > 0){ // don't allow lift to be raised further
                    liftInput = 0;
                }
                telemetry.addLine("Slide Topped Out!");
            }

            // applying input to lift
            if(liftInput != 0){
                slideMacroActive = false; // stop lift macros
                lift.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if(liftInput > 0){ // raising the lift
                    lift.setPower(liftInput);
                }
                else{ // lowering the lift
                    lift.setPower(liftInput/2);
                }
                positionToHold = lift.getPosition(); // used to hold when no input
            }


            // no input for lift and macro isn't running (Stop and Hold)
            if(liftInput == 0 && !slideMacroActive && !robot.liftBottomSensor.isPressed()){
                if(positionToHold == -99){positionToHold = lift.getPosition();}

                lift.runToPosition(positionToHold,0.5);
                telemetry.addLine("Slide Holding.");
            }


            /// Box Control
            if(boxInput){
                box.dump();
            }
            else{
                box.transfer();
            }

            // update telemetry
            telemetry.update();

        }
    }
}
