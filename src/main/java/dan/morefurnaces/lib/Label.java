package dan.morefurnaces.lib;

import net.minecraft.client.renderer.GlStateManager;

public class Label extends Control
{
    protected String text;
    protected int color = 0xff000000;
    protected boolean centered;
    private String[] lines;

    public Label(String text, Anchor anchor, ControlContainer parent)
    {
        super(anchor, parent);
        setText(text);
    }

    public Label setCentered()
    {
        centered = true;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String value)
    {
        text = value;
        if (text != null)
            lines = text.split("\n");
        else
            lines = new String[0];

        int width = 0;
        for (int i = 0; i < lines.length; i++)
        {
            int w = mc.fontRenderer.getStringWidth(lines[i]);
            if (w > width)
                width = w;
        }

        anchor.width(width).height(lines.length * 13 - 4);
        if (getBounds() != null)
            updateBounds();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        for (int i = 0; i < lines.length; i++)
        {
            GlStateManager.color(1f, 1f, 1f, 1f);

            int y = getY() + i * 13;
            if (centered)
            {
                mc.fontRenderer.drawString(lines[i], getX() + (getWidth() - mc.fontRenderer.getStringWidth(text)) / 2, y, color);
            } else
            {
                mc.fontRenderer.drawString(lines[i], getX(), y, color);
            }
        }
    }

    public static class Builder extends ControlBuilder<Label>
    {
        private final String text;

        public Builder(String text, GuiData data, String name, ControlContainer parent)
        {
            super(data, name, parent);
            this.text = text;
        }

        @Override
        protected Label createInstance()
        {
            return new Label(text, anchor, parent);
        }
    }
}
