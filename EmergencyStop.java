import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

class EmergencyStops implements Behavior {
	private MovePilot pilot;
	
	public EmergencyStops(MovePilot pilot) {
		this.pilot = pilot;
	}
	
	
	
	public boolean takeControl() {
		return (Button.ESCAPE.isDown());
	}

	public void action() {
		pilot.stop();
		System.exit(0);
	}

	public void suppress() {
	}
}