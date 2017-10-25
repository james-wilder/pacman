package co.uk.handmadetools;

import co.uk.handmadetools.model.Drawable;
import co.uk.handmadetools.model.Event;
import co.uk.handmadetools.model.MapState;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.uk.handmadetools.model.Event.Type.CHANGE_DIRECTION;
import static co.uk.handmadetools.model.Event.Type.CREATED;

public class GameEngine {

    float SPEED = 1.0f;
//    float SPEED = 0.4f;

    private MapState mapState = new MapState(); // TODO: update with events
    private List<Event> events = new ArrayList<>();
    private Instant lastInstant = Instant.now();

    public GameEngine() throws IOException, URISyntaxException {
    }

    public void start() {
        Instant created = Instant.now();
        events.add(new Event(CREATED, 13.5f, 11f, -4.0f, 0.0f, "ghost_1", created));
        events.add(new Event(CREATED, 11.5f, 14f, 0.0f, -3.0f, "ghost_2", created));
        events.add(new Event(CREATED, 13.5f, 14f, 0.0f, 3.0f, "ghost_3", created));
        events.add(new Event(CREATED, 15.5f, 14f, 0.0f, -3.0f, "ghost_4", created));
//        events.add(new Event(CHANGE_DIRECTION, 9.0f, 10.5f, 0.0f, 4.0f, "ghost_1", created.plusSeconds(1)));
        events.add(new Event(CREATED, 13.5f, 23f, 3.0f, 0.0f, "pacman", created));
    }

    private void update(Instant now) {
        List<String> names = events.stream()
                .map(Event::getName)
                .collect(Collectors.toList());

        for (String name : names) {
            Optional<Event> optEvent = events.stream()
                    .filter(event -> event.getName().equals(name))
                    .reduce((a, b) -> b);

            if ("pacman".equals(name)) {

            } else if (optEvent.isPresent()) {
                Event event = optEvent.get();

                Duration duration = Duration.between(event.getCreated(), now);

                // Slight bodge, otherwise we fire repeatedly for each boundary
                if (duration.toMillis() > 100) {
                    float x1 = getCoOrd(event.getX(), event.getXs(), event.getCreated(), lastInstant);
                    float y1 = getCoOrd(event.getY(), event.getYs(), event.getCreated(), lastInstant);
                    float x2 = getCoOrd(event.getX(), event.getXs(), event.getCreated(), now);
                    float y2 = getCoOrd(event.getY(), event.getYs(), event.getCreated(), now);

                    float v = Math.abs(event.getXs()) + Math.abs(event.getYs());

                    boolean goLeft = false;
                    boolean goRight = false;
                    boolean do180 = false;

                    int intXd = (int) Math.signum(event.getXs());
                    int intYd = (int) Math.signum(event.getYs());

                    float xFloor1 = (float) Math.floor(x1);
                    float xFloor2 = (float) Math.floor(x2);
                    float yFloor1 = (float) Math.floor(y1);
                    float yFloor2 = (float) Math.floor(y2);

                    int xGrid = Math.round(x2);
                    int yGrid = Math.round(y2);

                    float boundaryX = 0;    // because stupid IDE warnings
                    float boundaryY = 0;    // because stupid IDE warnings

                    // cross boundary
                    if (isInGhostHome(x1, y1)) {
                        if (intXd == 0) {
                            if ((y1 > 13.5f) && (y2 <= 13.5f)) {
                                boundaryX = x1;
                                boundaryY = 13.5f;
                                if (x1 != 13.5f) {
                                    if ((Math.random() < 0.5f)) {
                                        do180 = true;
                                    } else {
                                        if (x2 < 13.5f) {
                                            goRight = true;
                                        } else {
                                            goLeft = true;
                                        }
                                    }
                                }
                            } else if ((y1 < 14.5f) && (y2 >= 14.5f)) {
                                boundaryX = x1;
                                boundaryY = 14.5f;
                                do180 = true;
                            }
                        } else {
                            if (((x1 <= 13.5f) && (x2 >= 13.5)) ||
                                    ((x2 <= 13.5f) && (x1 >= 13.5))) {
                                boundaryX = 13.5f;
                                boundaryY = y1;
                                if (intXd < 0) {
                                    goRight = true;
                                } else {
                                    goLeft = true;
                                }
                            }
                        }
                    } else if (((Math.round(x1) != event.getX()) || (Math.round(y1) != event.getY())) &&
                            ((xFloor1 != xFloor2) || (yFloor1 != yFloor2))) {
                        // System.out.println(now);

                        // System.out.println("xGrid=" + xGrid);
                        // System.out.println("yGrid=" + yGrid);

                        boundaryX = (xFloor1 != xFloor2) ? xGrid : x1;
                        boundaryY = (yFloor1 != yFloor2) ? yGrid : y1;

                        boolean canLeft = canGo(xGrid, yGrid, intYd, -intXd);
                        boolean canForward = canGo(xGrid, yGrid, intXd, intYd);
                        boolean canRight = canGo(xGrid, yGrid, -intYd, intXd);

                        // System.out.println("canLeft=" + canLeft);
                        // System.out.println("canForward=" + canForward);
                        // System.out.println("canRight=" + canRight);

                        if (!canForward || ((Math.random() < 0.5) && (canLeft || canRight))) {
                            if (!canLeft || (canRight && (Math.random() < 0.5))) {
                                goRight = true;
                            } else {
                                goLeft = true;
                            }
                        }
                    }

                    float newXs;
                    float newYs;
                    if (goLeft || goRight || do180) {
                        if (goRight) {
                            System.out.println("Right");
                            newXs = v * - intYd;
                            newYs = v * intXd;
                        } else if (goLeft) {
                            System.out.println("Left");
                            newXs = v * intYd;
                            newYs = v * - intXd;
                        } else {
                            System.out.println("180");
                            newXs = - event.getXs();
                            newYs = - event.getYs();
                        }

                        System.out.println(String.format("%.2f %.2f %.2f %.2f ", event.getXs(), event.getYs(), newXs, newYs));

                        float partOfTimeStep;
                        if (x2 != x1) {
                            partOfTimeStep = Math.abs((event.getX() - boundaryX) / (x2 - event.getX()));
                        } else {
                            partOfTimeStep = Math.abs((event.getY() - boundaryY) / (y2 - event.getY()));
                        }
                        Instant newEventTime = event.getCreated().plusNanos((long) (duration.toNanos() * partOfTimeStep));
                        events.add(new Event(CHANGE_DIRECTION, boundaryX, boundaryY, newXs, newYs, event.getName(), newEventTime));
                    }
                }
            }
        }
        this.lastInstant = now;
    }

