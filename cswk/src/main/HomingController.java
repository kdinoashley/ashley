import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class HomingController implements IRobotController {
	// the robot in the maze
	private IRobot robot;
	// a flag to indicate whether we are looking for a path
	private boolean active = false;
	// a value (in ms) indicating how long we should wait
	// between moves
	private int delay;

	private final int 前 = 0;
	private final int 左 = 1;
	private final int 後ろ = 2;
	private final int 右 = 3;

	private final int 北 = 0;
	private final int 西 = 1;
	private final int 南 = 2;
	private final int 東 = 3;

	private int[] direction = {北, 西, 南, 東};

	private int absolutelyStrengthen(int absoluteDirection) {
		switch (absoluteDirection) {
			case IRobot.NORTH:
				return 北;
			case IRobot.WEST:
				return 西;
			case IRobot.SOUTH:
				return 南;
			case IRobot.EAST:
				return 東;
		}
		return -1;
	}

	private int absolutelyWeaken(int absoluteDirection) {
		switch (absoluteDirection) {
			case 北:
				return IRobot.NORTH;
			case 西:
				return IRobot.WEST;
			case 南:
				return IRobot.SOUTH;
			case 東:
				return IRobot.EAST;
		}
		return -1;
	}

	private int strengthenedRedirect(int strengthenedDirection, int strengthenedTarget) {
		return (strengthenedTarget - strengthenedDirection + 4) % 4;
	}

	private int relativelyWeaken(int relativeDirection) {
		switch (relativeDirection) {
			case 前:
				return IRobot.AHEAD;
			case 左:
				return IRobot.LEFT;
			case 後ろ:
				return IRobot.BEHIND;
			case 右:
				return IRobot.RIGHT;
		}
		return -1;
	}

	private boolean rightByDistance(int strengthenedDirection) {
		Point location = robot.getLocation();
		Point target = robot.getTargetLocation();
		switch (strengthenedDirection) {
			case 北:
				return isTargetNorth() == 1;
			case 西:
				return isTargetEast() == -1;
			case 南:
				return isTargetNorth() == -1;
			case 東:
				return isTargetEast() == 1;
		}
		return false;
	}

	private int strengthenedLook(int strengthenedDirection) {
		return robot.look(relativelyWeaken(strengthenedRedirect(absolutelyStrengthen(robot.getHeading()), strengthenedDirection)));
	}

	public byte isTargetNorth() {
		if (robot.getLocation().y < robot.getTargetLocation().y) {
			return -1;
		} else if (robot.getLocation().y > robot.getTargetLocation().y) {
			return 1;
		} else {
			return 0;
		}
	}

	public byte isTargetEast() {
		if (robot.getLocation().x < robot.getTargetLocation().x) {
			return 1;
		} else if (robot.getLocation().x > robot.getTargetLocation().x) {
			return -1;
		} else {
			return 0;
		}
	}


	// make them happy
	public int lookHeading(int absoluteDirection) {
		return strengthenedLook(absolutelyStrengthen(absoluteDirection));
	}

	private int[] authorize(int[] right, int direction) {
		int[] result = new int[right.length + 1];
		for (int i = 0; i < right.length; i++) {
			result[i] = right[i];
		}
		result[right.length] = direction;
		return result;
	}

	public int determineHeading() {
		int[] discovery = new int[0];
		int[] right = new int[0];
		int[] harmless = new int[0];
		for (int i = 0; i < direction.length; i++) {
			if (lookHeading(absolutelyWeaken(direction[i])) != IRobot.WALL) {
				harmless = authorize(harmless, direction[i]);
				if (strengthenedLook(direction[i]) == IRobot.PASSAGE) {
					discovery = authorize(discovery, direction[i]);
					if (rightByDistance(direction[i])) {
						right = authorize(right, direction[i]);
					}
				}
			}
		}
		if (right.length == 0) {
			right = discovery;
		}
		if (right.length == 0) {
			right = harmless;
		}
		int carefullyChosenResult = (int)(Math.random() * right.length);
		return relativelyWeaken(strengthenedRedirect(absolutelyStrengthen(robot.getHeading()), right[carefullyChosenResult]));
	}

	// this method is called when the "start" button is clicked
	// in the user interface
	public void start() {
		this.active = true;

		while (!robot.getLocation().equals(robot.getTargetLocation()) && active) {
			robot.face(determineHeading());
			robot.advance();
		}

		if (delay > 0)
			robot.sleep(delay);
	}


	// this method returns a description of this controller
	public String getDescription() {
		return "Product by Future Tech Co Ltd.";
	}

	// sets the delay
	public void setDelay(int millis) {
		delay = millis;
	}

	// gets the current delay
	public int getDelay() {
		return delay;
	}

	// stops the controller
	public void reset() {
		active = false;
	}

	// sets the reference to the robot
	public void setRobot(IRobot robot) {
		this.robot = robot;
	}
}
