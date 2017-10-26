package co.uk.handmadetools.model;

import java.time.Instant;

public class Event {

    public enum Type {
        CREATED,
        CHANGE_DIRECTION
    }

    private final Type type;
    private final Position position;
    private final Speed speed;
    private final String name;
    private final Instant created;

    public Event(Type type, Position position, Speed speed, String name, Instant created) {
        this.type = type;
        this.position = position;
        this.speed = speed;
        this.name = name;
        this.created = created;
    }

    public Instant getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Type getType() {
        return type;
    }

}
