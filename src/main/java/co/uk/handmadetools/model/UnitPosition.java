package co.uk.handmadetools.model;

public class UnitPosition {

    private final int x;
    private final int y;

    public UnitPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UnitPosition plus(UnitSpeed unitSpeed) {
        return new UnitPosition(x + unitSpeed.getVx(), y + unitSpeed.getVy());
    }

    public Position toPosition() {
        return new Position(x, y);
    }

}
