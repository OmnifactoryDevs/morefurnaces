package dan.morefurnaces.lib;

public class PictureBox extends Control {
    protected final GuiTexture texture;
    protected final String part;

    public PictureBox(GuiTexture texture, String part, Anchor anchor, ControlContainer parent) {
        super(anchor, parent);
        this.texture = texture;
        this.part = part;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        texture.drawPart(part, getX(), getY(), getWidth(), getHeight());
    }

    public static class Builder extends ControlBuilder<PictureBox> {
        private final GuiTexture texture;
        private final String part;

        public Builder(GuiTexture texture, String part, GuiData data, String name, ControlContainer parent) {
            super(data, name, parent);
            this.texture = texture;
            this.part = part;
        }

        @Override
        protected PictureBox createInstance() {
            return new PictureBox(texture, part, anchor, parent);
        }
    }
}
