package dan.morefurnaces.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotFuel extends SlotItemHandler {
    private final ItemHandlerFurnace inventoryIn;

    public SlotFuel(ItemHandlerFurnace inventoryIn, int slotIndex, int x, int y) {
        super(inventoryIn, slotIndex, x, y);
        this.inventoryIn = inventoryIn;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        inventoryIn.slotChecksEnabled = false;
        boolean allow = super.canTakeStack(playerIn);
        inventoryIn.slotChecksEnabled = true;

        return allow;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        inventoryIn.slotChecksEnabled = false;
        ItemStack stack = super.decrStackSize(amount);
        inventoryIn.slotChecksEnabled = true;

        return stack;
    }

    public boolean isItemValid(ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return SlotFurnaceFuel.isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }
}
