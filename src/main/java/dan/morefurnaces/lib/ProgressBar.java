package dan.morefurnaces.lib;

public abstract class ProgressBar extends PictureBox {
    protected float progress;

    public ProgressBar(GuiTexture texture, String part, Anchor anchor, ControlContainer parent) {
        super(texture, part, anchor, parent);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public static abstract class Builder<T extends ProgressBar> extends ControlBuilder<T> {
        protected final GuiTexture texture;
        protected final String part;

        public Builder(GuiTexture texture, String part, GuiData data, String name, ControlContainer parent) {
            super(data, name, parent);
            this.texture = texture;
            this.part = part;
        }
    }
}
