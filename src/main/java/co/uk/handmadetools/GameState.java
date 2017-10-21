package co.uk.handmadetools;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameState {

    private List<Moveable> moveables = new ArrayList<>();
    private Instant created;

    public GameState() {
        created = Instant.now();
        moveables.add(new Moveable(13.0f, 10.5f, -1.0f, 0.0f, "ghost_1", created));
    }

    public List<Moveable> getMoveables() {
        return moveables;
    }

}
