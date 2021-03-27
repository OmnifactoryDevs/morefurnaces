package dan.morefurnaces.gui;

import dan.morefurnaces.FurnaceType;
import dan.morefurnaces.inventory.ContainerIronFurnace;
import dan.morefurnaces.tileentity.TileEntityIronFurnace;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

// Goodbye CXLib
public class GuiMoreFurnace extends GuiContainer {

    public enum GUI {
        IRON(Textures.IRON, FurnaceType.IRON, 166),
        GOLD(Textures.GOLD, FurnaceType.GOLD, 166),
        DIAMOND(Textures.DIAMOND, FurnaceType.DIAMOND, 202),
        OBSIDIAN(Textures.OBSIDIAN, FurnaceType.OBSIDIAN, 196),
        NETHERRACK(Textures.NETHERRACK, FurnaceType.NETHERRACK, 166),
        COPPER(Textures.COPPER, FurnaceType.COPPER, 166),
        SILVER(Textures.SILVER, FurnaceType.SILVER, 166);

        private final ResourceLocation texture;
        private final FurnaceType mainType;

        // Fuel Overlay GUI Location
        private static final int fuelWidth = 14;
        private static final int fuelHeight = 13;

        // Cook Overlay GUI Location
        private static final int cookWidth = 24;
        private static final int cookHeight = 16;
        private final int cookY = 13;

        // BG GUI Location
        private static final int bgWidth = 176;
        private final int bgHeight;

        // Obsidian special case constants
        private static final int barX = 79;
        private static final int bar0Y = 18;
        private static final int bar1Y = 44;

        GUI(ResourceLocation texture, FurnaceType mainType, int bgHeight) {
            this.texture = texture;
            this.mainType = mainType;
            this.bgHeight = bgHeight;
        }

        protected ContainerIronFurnace makeContainer(InventoryPlayer player, TileEntityIronFurnace furnace) {
            return new ContainerIronFurnace(player, furnace, mainType);
        }

        public static GuiScreen buildGui(InventoryPlayer invPlayer, TileEntityIronFurnace invFurnace) {
            GUI type = values()[invFurnace.getType().ordinal()];
            ContainerIronFurnace container = type.makeContainer(invPlayer, invFurnace);

            return new GuiMoreFurnace(type, container);
        }
    }

    private final TileEntityIronFurnace furnace;

    private final GUI type;

    public GuiMoreFurnace(GUI type, ContainerIronFurnace invFurnace) {
        super(invFurnace);
        furnace = invFurnace.getTileEntity();
        this.type = type;
        xSize = GUI.bgWidth;
        ySize = type.bgHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(type.texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (furnace.isBurning()) {
            int left, top;
            switch(type) {
                case IRON:
                case COPPER:
                case NETHERRACK:
                    left = 57; top = 37; break;
                case SILVER:
                case GOLD:
                    left = 64; top = 37; break;
                case DIAMOND:
                    left = 64; top = 55; break;
                default: // Obsidian, but avoids an "uninitialized" error
                    left = 57; top = 67; break;
            }
            int px = getBurnLeftScaled();
            drawTexturedModalRect(guiLeft + left, guiTop + top + GUI.fuelHeight - px, GUI.bgWidth, GUI.fuelHeight - px, GUI.fuelWidth, px);
        }
        if (furnace.isActive()) {
            if (type == GUI.OBSIDIAN) { // Need to handle 2 progress bars independently
                int pxTop = getCookProgressScaled(0);
                int pxBottom = getCookProgressScaled(1);

                drawTexturedModalRect(guiLeft + GUI.barX, guiTop + GUI.bar0Y, GUI.bgWidth, GUI.fuelHeight, pxTop + 1, GUI.cookHeight);
                drawTexturedModalRect(guiLeft + GUI.barX, guiTop + GUI.bar1Y, GUI.bgWidth, GUI.fuelHeight, pxBottom + 1, GUI.cookHeight);
            } else {
                int left, top;
                switch (type) {
                    case IRON:
                    case COPPER:
                    case NETHERRACK:
                        left = 79; top = 35; break;
                    case SILVER:
                    case GOLD:
                        left = 81; top = 35; break;
                    default: // Diamond
                        left = 81; top = 53; break;
                }
                int px = getCookProgressScaled(0);
                drawTexturedModalRect(guiLeft + left, guiTop + top, GUI.bgWidth, GUI.fuelHeight, px + 1, GUI.cookHeight);
            }
        }
    }

    private int getCookProgressScaled(int id) {
        return (int)(GUI.cookWidth * this.furnace.getCookProgress(id));
    }

    private int getBurnLeftScaled() {
        return (int)(this.furnace.getBurnTimeRemaining() * GUI.fuelHeight);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
