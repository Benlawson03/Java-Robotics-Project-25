**Introduction**  
In this report I will provide an overview of how Lego Mindstorms operates, along with key programming concepts used in robot design and development. It includes a detailed description of the robot our team created, explaining its functionality and how it works. I'll also add how the report reflects on the collaborative dynamics and synergy within our group and evaluates how effectively we worked together throughout the project which made our robot effective/ineffective.

**Robot Description**  
Our robot was designed to autonomously solve a maze. It uses an ultrasonic sensor to scan the environment, measuring the distance between itself and the surrounding walls or potential paths. This allowed the robot to navigate through tight corners and open spaces. To enhance its decision-making capabilities, the robot is equipped with colour sensors that help identify specific markers within the maze. The colour sensors serve two primary purposes:  

1. Detect trap doors that are marked with a green colour, signalling the robot to proceed through that specific path.  
2. Alert the robot if it encounters a red wall, indicating it has reached the end of the maze or there is no way forward.  

The robot is driven by motors on each side, which allow it to move and rotate efficiently. This motorised movement helps it make precise turns and navigate through the maze without straying from its path. The combination of ultrasonic and colour sensors, along with the motors, enables the robot to autonomously solve the maze, avoiding obstacles and completing the challenge.

**Key Concepts**  

**Lego Mindstorms**  
Lego Mindstorms are programmable bricks equipped with simple computers that run Java Virtual Machines (JVM). These bricks can have Java-based programs uploaded to them and can execute these programs autonomously. The bricks feature buttons for user input and a button and speaker for displaying messages or playing sounds. Additionally, Lego Mindstorms support up to four motors and four sensors, enabling the robot to interact with and respond to its environment.

**Sensors**  
Sensors are devices that measure specific data and send the information back to the brick for processing. The Lego Mindstorms sensors include:  

- Sound sensor – measures loudness of the environment.  
- Ultrasonic sensor – emits a beam to detect the distance of objects.  
- Colour sensor – detects the colour of objects, used for tasks like line following or detecting markers.  

Our robot uses an ultrasonic sensor and a colour sensor to navigate the maze. It also monitors button inputs and battery level to ensure enough power for the task. These sensors and inputs work together to help the robot autonomously navigate and respond to its environment.

**MovePilot**  
MovePilot is a powerful class within the Lejos system that simplifies robot movement. By providing wheel diameter, distance from the robot’s centre, and chassis type, MovePilot enables precise movements, including straight-line travel and controlled turns. It also allows setting maximum speed and acceleration.

**Navigation**  
Navigation is key in robotics. MovePilot combined with `OdometryPoseProvider` tracks the robot's position in real-time. Odometry calculates location based on sensor/motor data, enabling the robot to navigate to specific waypoints and avoid obstacles.

**Behaviour**  
Lejos supports arbitrators and behaviours. A behaviour executes when a certain condition is met. For example, the “doorcolour” behaviour detects walls using the ultrasonic sensor and rotates to check doors. The arbitrator manages multiple behaviours and selects the highest-priority behaviour active at any moment.

**Reflection**  
Teamwork had challenges. Two members did not contribute to coding. The remaining teammate and I collaborated closely, proofreading, debugging, and understanding each other’s work. We also explored LeJOS EV3 documentation to handle sensors and movement. Despite challenges, our collaboration allowed the robot to function effectively.

**Technical Content**  

**Classes**  

- **Driver.java** – Main class. Initialises motors and sensors, constructs MovePilot, registers behaviours with the arbitrator, and handles program execution.  
- **CalibrateCS.java** – Calibrates the colour sensor by rotating the robot and recording RGB values.  
- **MapUpdate.java** – Updates a 2D map of visited positions in a separate thread.  
- **Trundling.java** – Moves robot forward continuously until interrupted.  
- **DoorColour.java** – Detects doors, rotates to check colour, and decides behaviour accordingly.  
- **BatteryLevels.java** – Monitors battery voltage and displays warnings.  
- **EmergencyStops.java** – Stops the robot when ESCAPE button is pressed.

**Code Examples**  
- MovePilot is used for precise linear and angular speed control.  
- PoseProvider tracks position and direction for odometry.  
- Sensors are initialised and calibrated, with error handling.  
- Behaviours are fed to the Arbitrator, ensuring only the highest-priority behaviour is active.  
- Colour sensor calibration ensures accurate detection despite lighting conditions.  
- MapUpdate maintains a real-time map of explored areas.

**Control Flow**  
The robot displays a welcome message, waits for Enter, initialises motors, sensors, and MovePilot, calibrates the colour sensor, starts the map thread, and runs behaviours with the arbitrator. Behaviour execution is determined by sensor input and priority (e.g., low battery or emergency stop).

**Conclusion**  
Despite some setbacks, we accomplished the core features: sensor calibration, mapping, and behaviour-based reactions. Door detection was occasionally buggy, and team contributions were uneven. However, the two of us who contributed consistently collaborated effectively, debugging, testing, and meeting outside labs. Overall, the robot functions as intended, and we are proud of our achievements.
