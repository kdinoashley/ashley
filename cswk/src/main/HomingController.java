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

    int heading;
    int direction;
    int rand;

    // this method returns 1 if the target is north of the
    // robot, -1 if the target is south of the robot, or
    // 0 if otherwise.
    public byte isTargetNorth() {
        // TODO: Implement for Task 5
        if(robot.getLocation().y < robot.getTargetLocation().y){
            return -1;
        }else if(robot.getLocation().y > robot.getTargetLocation().y){
            return 1;
        }else{
            return 0;
        }
    }

    // this method returns 1 if the target is east of the
    // robot, -1 if the target is west of the robot, or
    // 0 if otherwise.
    public byte isTargetEast() {
        // TODO: Implement for Task 5
        if(robot.getLocation().x < robot.getTargetLocation().x) {
            return 1;
        }else if(robot.getLocation().x > robot.getTargetLocation().x){
            return -1;
        }else{
            return 0;
        }
    }

    // this method causes the robot to look to the absolute
    // direction that is specified as argument and returns
    // what sort of square there is
    public int lookHeading(int absoluteDirection) {
        // TODO: Implement for Task 5
        if((robot.getHeading()+0)%4==absoluteDirection%4){
            return robot.look(IRobot.AHEAD);
        } else if((robot.getHeading()+1)%4==absoluteDirection%4){
            return robot.look(IRobot.RIGHT);
        } else if((robot.getHeading()+2)%4==absoluteDirection%4){
            return robot.look(IRobot.BEHIND);
        } else{
            return robot.look(IRobot.LEFT);
        }
    }

    // this method determines the heading in which the robot
    // should head next to move closer to the target
    public int determineHeading(){
        // TODO: Implement for Task 5
        // 0: north
        // 1: east
        // 2: south
        // 3: west
        if ((isTargetEast()==1) && (isTargetNorth() == -1)) { // south-east
            if ((lookHeading(IRobot.EAST) != IRobot.WALL) && (lookHeading(IRobot.SOUTH) != IRobot.WALL)) {
                rand = (int) (Math.random() * 2 + 1); // rand = 1 or 2
            } else if ((lookHeading(IRobot.EAST) != IRobot.WALL) && (lookHeading(IRobot.SOUTH) == IRobot.WALL)) {
                direction = IRobot.EAST;
            } else if ((lookHeading(IRobot.SOUTH) != IRobot.WALL) && (lookHeading(IRobot.EAST) == IRobot.WALL)) {
                direction = IRobot.SOUTH;
            } else {
                do {
                    rand = (int) (Math.random() * 4);
                } while (lookHeading(rand) == IRobot.WALL);
            }
        } else if ((isTargetEast() == 1) && (isTargetNorth() == 1)) { // north-east
            if (lookHeading(IRobot.EAST) != IRobot.WALL && lookHeading(IRobot.NORTH) != IRobot.WALL) {
                rand = (int) (Math.random() * 2); // rand = 0 or 1
            } else if ((lookHeading(IRobot.EAST) != IRobot.WALL) && (lookHeading(IRobot.NORTH) == IRobot.WALL)) {
                direction = IRobot.EAST;
            } else if ((lookHeading(IRobot.NORTH) != IRobot.WALL) && (lookHeading(IRobot.EAST) == IRobot.WALL)) {
                direction = IRobot.NORTH;
            } else {
                do {
                    rand = (int) (Math.random() * 4);
                } while (lookHeading(rand) == IRobot.WALL);
            }
        } else if ((isTargetEast() == -1) && (isTargetNorth() == -1)) { // south-west
            if ((lookHeading(IRobot.WEST) != IRobot.WALL) && (lookHeading(IRobot.SOUTH) != IRobot.WALL)) {
                rand = (int) (Math.random() * 2 + 2); // rand = 2 or 3
            } else if ((lookHeading(IRobot.WEST) != IRobot.WALL) && (lookHeading(IRobot.SOUTH) == IRobot.WALL)) {
                direction = IRobot.WEST;
            } else if ((lookHeading(IRobot.SOUTH) != IRobot.WALL) && (lookHeading(IRobot.WEST) == IRobot.WALL)) {
                direction = IRobot.SOUTH;
            } else {
                do {
                    rand = (int) (Math.random() * 4);
                } while (lookHeading(rand) == IRobot.WALL);
            }
        } else if ((isTargetEast() == -1) && (isTargetNorth() == 1)) { // north-west
            if ((lookHeading(IRobot.WEST) != IRobot.WALL) && (lookHeading(IRobot.NORTH) != IRobot.WALL)) {
                rand = (int) (Math.random() * 2) * 3; // rand = 0 or 3
            } else if ((lookHeading(IRobot.WEST) != IRobot.WALL) && (lookHeading(IRobot.NORTH) == IRobot.WALL)) {
                direction = IRobot.WEST;
            } else if ((lookHeading(IRobot.NORTH) != IRobot.WALL) && (lookHeading(IRobot.NORTH) != IRobot.WALL)) {
                direction = IRobot.NORTH;
            } else {
                do {
                    rand = (int) (Math.random() * 4);
                } while (lookHeading(rand) == IRobot.WALL);
            }
        } else if ((isTargetEast() == 1) && (isTargetNorth() == 0) && (lookHeading(IRobot.EAST)!=IRobot.WALL)) {
            direction = IRobot.EAST;
        } else if ((isTargetNorth() == -1) && (isTargetEast() == 0) && (lookHeading(IRobot.SOUTH) != IRobot.WALL)) {
            direction = IRobot.SOUTH;
        } else if ((isTargetNorth() == 1) && (isTargetEast() == 0) && (lookHeading(IRobot.NORTH) != IRobot.WALL)) {
            direction = IRobot.NORTH;
        } else if ((isTargetEast() == -1) && (isTargetNorth() == 0) && (lookHeading(IRobot.WEST) != IRobot.WALL)) {
            direction = IRobot.WEST;
        } else {
            do {
                rand = (int) (Math.random() * 4);
            } while (lookHeading(rand) == IRobot.WALL);
            return rand;
        }


        if(rand==0) {
            direction = IRobot.NORTH;
        }else if(rand==1) {
            direction = IRobot.EAST;
        }else if(rand==2) {
            direction = IRobot.SOUTH;
        }else {
            direction = IRobot.WEST;
        }

        if((robot.getHeading()+0)%4==direction%4){
            heading = IRobot.AHEAD;
        } else if((robot.getHeading()+1)%4==direction%4){
            heading = IRobot.RIGHT;
        } else if((robot.getHeading()+2)%4==direction%4){
            heading = IRobot.BEHIND;
        } else{
            heading = IRobot.LEFT;
        }

        return heading;
    }

    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {
        this.active = true;

        while (!robot.getLocation().equals(robot.getTargetLocation()) && active) {
            robot.face(determineHeading());
            robot.advance();

            //int direction = IRobot.AHEAD;
            //int randno = 0;
            //randno = (int) (Math.random() * 4);

            // change the direction based on the random number
            //if (randno == 0) {
            //  direction = IRobot.AHEAD;
            //} else if (randno == 1) {
            //  direction = IRobot.LEFT;
            //} else if (randno == 2) {
            //  direction = IRobot.RIGHT;
            //} else {
            //  direction = IRobot.BEHIND;
            //}

            //if (robot.look(direction)!=IRobot.WALL) {
            //  robot.face(direction);  /* Face the direction */
            //robot.advance();
            //}

        }

        // wait for a while if we are supposed to
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
