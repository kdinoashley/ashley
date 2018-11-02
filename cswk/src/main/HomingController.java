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

    private final int AHEAD = 0;
    private final int RIGHT = 1;
    private final int BEHIND = 2;
    private final int LEFT = 3;

    private final int NORTH = 0;
    private final int EAST = 1;
    private final int SOUTH = 2;
    private final int WEST = 3;

    // generate an array containing 4 directions
    private int[] direction = {NORTH, EAST, SOUTH, WEST};

    // assign values to absolute directions
    private int absolutelyStrengthen(int absoluteDirection) {
        switch (absoluteDirection) {
            case IRobot.NORTH:
                return NORTH;
            case IRobot.EAST:
                return EAST;
            case IRobot.SOUTH:
                return SOUTH;
            case IRobot.WEST:
                return WEST;
        }
        return -1;
    }

    // refer back to absolute directions
    private int absolutelyWeaken(int absoluteDirection) {
        switch (absoluteDirection) {
            case NORTH:
                return IRobot.NORTH;
            case EAST:
                return IRobot.EAST;
            case SOUTH:
                return IRobot.SOUTH;
            case WEST:
                return IRobot.WEST;
        }
        return -1;
    }

    // refer back to relative direction from int values
    private int relativelyWeaken(int relativeDirection) {
        switch (relativeDirection) {
            case AHEAD:
                return IRobot.AHEAD;
            case RIGHT:
                return IRobot.RIGHT;
            case BEHIND:
                return IRobot.BEHIND;
            case LEFT:
                return IRobot.LEFT;
        }
        return -1;
    }

    // this method returns 1 if the target is north of the
    // robot, -1 if the target is south of the robot, or
    // 0 if otherwise.
    public byte isTargetNorth() {
        // TODO: Implement for Task 5
        if (robot.getLocation().y < robot.getTargetLocation().y) {
            return -1;
        } else if (robot.getLocation().y > robot.getTargetLocation().y) {
            return 1;
        } else {
            return 0;
        }
    }

    // this method returns 1 if the target is east of the
    // robot, -1 if the target is west of the robot, or
    // 0 if otherwise.
    public byte isTargetEast() {
        // TODO: Implement for Task 5
        if (robot.getLocation().x < robot.getTargetLocation().x) {
            return 1;
        } else if (robot.getLocation().x > robot.getTargetLocation().x) {
            return -1;
        } else {
            return 0;
        }
    }

    // this method returns whether the target is in certain direction of the robot
    private boolean rightByDistance(int strengthenedDirection) {
        switch (strengthenedDirection) {
            case NORTH:
                return isTargetNorth() == 1;
            case WEST:
                return isTargetEast() == -1;
            case SOUTH:
                return isTargetNorth() == -1;
            case EAST:
                return isTargetEast() == 1;
        }
        return false;
    }

    // this method causes the robot to look to the absolute
    // direction that is specified as argument and returns
    // what sort of square there is
    public int lookHeading(int absoluteDirection) {
        // TODO: Implement for Task 5
        // the difference of the corresponding values of the directions of Heading and aim is how much robot should turn
        // +4 ensures it is always positive, remainder is same
        int turn = (absolutelyStrengthen(absoluteDirection) - absolutelyStrengthen(robot.getHeading())+ 4) % 4;
        return robot.look(relativelyWeaken(turn));
    }

    // this method creates a new array that expand original array by 1 element
    private int[] extendArray(int[] originalArray, int specificDirection) {
        // create a new array that has one more element than the original one
        int[] result = new int[originalArray.length + 1];
        // the first few elements of new array are the same as the original one's
        for (int i = 0; i < originalArray.length; i++) {
            result[i] = originalArray[i];
        }
        // add specificDirection to the last one
        result[originalArray.length] = specificDirection;
        return result;
    }

    // this method determines the heading in which the robot
    // should head next to move closer to the target
    public int determineHeading() {
        int[] noWall = new int[0];
        int[] neverBeenBefore = new int[0];
        int[] closer = new int[0];

        // check if four directions meet requirements above one by one
        for (int i = 0; i < direction.length; i++) {
            // this array contains directions that will not lead robot to walls
            if (lookHeading(absolutelyWeaken(direction[i])) != IRobot.WALL) {
                noWall = extendArray(noWall, direction[i]);
                // in addition, this array contains directions that lead robot to roads never been before
                if (lookHeading(absolutelyWeaken(direction[i])) == IRobot.PASSAGE) {
                    neverBeenBefore = extendArray(neverBeenBefore, direction[i]);
                    // again in addition, this array contains directions that lead robot closer to target
                    if (rightByDistance(direction[i])) {
                        closer = extendArray(closer, direction[i]);
                    }
                }
            }
        }



        // if no directions meet all 3 requirements above, use the array that meets first 2 requirements
        if(closer.length == 0){
            closer = neverBeenBefore;
        }

        // if the array is still empty, use the array that only meets 1st requirement
        if (closer.length == 0) {
            closer = noWall;
        }

        // generate a random integer between 0 and closer.length-1, indicate a specific index of closer
        int index = (int)(Math.random() * closer.length);
        // work out which relative direction robot should face to get to the chosen direction
        int turn = (closer[index] - absolutelyStrengthen(robot.getHeading()) +4 ) % 4;
        return relativelyWeaken(turn);
    }

    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {
        this.active = true;

        while (!robot.getLocation().equals(robot.getTargetLocation()) && active) {

            // TODO: TASK4
            // int direction = IRobot.AHEAD;
            // int randno = 0;
            // randno = (int) (Math.random() * 4);

            // change the direction based on the random number
            // if (randno == 0) {
            //  direction = IRobot.AHEAD;
            // } else if (randno == 1) {
            //  direction = IRobot.LEFT;
            // } else if (randno == 2) {
            //  direction = IRobot.RIGHT;
            // } else {
            //  direction = IRobot.BEHIND;
            // }

            //if (robot.look(direction)!=IRobot.WALL) {
            //  robot.face(direction);  /* Face the direction */
            //robot.advance();
            //}

            robot.face(determineHeading());
            robot.advance();
        }

        if (delay > 0)
            robot.sleep(delay);
    }


    // this method returns a description of this controller
    public String getDescription() {
        return "A controller which homes in on the target";
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