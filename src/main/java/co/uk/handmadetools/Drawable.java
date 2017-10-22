package co.uk.handmadetools;

import java.time.Instant;

public class Drawable {

    private final float x;
    private final float y;
    private final float xFacing;
    private final float yFacing;
    private final String name;
    private final Instant created;

    public Drawable(float x, float y, float xFacing, float yFacing, String name, Instant created) {
        this.x = x;
        this.y = y;
        this.xFacing = xFacing;
        this.yFacing = yFacing;
        this.name = name;
        this.created = created;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getXFacing() {
        return xFacing;
    }

    public float getYFacing() {
        return yFacing;
    }

    public String getName() {
        return name;
    }

    public Instant getCreated() {
        return created;
    }
}
