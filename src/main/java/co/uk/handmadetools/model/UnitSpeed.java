package co.uk.handmadetools.model;

public class UnitSpeed {

    public static final UnitSpeed LEFT = new UnitSpeed(-1, 0);
    public static final UnitSpeed RIGHT = new UnitSpeed(1, 0);
    public static final UnitSpeed UP = new UnitSpeed(0, -1);
    public static final UnitSpeed DOWN = new UnitSpeed(0, 1);

    private final int vx;
    private final int vy;

    public UnitSpeed(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

}
