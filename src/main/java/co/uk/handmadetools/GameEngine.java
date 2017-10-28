package co.uk.handmadetools;

import co.uk.handmadetools.model.Drawable;
import co.uk.handmadetools.model.Event;
import co.uk.handmadetools.model.MapState;
import co.uk.handmadetools.model.Position;
import co.uk.handmadetools.model.Speed;
import co.uk.handmadetools.model.UnitPosition;
import co.uk.handmadetools.model.UnitSpeed;
import co.uk.handmadetools.ui.KeyState;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.uk.handmadetools.model.Event.Type.CHANGE_DIRECTION;
import static co.uk.handmadetools.model.Event.Type.CREATED;

public class GameEngine {

    private static final List<String> NAMES = Arrays.asList("bunny", "ghost_1", "ghost_2", "ghost_3", "ghost_4");
    private static final List<String> PLAYER_NAMES = Arrays.asList("bunny");
    private static final float SPEED = 1.0f;
    private static final float BUNNY_SPEED = 3.5f;
//    float SPEED = 0.4f;

    private final MapState mapState; // TODO: update with events
    private final List<Event> events;
    private final Instant created;

    public GameEngine() {
        created = Instant.now();
        mapState = new MapState();
        events = new ArrayList<>();

        events.add(new Event(CREATED, new Position(1.0f, 1.0f), new Speed(0.0f, 4.0f), "ghost_1", created));
        events.add(new Event(CREATED, new Position(31f, 1f), new Speed(0.0f, 4.0f), "ghost_2", created));
        events.add(new Event(CREATED, new Position(1.0f, 25f), new Speed(0.0f, -4.0f), "ghost_3", created));
        events.add(new Event(CREATED, new Position(31.0f, 25f), new Speed(0.0f, -4.0f), "ghost_4", created));
//        events.add(new Event(CHANGE_DIRECTION, new Position(9.0f, 10.5f), new Speed(0.0f, 4.0f, "ghost_1", created.plusSeconds(1)));
        events.add(new Event(CREATED, new Position(16.5f, 13f), new Speed(BUNNY_SPEED, 0.0f), "bunny", created));
    }

    public GameEngine(Instant now, List<Event> events, MapState mapState) {
        this.mapState = mapState;
        this.events = events;
        this.created = now;
    }

