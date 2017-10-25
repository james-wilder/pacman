package co.uk.handmadetools.model;

import java.time.Instant;

public class Event {

    public Type getType() {
        return type;
    }

    public enum Type {
        CREATED,
        CHANGE_DIRECTION
    }

    private final Type type;
    private final float x;
    private final float y;
    private final float xs;
    private final float ys;
    private final String name;
    private final Instant created;

    public Event(Type type, float x, float y, float xs, float ys, String name, Instant created) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.xs = xs;
        this.ys = ys;
        this.name = name;
        this.created = created;
    }

    public Instant getCreated() {
        return created;
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

}
