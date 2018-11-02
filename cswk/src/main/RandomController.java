import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class RandomController implements IRobotController {
    // the robot in the maze
    private IRobot robot;
    // a flag to indicate whether we are looking for a path
    private boolean active = false;
    // a value (in ms) indicating how long we should wait
    // between moves
    private int delay;

    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {
        this.active = true;
        int a=0; // the number of moving ahead
        int l=0; // the number of moving left
        int r=0; // the number of moving right
        int b=0; // the number of moving behind
        int i = 0; // total number of moves

        // loop while we haven't found the exit and the agent
        // has not been interrupted
        while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {

            double index;

            // keep moving ahead if no walls and total number of moves is not a multiple of 8 (no need for changing direction)
            if ((robot.look(IRobot.AHEAD) != IRobot.WALL && i%8!=0)) {
                robot.getLogger().log(IRobot.AHEAD);
                robot.advance();
                //increase the number of moving ahead by one
                a++;
                i = a+l+r+b;;
            }

            // change direction if there is a wall ahead and total number of moves is a multiple of 8
            while ((robot.look(IRobot.AHEAD) == IRobot.WALL || i%8==0 ) && !robot.getLocation().equals(robot.getTargetLocation())) {

                // generate a double number
                index = (Math.random());

                // the situation that total number of moves is a multiple of 8 but can only go ahead
                if(robot.look(IRobot.LEFT) == IRobot.WALL && robot.look(IRobot.RIGHT) == IRobot.WALL && robot.look(IRobot.AHEAD)!= IRobot.WALL){
                    //keep track of the current number of moving ahead
                    robot.getLogger().log(IRobot.AHEAD);
                    //increase the number of moving ahead by one
                    a++;
                }

                // based on previous no-parameter stats, index is determined to make the probability of each direction relatively equal
                // choose a direction, advance if no walls, otherwise go back to the beginning of the while loop
                else if (index < 0.48) {
                    if(robot.look(IRobot.LEFT) != IRobot.WALL) {
                        // face left if no walls
                        robot.face(IRobot.LEFT);
                        //keep track of the current number of moving left
                        robot.getLogger().log(IRobot.LEFT);
                        //increase the number of moving left by one
                        l++;
                    }else{
                        // exit if wall exists on the left
                        break;
                    }
                } else if (index < 0.985) {
                    if(robot.look(IRobot.RIGHT) != IRobot.WALL) {
                        //face right if no walls
                        robot.face(IRobot.RIGHT);
                        //keep track of the current number of moving right
                        robot.getLogger().log(IRobot.RIGHT);
                        //increase the number of moving right by one
                        r++;
                    }else{
                        // exit if wall exists on the right
                        break;
                    }
                } else if (index < 1){
                    if (robot.look(IRobot.BEHIND) != IRobot.WALL) {
                        robot.face(IRobot.BEHIND);
                        //keep track of the current number of moving behind
                        robot.getLogger().log(IRobot.BEHIND);
                        //increase the number of moving behind by one
                        b++;
                    }else{
                        // exit if wall exists behind
                        break;
                    }
                }

                // advance if meet conditions above
                robot.advance();
            }

            // update the total number of moves
            i = a+l+r+b;
        }

        // check if it is coherent with robot.getLogger()
        System.out.print(i);

        // wait for a while if we are supposed to
        if (delay > 0)
            robot.sleep(delay);
    }



    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which randomly chooses where to go";
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
