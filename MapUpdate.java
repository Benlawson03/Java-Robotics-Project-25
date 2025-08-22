import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

public class MapUpdate extends Thread{
	private PoseProvider poseProvider;
	private int[][] map;
	private final long interval;
	
	public MapUpdate(PoseProvider poseProvider, int[][] map, long interval) {
		this.poseProvider = poseProvider;
		this.map = map;
		this.interval = interval;
	}
	
	public void run() {
		while (true) {
			Pose pose = poseProvider.getPose();
			int x = (int) (pose.getX() / map[0].length);
			int y = (int) (pose.getY() / map.length);
        
			if (x >= 0 && x < map.length && y >= 0 && y < map[0].length) {
				map[x][y] = 1; 
			}
        
			Delay.msDelay(interval);
		}
	}
}