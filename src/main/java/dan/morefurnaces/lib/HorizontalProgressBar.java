package dan.morefurnaces.lib;

public class HorizontalProgressBar extends ProgressBar {
    public HorizontalProgressBar(GuiTexture texture, String part, Anchor anchor, ControlContainer parent) {
        super(texture, part, anchor, parent);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        texture.drawPart(part, getX(), getY(), progress, 1f);
    }

    public static class Builder extends ProgressBar.Builder<HorizontalProgressBar> {
        public Builder(GuiTexture texture, String part, GuiData data, String name, ControlContainer parent) {
            super(texture, part, data, name, parent);
        }

        @Override
        protected HorizontalProgressBar createInstance() {
            return new HorizontalProgressBar(texture, part, anchor, parent);
        }
    }
}
