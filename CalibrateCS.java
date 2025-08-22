import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class CalibrateCS{
	private MovePilot pilot;
	private PoseProvider poseProvider;
	private SampleProvider light;
	private float greenMin;
	private float greenMax;
	private float redMin;
	private float redMax;

	public CalibrateCS(MovePilot pilot, PoseProvider poseProvider, EV3ColorSensor sensor) {
		this.pilot = pilot;
		this.poseProvider = poseProvider;
		this.light = sensor.getRGBMode();
		this.greenMin = 1;
		this.greenMax = 0;
		this.redMin = 1;
		this.redMax = 0;
		
		LCD.drawString("Calibrating colour sensor...", 0, 0);
		calibrate();
		LCD.drawString("Calibrated!", 0, 0);
		Delay.msDelay(500);
	}
	
	public float getGreenMin() {
		return greenMin;
	}
	
	public float getGreenMax() {
		return greenMax;
	}
	
	public float getRedMin() {
		return redMin;
	}
	
	public float getRedMax() {
		return redMax;
	}
	
	private void calibrate() {
		float[] lightSample = new float[light.sampleSize()];
		int pollInterval = 20; //every x degrees record the colour, must be divisable by 360
		int pollTimes = 360/pollInterval; //turn x number of times
		float resolution = pollInterval/2; //The error margin (degrees) for repeating until desired degrees turned, large value for speed
		float debt = 0; // the amount of degrees left to turn until 360 degrees is reached
		
		LCD.clear();
		
		for (int i=0;i<pollTimes;i++) {
			debt += calibrationRotation(pollInterval, resolution);			
			
 			light.fetchSample(lightSample, 0);
 			
 			float redLevel = lightSample[0];
			if (redLevel < redMin) { redMin = redLevel; }
			else if (redLevel > redMax) { redMax = redLevel; }
 			
			float greenLevel = lightSample[1];
			if (greenLevel < greenMin) { greenMin = greenLevel; }
			else if (greenLevel > greenMax) { greenMax = greenLevel; }
			
			LCD.drawString("This is loop " + i + "out of " + pollTimes + " loops", 0, 0);
			Button.ENTER.waitForPressAndRelease();
			LCD.clear();
		}
		
		pilot.rotate(debt); //rotates the last few degrees to face the starting position
	}
	
	private float calibrationRotation(float distance, float resolution) {
	    float oldHeading;		    
		float newHeading;
		int timeout = 3;
		
		do {
			oldHeading = poseProvider.getPose().getHeading();	    
			
			pilot.rotate(distance);
			
			LCD.drawString("This is rotating " + distance + " degrees", 0, 0);
			Button.ENTER.waitForPressAndRelease();
			LCD.clear();
			
			newHeading = poseProvider.getPose().getHeading();
			distance = newHeading - oldHeading;		
			timeout--;
		}
		while (Math.abs(distance) > Math.abs(resolution) && (timeout > 0));
		
		return distance;
	}
}