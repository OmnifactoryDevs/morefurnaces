package dan.morefurnaces.lib;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

public class Screen extends ControlContainer<Control> {
    protected IGuiCX gui;

    public Screen(ResourceLocation location) {
        super(location, new Anchor(), null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(0, 0, gui.getTheWidth(), gui.getTheHeight());
    }
}
