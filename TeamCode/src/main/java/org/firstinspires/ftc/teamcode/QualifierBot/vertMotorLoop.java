
public class vertMotorLoop extends Thread{
    private final LinearOpMode opMode;
    public PIDControl(LinearOpMode opMode){
        this.opMode = opMode;
    }

    @Override
    public void run(){
        opMode.robot.vertMotor.setMode() //lalala
    }
}
