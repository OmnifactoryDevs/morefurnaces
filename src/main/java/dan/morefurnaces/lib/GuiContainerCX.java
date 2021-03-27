package dan.morefurnaces.lib;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class GuiContainerCX extends GuiContainer implements IGuiCX
{
    private final Screen screen;

    public GuiContainerCX(Screen screen, Container container)
    {
        super(container);

        this.screen = screen;
        this.screen.setGui(this);
    }

    public void setSize(int w, int h)
    {
        xSize = w;
        ySize = h;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);

        screen.updateBounds();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        ScaledResolution resolution = new ScaledResolution(mc);
        int mouseX = Mouse.getX() * resolution.getScaledWidth() / mc.displayWidth;
        int mouseY = resolution.getScaledHeight() - Mouse.getY() * resolution.getScaledHeight() / mc.displayHeight - 1;

        screen.update(mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        screen.drawForeground(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        screen.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        screen.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, button);

        screen.mouseClicked(mouseX, mouseY, button, true);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int button)
    {
        super.mouseReleased(mouseX, mouseY, button);

        screen.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, button, timeSinceLastClick);

        screen.mouseClickMove(mouseX, mouseY, button, timeSinceLastClick);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        screen.onClosed();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return screen.doesPauseGame();
    }

    @Override
    public void renderTheToolTip(@Nonnull ItemStack stack, int x, int y)
    {
        renderToolTip(stack, x, y);
    }

    @Override
    public void drawTheHoveringText(List<String> textLines, int x, int y)
    {
        drawHoveringText(textLines, x, y);
    }

    @Override
    public void drawTheDefaultBackground()
    {
        drawDefaultBackground();
    }

    @Override
    public int getTheWidth()
    {
        return width;
    }

    @Override
    public int getTheHeight()
    {
        return height;
    }
}
