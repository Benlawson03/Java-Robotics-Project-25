
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.hardware.port.MotorPort;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.WheeledChassis;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.localization.OdometryPoseProvider;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import java.lang.Math;

public class Driver {
	final static float WHEEL_DIAMETER = 56; // The diameter (mm) of the wheels
    final static float AXLE_LENGTH = 120; // The distance (mm) your two driven wheels
    final static float ANGULAR_SPEED = 100; // How fast around corners (degrees/sec)
    final static float LINEAR_SPEED = 300; // How fast in a straight line (cm/sec)
    final static long POS_INTERVALS = Math.round(LINEAR_SPEED/((WHEEL_DIAMETER*Math.PI/1000)/2)); //interval checking for movement every half wheel turn (ms)
    final static float SQUARE_SIDE = 1000; //The size (mm) of the square
    final static float RESOLUTION = 1; //The error margin (mm) for repeating until desired distance reached
    
    final static int MAP_WIDTH = 25;
    final static int MAP_HEIGHT = 25;
    private static int[][] map = new int[MAP_WIDTH][MAP_HEIGHT];
    
    public static void main(String[] args) {
    	//initialising move pilot
    	BaseRegulatedMotor mL = new EV3LargeRegulatedMotor(MotorPort.A);
        BaseRegulatedMotor mR = new EV3LargeRegulatedMotor(MotorPort.B);
        Wheel wLeft = WheeledChassis.modelWheel(mL, WHEEL_DIAMETER).offset(-AXLE_LENGTH / 2);
        Wheel wRight = WheeledChassis.modelWheel(mR, WHEEL_DIAMETER).offset(AXLE_LENGTH / 2);
        Chassis chassis = new WheeledChassis((new Wheel[] {wRight, wLeft}), WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
        pilot.setLinearSpeed(LINEAR_SPEED);
        pilot.setAngularSpeed(ANGULAR_SPEED);
        
        //initialising pose provider
        PoseProvider poseProvider = new OdometryPoseProvider(pilot);
        poseProvider.setPose(new Pose()); //sets pose to origin, facing north
    	
        //initialising sensors
        EV3ColorSensor cs = null;
        EV3UltrasonicSensor us = null;
        try {
        	cs = new EV3ColorSensor(SensorPort.S2);
        	us = new EV3UltrasonicSensor(SensorPort.S3);
        } catch (Exception e) {
        	LCD.clear();
        	LCD.drawString("Error in intialising sensors", 0, 0);
        	System.exit(1);
        }
        	
        
        LCD.drawString("Press Enter", 0, 0);
        Button.ENTER.waitForPressAndRelease();
        
        //calibrating light sensor
        CalibrateCS calibration = new CalibrateCS(pilot, poseProvider, cs);
        float[] lightValues = new float[] {calibration.getRedMin(), calibration.getRedMax(), calibration.getGreenMin(), calibration.getGreenMax()};
        
        //initialising map
        for (int i=0;i<MAP_HEIGHT;i++) {
        	for (int j=0;j<MAP_WIDTH;j++) {
        		map[i][j] = 0;
        	}
        }
        
        //starting the constant updating of the map in a new thread
        MapUpdate updater = new MapUpdate(poseProvider, map, POS_INTERVALS);
        updater.start();
        
        //creating behaviours
        Behavior[] behaviors = {
        		new Trundling(pilot),
        		new DoorColour(pilot, poseProvider, us, cs, lightValues),
        		new BatteryLevels(),
        		new EmergencyStops(pilot)
        };
        
        //initialising the arbitrator
        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.go();
      
        cs.close();
        us.close();
    }
}
