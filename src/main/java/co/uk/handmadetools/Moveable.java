package co.uk.handmadetools;

import java.time.Instant;

public class Moveable {

    private final float x;
    private final float y;
    private final float xs;
    private final float ys;
    private final String name;
    private final Instant created;

    public Moveable(float x, float y, float xs, float ys, String name, Instant created) {
        this.x = x;
        this.y = y;
        this.xs = xs;
        this.ys = ys;
        this.name = name;
        this.created = created;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getXs() {
        return xs;
    }

    public float getYs() {
        return ys;
    }

    public String getName() {
        return name;
    }

    public float getX(Instant i) {
        return x + xs * 0.001f * (i.toEpochMilli() - created.toEpochMilli());
    }

    public float getY(Instant i) {
        return y + ys * 0.001f * (i.toEpochMilli() - created.toEpochMilli());
    }

}
