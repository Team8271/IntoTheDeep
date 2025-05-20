package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(16,16)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(8, -63, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-4,-31),Math.toRadians(90))
                        .waitSeconds(1) // clip
                        .strafeTo(new Vector2d(10,-35))
                        .strafeTo(new Vector2d(38,-35))
                        .strafeTo(new Vector2d(38,-10))
                        .strafeTo(new Vector2d(45,-15))
                        .strafeTo(new Vector2d(45,-58))
                        .strafeTo(new Vector2d(48,-10))
                        .strafeTo(new Vector2d(54,-10))
                        .strafeTo(new Vector2d(54,-58))
                        .strafeTo(new Vector2d(57,-15))
                        .strafeTo(new Vector2d(61,-10))
                        .strafeTo(new Vector2d(61,-55))
                        .strafeTo(new Vector2d(48,-55))
                        .turn(Math.toRadians(180))
                        .strafeTo(new Vector2d(48,-61))
                        .waitSeconds(1) // grab specimen
                        .strafeTo(new Vector2d(-2,-35))
                        .turn(Math.toRadians(-180))
                        .strafeTo(new Vector2d(-2,-31))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}