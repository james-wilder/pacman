package co.uk.handmadetools.model;

public class Drawable {

    private final Position position;
    private final Speed speed;
    private final String name;

    public Drawable(Position position, Speed speed, String name) {
        this.position = position;
        this.speed = speed;
        this.name = name;
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
}
