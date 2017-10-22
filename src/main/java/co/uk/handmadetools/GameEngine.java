package co.uk.handmadetools;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.uk.handmadetools.Event.Type.CHANGE_DIRECTION;
import static co.uk.handmadetools.Event.Type.CREATED;

public class GameEngine {

    float SPEED = 1.0f;

    private MapState mapState = new MapState(); // TODO: update with events
    private List<Event> events = new ArrayList<>();

    public GameEngine() throws IOException, URISyntaxException {
    }

    public void start() {
        Instant created = Instant.now();
        events.add(new Event(CREATED, 13.5f, 11.0f, -4.0f, 0.0f, "ghost_1", created));
        events.add(new Event(CREATED, 11.0f, 13.5f, 0.0f, -3.0f, "ghost_2", created));
        events.add(new Event(CREATED, 13.0f, 13.5f, 0.0f, 3.0f, "ghost_3", created));
        events.add(new Event(CREATED, 15.0f, 13.5f, 0.0f, -3.0f, "ghost_4", created));
//        events.add(new Event(CHANGE_DIRECTION, 9.0f, 10.5f, 0.0f, 4.0f, "ghost_1", created.plusSeconds(1)));
    }

    private void update(Instant now) {
        List<String> names = events.stream()
                .map(Event::getName)
                .collect(Collectors.toList());

        for (String name : names) {
            Optional<Event> optEvent = events.stream()
                    .filter(event -> event.getName().equals(name))
                    .reduce((a, b) -> b);
            if (optEvent.isPresent()) {
                Event event = optEvent.get();
                float x1 = event.getX();
                float y1 = event.getY();
                float x2 = getCoOrd(event.getX(), event.getXs(), event.getCreated(), now);
                float y2 = getCoOrd(event.getY(), event.getYs(), event.getCreated(), now);

                float v = Math.abs(event.getXs()) + Math.abs(event.getYs());

                // cross x boundary
                float xRound1 = Math.round(x1);
                float xRound2 = Math.round(x2);
                if (xRound1 != xRound2) {
//                    float boundary = (xRound1 + xRound2) / 2.0f;
                    float boundary = Math.round(xRound2);

                    int xGrid = Math.round(boundary);
                    int yGrid = Math.round(y2);

                    int intXd = (int) Math.signum(event.getXs());
                    int intYd = (int) Math.signum(event.getYs());

                    boolean canLeft = canGo(xGrid, yGrid, intYd, - intXd);
                    boolean canForward = canGo(xGrid, yGrid, intXd, intYd);
                    boolean canRight = canGo(xGrid, yGrid, - intYd, intXd);

                    System.out.println("canLeft=" + canLeft);
                    System.out.println("canForward=" + canForward);
                    System.out.println("canRight=" + canRight);

                    float newXs = event.getXs();
                    float newYs = event.getYs();

                    boolean turn = !canForward || ((Math.random() < 0.5) && (canLeft || canRight));
//                    boolean turn = !canForward;
                    if (turn) {
                        if (!canLeft || (canRight && (Math.random() < 0.5))) {
                            System.out.println("Right");
                            newXs = v * -(float)intYd;
                            newYs = v * (float)intXd;
                        } else {
                            System.out.println("Left");
                            newXs = v * (float)intYd;
                            newYs = v * -(float)intXd;
                        }
                    }

                    System.out.println(String.format("%.2f %.2f %.2f %.2f ", event.getXs(), event.getYs(), newXs, newYs));

                    float partOfTimeStep = Math.abs((x1 - boundary) / (x2 - x1));
                    Duration duration = Duration.between(event.getCreated(), now);
                    Instant newEventTime = event.getCreated().plusNanos((long) (duration.toNanos() * partOfTimeStep));

                    events.add(new Event(CHANGE_DIRECTION, boundary, y2, newXs, newYs, event.getName(), newEventTime));
                }

                // cross y boundary
                float yRound1 = Math.round(y1);
                float yRound2 = Math.round(y2);
                if (yRound1 != yRound2) {
//                    float boundary = (yRound1 + yRound2) / 2.0f;
                    float boundary = Math.round(yRound2);

                    int xGrid = Math.round(x2);
                    int yGrid = Math.round(boundary);

                    int intXd = (int) Math.signum(event.getXs());
                    int intYd = (int) Math.signum(event.getYs());

                    boolean canLeft = canGo(xGrid, yGrid, intYd, - intXd);
                    boolean canForward = canGo(xGrid, yGrid, intXd, intYd);
                    boolean canRight = canGo(xGrid, yGrid, - intYd, intXd);

                    System.out.println("canLeft=" + canLeft);
                    System.out.println("canForward=" + canForward);
                    System.out.println("canRight=" + canRight);

                    float newXs = event.getXs();
                    float newYs = event.getYs();

                    boolean turn = !canForward || ((Math.random() < 0.5) && (canLeft || canRight));
//                    boolean turn = !canForward;
                    if (turn) {
                        if (!canLeft || (canRight && (Math.random() < 0.5))) {
                            System.out.println("Right");
                            newXs = v * -(float)intYd;
                            newYs = v * (float)intXd;
                        } else {
                            System.out.println("Left");
                            newXs = v * (float)intYd;
                            newYs = v * -(float)intXd;
                        }
                    }

                    System.out.println(String.format("%.2f %.2f %.2f %.2f ", event.getXs(), event.getYs(), newXs, newYs));

                    float partOfTimeStep = Math.abs((y1 - boundary) / (y2 - y1));
                    Duration duration = Duration.between(event.getCreated(), now);
                    Instant newEventTime = event.getCreated().plusNanos((long) (duration.toNanos() * partOfTimeStep));
                    events.add(new Event(CHANGE_DIRECTION, x2, boundary, newXs, newYs, event.getName(), newEventTime));
                }
            }
        }
    }

    private boolean canGo(int x, int y, int xs, int ys) {
        if (!mapState.isWall(x + xs, y + ys)) {
            return true;
        }
        return mapState.isGhostDoor(x + xs, y + ys) && (ys < 0);
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
