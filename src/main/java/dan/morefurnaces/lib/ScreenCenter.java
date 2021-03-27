package dan.morefurnaces.lib;

import dan.morefurnaces.inventory.INamedSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class ScreenCenter extends Screen
{
    protected final ControlContainer<Control> window;

    public ScreenCenter(ResourceLocation location)
    {
        super(location);

        window = container("bg", location).add();
    }

    public void setGui(IGuiCX gui)
    {
        super.setGui(gui);

        if (gui instanceof GuiContainerCX)
        {
            GuiContainerCX guiContainer = (GuiContainerCX)gui;

            for (Slot slot : guiContainer.inventorySlots.inventorySlots)
            {
                if (slot instanceof INamedSlot)
                {
                    data.apply(slot(null, slot)).add();
                }
                else
                {
                    slot(null, slot).left(slot.xPos).top(slot.yPos).add();
                }
            }
        }
    }

    public SlotControl.Builder slot(String name, Slot slot)
    {
        return new SlotControl.Builder(slot, data, name, window);
    }
}
