package co.uk.handmadetools;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameState {

    private List<Moveable> moveables = new ArrayList<>();
    private Instant created;

    public GameState() {
        created = Instant.now();
        moveables.add(new Moveable(13.0f, 10.5f, -4.0f, 0.0f, "ghost_1", created));
        moveables.add(new Moveable(11.0f, 13.5f, 0.0f, -3.0f, "ghost_2", created));
        moveables.add(new Moveable(13.0f, 13.5f, 0.0f, 3.0f, "ghost_3", created));
        moveables.add(new Moveable(15.0f, 13.5f, 0.0f, -3.0f, "ghost_4", created));
    }

    public List<Moveable> getMoveables() {
        return moveables;
    }

}
