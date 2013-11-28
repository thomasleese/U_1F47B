package bot;

public abstract class Gun extends Component {

    protected double bulletPower;
    protected double rotation;

    public Gun(State state) {
        super(state);
    }

    public double getBulletPower() {
        return this.bulletPower
    }

    public double getRotation() {
        return this.rotation;
    }

}
