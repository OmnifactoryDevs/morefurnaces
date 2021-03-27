package dan.morefurnaces.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.util.Rectangle;

public class Control extends Gui {
    public final Minecraft mc;

    protected final ControlContainer parent;
    protected final Screen screen;

    protected final Anchor anchor;
    private Rectangle bounds;

    private boolean isEnabled = true;
    private boolean isVisible = true;

    public Control(Anchor anchor, ControlContainer parent) {
        this.anchor = anchor;
        this.parent = parent;

        mc = FMLClientHandler.instance().getClient();

        if (this instanceof Screen) {
            screen = (Screen) this;
        } else {
            screen = parent.screen;
        }
    }

    public boolean isEnabled() {
        return isVisible && isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getX() {
        return bounds.getX();
    }

    public int getY() {
        return bounds.getY();
    }

    public int getWidth() {
        return bounds.getWidth();
    }

    public int getHeight() {
        return bounds.getHeight();
    }

    public void updateBounds() {
        bounds = anchor.apply();
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {

    }

    public void drawForeground(int mouseX, int mouseY, float partialTicks) {

    }

    public void update(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button, boolean intoControl) {

    }

    public void mouseReleased(int mouseX, int mouseY, int button) {

    }

    public void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick) {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void onClosed() {

    }

    public boolean isMouseOverControl(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }
}
