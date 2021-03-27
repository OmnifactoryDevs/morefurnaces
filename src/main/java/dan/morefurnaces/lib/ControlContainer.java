package dan.morefurnaces.lib;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControlContainer<T extends Control> extends Control implements IControlProvider
{
    protected GuiData data;
    private final List<T> children = Lists.newLinkedList();
    private final Map<String, T> childrenMap = Maps.newHashMap();

    public ControlContainer(ResourceLocation location, Anchor anchor, ControlContainer parent)
    {
        super(anchor, parent);

        data = ClientUtil.loadGuiData(location, true);
    }

    public void addChild(T child, String name)
    {
        children.add(child);
        if (name != null)
            childrenMap.put(name, child);
    }

    @Override
    public Control getControl(String name)
    {
        if (childrenMap.containsKey(name))
            return childrenMap.get(name);
        /*if (parent != null && parent != this)
            return parent.getControl(name);*/

        return null;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);

        children.forEach(c -> c.setEnabled(enabled));
    }

    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);

        children.forEach(c -> c.setVisible(visible));
    }

    @Override
    public void updateBounds()
    {
        super.updateBounds();

        children.forEach(T::updateBounds);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        super.draw(mouseX, mouseY, partialTicks);

        children.stream()
                .filter(Control::isVisible)
                .forEach(c -> c.draw(mouseX, mouseY, partialTicks));
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);

        children.stream()
                .filter(Control::isVisible)
                .forEach(c -> c.drawForeground(mouseX, mouseY, partialTicks));
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);

        children.forEach(c -> c.update(mouseX, mouseY));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button, boolean intoControl)
    {
        super.mouseClicked(mouseX, mouseY, button, intoControl);

        Iterator<T> it = children.stream()
                .filter(Control::isEnabled).iterator();

        while (it.hasNext())
        {
            T c = it.next();

            boolean inControl = c.isMouseOverControl(mouseX, mouseY);
            c.mouseClicked(mouseX, mouseY, button, inControl);
            if (inControl)
            {
                controlClicked(c, mouseX, mouseY, button);
            }
        }
    }

    protected void controlClicked(T c, int mouseX, int mouseY, int button)
    {
        if (parent instanceof ControlContainer)
            parent.controlClicked(c, mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        super.mouseReleased(mouseX, mouseY, button);

        children.stream()
                .filter(Control::isEnabled)
                .forEach(c -> c.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, button, timeSinceLastClick);

        children.stream()
                .filter(Control::isEnabled)
                .forEach(c -> c.mouseClickMove(mouseX, mouseY, button, timeSinceLastClick));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        super.keyTyped(typedChar, keyCode);

        children.stream()
                .filter(Control::isEnabled)
                .forEach(c -> c.keyTyped(typedChar, keyCode));
    }

    @Override
    public void onClosed()
    {
        super.onClosed();

        children.forEach(Control::onClosed);
    }

    public <S extends Control> Builder<S> container(String name, ResourceLocation dataLocation)
    {
        return new Builder<>(dataLocation, data, name, this);
    }

    public PictureBox.Builder pictureBox(String name, GuiTexture texture, String part)
    {
        return new PictureBox.Builder(texture, part, data, name, this);
    }

    public HorizontalProgressBar.Builder horizontalBar(String name, GuiTexture texture, String part)
    {
        return new HorizontalProgressBar.Builder(texture, part, data, name, this);
    }

    public VerticalProgressBar.Builder verticalBar(String name, GuiTexture texture, String part)
    {
        return new VerticalProgressBar.Builder(texture, part, data, name, this);
    }

    public Button.Builder button(String name, String text)
    {
        return new Button.Builder(text, data, name, this);
    }

    public Label.Builder label(String name, String text)
    {
        return new Label.Builder(text, data, name, this);
    }

    public static class Builder<S extends Control> extends ControlBuilder<ControlContainer<S>>
    {
        private final ResourceLocation dataLocation;

        public Builder(ResourceLocation dataLocation, GuiData data, String name, ControlContainer parent)
        {
            super(data, name, parent);
            this.dataLocation = dataLocation;
        }

        @Override
        protected ControlContainer<S> createInstance()
        {
            return new ControlContainer<>(dataLocation, anchor, parent);
        }
    }
}
