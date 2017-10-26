package co.uk.handmadetools.model;

public class Position {

    private final float x;
    private final float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position o = (Position) other;
            return (o.getX() == x) && (o.getY() == y);
        }
        throw new IllegalArgumentException();
    }

}
