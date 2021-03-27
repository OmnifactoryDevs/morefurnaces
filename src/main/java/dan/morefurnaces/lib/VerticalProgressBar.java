package dan.morefurnaces.lib;

public class VerticalProgressBar extends ProgressBar
{
    public VerticalProgressBar(GuiTexture texture, String part, Anchor anchor, ControlContainer parent)
    {
        super(texture, part, anchor, parent);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        texture.drawPart(part, getX(), getY(), 1f, progress);
    }

    public static class Builder extends ProgressBar.Builder<VerticalProgressBar>
    {
        public Builder(GuiTexture texture, String part, GuiData data, String name, ControlContainer parent)
        {
            super(texture, part, data, name, parent);
        }

        @Override
        protected VerticalProgressBar createInstance()
        {
            return new VerticalProgressBar(texture, part, anchor, parent);
        }
    }
}
