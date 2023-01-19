package api.state;

public class RobotState {

    private int speed;
    private boolean isMoving;

    public RobotState(int speed, boolean isMoving) {
        this.speed = speed;
        this.isMoving = isMoving;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

}