    private boolean isInGhostHome(float x, float y) {
        return ((x >= 11) && (x <= 16) && (y >= 12f) && (y <=15));
//        return false;
    }

    private boolean canGo(int x, int y, int xs, int ys) {
        if (!mapState.isWall(x + xs, y + ys)) {
            return true;
        }
        return false;
//        return mapState.isGhostDoor(x + xs, y + ys) && (ys < 0);
    }

    public List<Drawable> getDrawables(Instant now) {
//        System.out.println("getDrawables");

        // disble for replay
        update(now);
        // added to allow replay
        List<Event> eventsSoFar = events.stream()
                .filter(event -> event.getCreated().compareTo(now) < 0)
                .collect(Collectors.toList());

        List<String> names = eventsSoFar.stream()
                .map(Event::getName)
                .collect(Collectors.toList());

        List<Drawable> drawables = names.stream()
                .map((name) -> eventsSoFar.stream()
                        .filter(event -> event.getName().equals(name))
                        .reduce((a, b) -> b)
                        .map((event) -> new Drawable(
                                        getCoOrd(event.getX(), event.getXs(), event.getCreated(), now),
                                        getCoOrd(event.getY(), event.getYs(), event.getCreated(), now),
                                        event.getXs(),
                                        event.getYs(),
                                        event.getName(),
                                        now
                                )
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return drawables;
    }

    public MapState getMapState() {
        return mapState;
    }

    public float getCoOrd(float startCoOrd, float v, Instant start, Instant now) {
//        return startCoOrd;
        return startCoOrd + SPEED * v * 0.001f * (now.toEpochMilli() - start.toEpochMilli());
    }

}
