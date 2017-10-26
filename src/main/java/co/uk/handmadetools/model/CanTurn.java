package co.uk.handmadetools.model;

public class CanTurn {

    private final boolean left;
    private final boolean forward;
    private final boolean right;

    public CanTurn(boolean left, boolean forward, boolean right) {
        this.left = left;
        this.forward = forward;
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isRight() {
        return right;
    }

}
