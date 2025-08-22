import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class DoorColour implements Behavior{
	private MovePilot pilot;
	private PoseProvider poseProvider;
	private SampleProvider distanceSensor;
	private float[] distanceSample;
	private SampleProvider light;
	private float redAverage;
	private float greenAverage;
	private boolean suppressed = false;
	final static private float FRONT_DISTANCE = 0.025f; // distance (m) between distance sensor and front of robot 
	final private float RESOLUTION = 1; //The error margin (degrees) for repeating until desired degrees turned

	public DoorColour(
			MovePilot pilot,
			PoseProvider poseProvider,
			EV3UltrasonicSensor sensor1,
			EV3ColorSensor sensor2,
			float[] lightLevels
			) {
		
		this.pilot = pilot;
		this.poseProvider = poseProvider;
		this.distanceSensor = sensor1.getDistanceMode();
		this.distanceSample = new float[distanceSensor.sampleSize()];
		this.light = sensor2.getRGBMode();
		this.greenAverage = (lightLevels[2] + lightLevels[3])/2;
		this.redAverage = (lightLevels[0] + lightLevels[1])/2;
		}
	
	public boolean takeControl() {
		distanceSensor.fetchSample(distanceSample, 0);
		return (distanceSample[0] < FRONT_DISTANCE + 0.01); //if door is closer than 1cm
	}

	public void action() {
		suppressed = false;
		pilot.rotate(90);
		for (int i=-90;i<360;i+=90) {
			if (redDoor(light)) {
				pilot.stop();
				LCD.clear();
				LCD.drawString("maze solved", 0,0);
				System.exit(0);
			}
			
			else if (greenDoor(light)) { // Possibly remove suppress check. 
				break;
			} else {
				pilot.rotate(-90);
			}
		}
	}

	public void suppress() {
		suppressed = true;
	}
	
	private boolean greenDoor(SampleProvider sensor) {
		float[] lightSample = new float[light.sampleSize()];
		light.fetchSample(lightSample, 0);
		float greenLevel = lightSample[1];
		return greenLevel > greenAverage;
	}
	
	private boolean redDoor(SampleProvider sensor) {
		float[] lightSample = new float[light.sampleSize()];
		light.fetchSample(lightSample, 0);
		float redLevel = lightSample[0];
		return redLevel > redAverage;
	}
	
//	private void rotation(float distance) {
//	    float oldHeading;		    
//		float newHeading;
//		
//		do {
//			oldHeading = poseProvider.getPose().getHeading();	    
//			
//			pilot.rotate(distance);
//			
//			newHeading = poseProvider.getPose().getHeading();
//			distance = distance - (newHeading - oldHeading);
//		}
//		while (Math.abs(distance) > Math.abs(RESOLUTION) && !suppressed);
//	}
}