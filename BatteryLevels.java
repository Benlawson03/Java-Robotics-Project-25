import lejos.hardware.Battery;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class BatteryLevels implements Behavior {
	private boolean suppressed = false;
	
	public boolean takeControl() {
		return Battery.getVoltage() < 7.0;
	}

	public void action() {
		suppressed = false;
		while (Battery.getVoltage() < 7.0) {
			LCD.clear();
			LCD.drawString("Battery Low!", 0, 0);
			Sound.beep();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	public void suppress() {
		suppressed = true;
	}
}