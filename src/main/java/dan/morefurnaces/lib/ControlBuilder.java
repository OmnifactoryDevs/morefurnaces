package dan.morefurnaces.lib;

public abstract class ControlBuilder<T extends Control> {
    protected final ControlContainer parent;
    protected final Anchor anchor;
    protected final GuiData data;
    private final String name;

    public ControlBuilder(GuiData data, String name, ControlContainer parent) {
        this.parent = parent;
        this.data = data;
        this.name = name;
        if (name != null) {
            anchor = data.apply(name, parent, parent);
        } else {
            anchor = new Anchor();
        }
    }

    public T add() {
        T control = createInstance();
        parent.addChild(control, name);
        return control;
    }

    protected abstract T createInstance();

    public ControlBuilder<T> left(int dist) {
        return left(parent, dist, true);
    }

    public ControlBuilder<T> left(Control c, int dist) {
        return left(c, dist, false);
    }

    public ControlBuilder<T> left(Control c, int dist, boolean sameSide) {
        anchor.left(c, dist, sameSide);
        return this;
    }

    public ControlBuilder<T> right(int dist) {
        return right(parent, dist, true);
    }

    public ControlBuilder<T> right(Control c, int dist, boolean sameSide) {
        anchor.right(c, dist, sameSide);
        return this;
    }

    public ControlBuilder<T> top(int dist) {
        return top(parent, dist, true);
    }

    public ControlBuilder<T> top(Control c, int dist) {
        return top(c, dist, false);
    }

    public ControlBuilder<T> top(Control c, int dist, boolean sameSide) {
        anchor.top(c, dist, sameSide);
        return this;
    }

    public ControlBuilder<T> bottom(int dist) {
        return bottom(parent, dist, true);
    }

    public ControlBuilder<T> bottom(Control c, int dist, boolean sameSide) {
        anchor.bottom(c, dist, sameSide);
        return this;
    }
}