    public GameEngine update(Instant now) {
        List<Event> newEvents = new ArrayList<>();
        newEvents.addAll(events);
        for (String name : NAMES) {
            Optional<Event> optEvent = events.stream()
                    .filter(event -> event.getName().equals(name))
                    .reduce((a, b) -> b);

            if (optEvent.isPresent()) {
                Event event = optEvent.get();

                Position position1 = calcPosition(event, created);
                Position position2 = calcPosition(event, now);

                UnitPosition unitPosition = new UnitPosition(
                        Math.round(position2.getX()),
                        Math.round(position2.getY())
                );

                UnitSpeed unitSpeed = new UnitSpeed(
                        (int) Math.signum(event.getSpeed().getVx()),
                        (int) Math.signum(event.getSpeed().getVy())
                );

                Position floor1 = new Position(
                        (float) Math.floor(position1.getX()),
                        (float) Math.floor(position1.getY())
                );
                Position floor2 = new Position(
                        (float) Math.floor(position2.getX()),
                        (float) Math.floor(position2.getY())
                );

                if (PLAYER_NAMES.contains(name)) {
                    if ((!position1.equals(event.getPosition()) && !floor1.equals(floor2)) || (event.getSpeed().equals(Speed.STOP))) {
                        boolean canLeft = canGo(unitPosition, UnitSpeed.LEFT);
                        boolean canRight = canGo(unitPosition, UnitSpeed.RIGHT);
                        boolean canUp = canGo(unitPosition, UnitSpeed.UP);
                        boolean canDown = canGo(unitPosition, UnitSpeed.DOWN);

//                        System.out.println("canLeft=" + canLeft);
//                        System.out.println("canRight=" + canRight);
//                        System.out.println("canUp=" + canUp);
//                        System.out.println("canDown=" + canDown);

                        Position boundary;
                        if (event.getSpeed().equals(Speed.STOP)) {
                            boundary = event.getPosition();
                        } else if (event.getSpeed().getVy() == 0) {
                            boundary = new Position(unitPosition.getX(), position1.getY());
                        } else {
                            boundary = new Position(position1.getX(), unitPosition.getY());
                        }

                        Instant newEventTime = instantFromEvent(event, boundary, position2, now);
                        if (event.getSpeed().equals(Speed.STOP)) {
                            newEventTime = now;
                        }
                        if (canRight && ((event.getSpeed().getVx() > 0) || KeyState.isRight())) {
                            if (event.getSpeed().getVx() <= 0) {
                                System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), BUNNY_SPEED, 0f));
                                newEvents.add(new Event(CHANGE_DIRECTION, boundary, new Speed(BUNNY_SPEED, 0), event.getName(), newEventTime));
                            }
                        }
                        if (canLeft && ((event.getSpeed().getVx() < 0) || KeyState.isLeft())) {
                            if (event.getSpeed().getVx() >= 0) {
                                System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), -BUNNY_SPEED, 0f));
                                newEvents.add(new Event(CHANGE_DIRECTION, boundary, new Speed(-BUNNY_SPEED, 0), event.getName(), newEventTime));
                            }
                        }
                        if (canDown && ((event.getSpeed().getVy() > 0) || KeyState.isDown())) {
                            if (event.getSpeed().getVy() <= 0) {
                                System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, BUNNY_SPEED));
                                newEvents.add(new Event(CHANGE_DIRECTION, boundary, new Speed(0, BUNNY_SPEED), event.getName(), newEventTime));
                            }
                        }
                        if (canUp && ((event.getSpeed().getVy() < 0) || KeyState.isUp())) {
                            if (event.getSpeed().getVy() >= 0) {
                                System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, -BUNNY_SPEED));
                                newEvents.add(new Event(CHANGE_DIRECTION, boundary, new Speed(0, -BUNNY_SPEED), event.getName(), newEventTime));
                            }
                        }
                        if (!canRight && (event.getSpeed().getVx() > 0)) {
                            System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, 0f));
                            newEvents.add(new Event(CHANGE_DIRECTION, boundary, Speed.STOP, event.getName(), newEventTime));
                        }
                        if (!canLeft && (event.getSpeed().getVx() < 0)) {
                            System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, 0f));
                            newEvents.add(new Event(CHANGE_DIRECTION, boundary, Speed.STOP, event.getName(), newEventTime));
                        }
                        if (!canDown && (event.getSpeed().getVy() > 0)) {
                            System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, 0f));
                            newEvents.add(new Event(CHANGE_DIRECTION, boundary, Speed.STOP, event.getName(), newEventTime));
                        }
                        if (!canUp && (event.getSpeed().getVy() < 0)) {
                            System.out.println(String.format("%.2f %.2f %.2f %.2f %.2f %.2f ", boundary.getX(), boundary.getY(), event.getSpeed().getVx(), event.getSpeed().getVy(), 0f, 0f));
                            newEvents.add(new Event(CHANGE_DIRECTION, boundary, Speed.STOP, event.getName(), newEventTime));
                        }
                    }
                } else {
                    boolean goLeft = false;
                    boolean goRight = false;
                    boolean do180 = false;

                    Position boundary = null;
                    if (!unitPosition.toPosition().equals(event.getPosition()) && !floor1.equals(floor2)) {
                        if (event.getSpeed().getVy() == 0) {
                            boundary = new Position(unitPosition.getX(), position1.getY());
                        } else {
                            boundary = new Position(position1.getX(), unitPosition.getY());
                        }

                        boolean canLeft = canGo(unitPosition, turnLeft(unitSpeed));
                        boolean canForward = canGo(unitPosition, unitSpeed);
                        boolean canRight = canGo(unitPosition, turnRight(unitSpeed));

//                         System.out.println("canLeft=" + canLeft);
//                         System.out.println("canForward=" + canForward);
//                         System.out.println("canRight=" + canRight);

                        if (!canForward || ((Math.random() < 0.5) && (canLeft || canRight))) {
                            if (!canLeft || (canRight && (Math.random() < 0.5))) {
                                goRight = true;
                            } else {
                                goLeft = true;
                            }
                        }
                    }

                    Speed newSpeed;
                    if (goLeft || goRight || do180) {
                        if (goRight) {
                            newSpeed = turnRight(event.getSpeed());
                        } else if (goLeft) {
                            newSpeed = turnLeft(event.getSpeed());
                        } else {
                            newSpeed = turn180(event.getSpeed());
                        }

                        Instant newEventTime = instantFromEvent(event, boundary, position2, now);
                        newEvents.add(new Event(CHANGE_DIRECTION, boundary, newSpeed, event.getName(), newEventTime));
                    }
                }
            }
        }
        return new GameEngine(now, newEvents, mapState);
    }

    private Speed turnRight(Speed speed) {
//        System.out.println("Right");
        return new Speed(-speed.getVy(), speed.getVx());
    }

    private Speed turnLeft(Speed speed) {
//        System.out.println("Left");
        return new Speed(speed.getVy(), -speed.getVx());
    }

    private Speed turn180(Speed speed) {
//        System.out.println("180");
        return new Speed(-speed.getVx(), -speed.getVy());
    }

    private UnitSpeed turnRight(UnitSpeed speed) {
//        System.out.println("Right");
        return new UnitSpeed(-speed.getVy(), speed.getVx());
    }

    private UnitSpeed turnLeft(UnitSpeed speed) {
//        System.out.println("Left");
        return new UnitSpeed(speed.getVy(), -speed.getVx());
    }

    private UnitSpeed turn180(UnitSpeed speed) {
//        System.out.println("180");
        return new UnitSpeed(-speed.getVx(), -speed.getVy());
    }

    private boolean isInGhostHome(Position position) {
        float x = position.getX();
        float y = position.getY();
        return ((x >= 11) && (x <= 16) && (y >= 12f) && (y <=15));
    }

    private Instant instantFromEvent(Event event, Position boundary, Position withoutBoundary, Instant now) {
        Duration duration = Duration.between(event.getCreated(), now);

        float partOfTimeStep;
        if (event.getPosition().getX() != boundary.getX()) {
            partOfTimeStep = Math.abs((event.getPosition().getX() - boundary.getX()) / (withoutBoundary.getX() - event.getPosition().getX()));
        } else {
            partOfTimeStep = Math.abs((event.getPosition().getY() - boundary.getY()) / (withoutBoundary.getY() - event.getPosition().getY()));
        }
        return event.getCreated().plusNanos((long) (duration.toNanos() * partOfTimeStep));
    }

    private boolean canGo(UnitPosition unitPosition, UnitSpeed unitSpeed) {
        UnitPosition moved = unitPosition.plus(unitSpeed);
        return !mapState.isWall(moved.getX(), moved.getY());
    }

    private Position calcPosition(Event event, Instant created) {
        return new Position(
            getCoOrd(event.getPosition().getX(), event.getSpeed().getVx(), event.getCreated(), created),
            getCoOrd(event.getPosition().getY(), event.getSpeed().getVy(), event.getCreated(), created)
        );
    }

    private boolean canGo(int x, int y, int xs, int ys) {
        return !mapState.isWall(x + xs, y + ys);
    }

    public List<Drawable> getDrawables(Instant now) {
        List<Event> eventsSoFar = events.stream()
                .filter(event -> event.getCreated().compareTo(now) <= 0)
                .collect(Collectors.toList());

        return NAMES.stream()
                .map((name) -> eventsSoFar.stream()
                        .filter(event -> event.getName().equals(name))
                        .reduce((a, b) -> b)
                        .map((event) -> new Drawable(
                                        calcPosition(event, now),
                                        event.getSpeed(),
                                        event.getName()
                                )
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public MapState getMapState() {
        return mapState;
    }

    public float getCoOrd(float startCoOrd, float v, Instant start, Instant now) {
        return startCoOrd + SPEED * v * 0.001f * (now.toEpochMilli() - start.toEpochMilli());
    }

}
