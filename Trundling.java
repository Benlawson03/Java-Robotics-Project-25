import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class Trundling implements Behavior {
	private MovePilot pilot;
	private boolean suppressed = false;

	public Trundling(MovePilot pilot) {
		this.pilot = pilot;
	}

	public boolean takeControl() {
		return !Button.ESCAPE.isDown();
	}

	public void action() {
		suppressed = false;
		pilot.forward();
		while (!suppressed) {
			Thread.yield();
		}
		pilot.stop();
	}

	public void suppress() {
		suppressed = true;
	}
}