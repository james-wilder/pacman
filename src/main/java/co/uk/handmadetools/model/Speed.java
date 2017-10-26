package co.uk.handmadetools.model;

public class Speed {

    public static final Speed STOP = new Speed(0, 0);

    private final float vx;
    private final float vy;

    public Speed(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

}
