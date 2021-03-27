package dan.morefurnaces.gui;

import dan.morefurnaces.FurnaceType;
import dan.morefurnaces.inventory.ContainerIronFurnace;
import dan.morefurnaces.tileentity.TileEntityIronFurnace;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

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
        private static final int fuelX = 176;
        private static final int fuelY = 0;

        // Cook Overlay GUI Location
        private static final int cookWidth = 24;
        private static final int cookHeight = 16;
        private static final int cookX = 176;
        private static final int cookY = 14;

        // BG GUI Location
        private static final int bgWidth = 176;
        private final int bgHeight;
        private static final int bgX = 0;
        private static final int bgY = 0;

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

    //private final HorizontalProgressBar[] cookBars;
    //private final VerticalProgressBar fuelBar;
    private final GUI type;

    public GuiMoreFurnace(GUI type, ContainerIronFurnace invFurnace) {
        super(invFurnace);
        furnace = invFurnace.getTileEntity();
        this.type = type;

        //cookBars = new HorizontalProgressBar[type.mainType.parallelSmelting];
        //for (int i = 0; i < cookBars.length; i++) {
        //    cookBars[i] = window.horizontalBar("cook" + i, type.texture, "cook").add();
        //}
        //fuelBar = window.verticalBar("fuel", type.texture, "fuel").add();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(type.texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();

        //for (int i = 0; i < cookBars.length; i++)
        //    cookBars[i].setProgress(furnace.getCookProgress(i));
        //fuelBar.setProgress(-1f + furnace.getBurnTimeRemaining());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
