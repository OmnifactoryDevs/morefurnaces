package dan.morefurnaces.inventory;

import dan.morefurnaces.FurnaceType;
import dan.morefurnaces.tileentity.TileEntityIronFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerIronFurnace extends Container {
    private final FurnaceType type;
    private final TileEntityIronFurnace furnace;
    private int[] lastCookTime;
    private int lastBurnTime = 0;
    private int lastItemBurnTime = 0;

    public ContainerIronFurnace(InventoryPlayer invPlayer, TileEntityIronFurnace invFurnace, FurnaceType type) {
        furnace = invFurnace;
        this.type = type;
        lastCookTime = new int[type.parallelSmelting];

        addFurnaceSlots(invPlayer);
        addPlayerSlots(invPlayer);
    }

    // lets make our GUIs all extremely similar yet totally distinct :megaweary:
    private void addFurnaceSlots(InventoryPlayer inv) {
        ItemHandlerFurnace itemHandler = this.furnace.getItemHandler();
        if (type.parallelSmelting != 1) {
            // OBSIDIAN FURNACE
            int index = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    int x = 56 - 18 * col;
                    int y = 17 + 26 * row; // 26 to create the gap between the inputs
                    this.addSlotToContainer(new SlotItemHandler(itemHandler, index++, x,  y));
                }
            }
            for (int slot = 0; slot < 2; slot++) {
                int x = 56 - 18 * slot;
                int y = 83;
                this.addSlotToContainer(new SlotFuel(itemHandler, index++, x, y));
            }
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    int x = 116 + 22 * col;
                    int y = 18 + (26 * row) + (4 * col);
                    this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, x, y));
                }
            }
        } else if (type.getNumInputSlots() == 4) {
            // SILVER, GOLD FURNACE
            int index = 0;
            for (int slot = 0; slot < type.getNumInputSlots(); slot++) {
                int x = 62 - 18 * slot;
                int y = 17;
                this.addSlotToContainer(new SlotItemHandler(itemHandler, index++, x, y));
            }
            for (int slot = 0; slot < type.getNumFuelSlots(); slot++) {
                int x = 62 - 18 * slot;
                int y = 53;
                this.addSlotToContainer(new SlotFuel(itemHandler, index++, x, y));
            }
            this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, 112, 35));
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    int x = 134 + 18 * col;
                    int y = 26 + 18 * row;
                    this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, x, y));
                }
            }
        } else if (type.getNumFuelSlots() < 3) {
            // IRON, NETHERRACK, COPPER FURNACE
            int index = 0;

            // Input Slots
            for (int slot = 0; slot < type.getNumInputSlots(); slot++) {
                int x = 56 - 18 * slot;
                int y = 17;
                this.addSlotToContainer(new SlotItemHandler(itemHandler, index++, x, y));
            }
            // Fuel Slots
            for (int slot = 0; slot < type.getNumFuelSlots(); slot++) {
                int x = 56 - 18 * slot;
                int y = 53;
                this.addSlotToContainer(new SlotFuel(itemHandler, index++, x, y));
            }
            // Primary Output Slot
            this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, 116, 35));
            if (type.getNumOutputSlots() == 2)
                this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index, 138, 39));
        } else {
            // DIAMOND FURNACE
            int index = 0;
            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 2; row++) {
                    if (col == 0 && row == 1) continue; // Don't add the slot in the top-right position
                    int x = 62 - 18 * col;
                    int y = 35 - 18 * row;
                    this.addSlotToContainer(new SlotItemHandler(itemHandler, index++, x, y));
                }
            }
            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 2; row++) {
                    if (col == 0 && row == 1) continue;
                    int x = 62 - 18 * col;
                    int y = 71 + 18 * row;
                    this.addSlotToContainer(new SlotFuel(itemHandler, index++, x, y));
                }
            }
            this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, 112, 53));
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 4; col++) {
                    int x = 134 + 18 * col;
                    int y = 26 + 18 * row;
                    this.addSlotToContainer(new SlotOutput(inv.player, itemHandler, index++, x, y));
                }
            }
        }
    }

    private void addPlayerSlots(IInventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = row * 18 + type.invStartY;
                this.addSlotToContainer(new Slot(inv, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; row++) {
            int x = 8 + row * 18;
            int y = 58 + type.invStartY;
            this.addSlotToContainer(new Slot(inv, row, x, y));
        }
    }

    public TileEntityIronFurnace getTileEntity() {
        return furnace;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            for (int i = 0; i < type.parallelSmelting; i++) {
                if (lastCookTime[i] != furnace.furnaceCookTime[i]) {
                    listener.sendWindowProperty(this, i, furnace.furnaceCookTime[i]);
                }
            }

            if (lastBurnTime != furnace.furnaceBurnTime) {
                listener.sendWindowProperty(this, type.parallelSmelting, furnace.furnaceBurnTime);
            }

            if (lastItemBurnTime != furnace.currentItemBurnTime) {
                listener.sendWindowProperty(this, type.parallelSmelting + 1, furnace.currentItemBurnTime);
            }
        }

        System.arraycopy(furnace.furnaceCookTime, 0, lastCookTime, 0, type.parallelSmelting);
        lastBurnTime = furnace.furnaceBurnTime;
        lastItemBurnTime = furnace.currentItemBurnTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int j) {
        if (i < type.parallelSmelting) {
            furnace.furnaceCookTime[i] = j;
        }

        if (i == type.parallelSmelting) {
            furnace.furnaceBurnTime = j;
        }

        if (i == type.parallelSmelting + 1) {
            furnace.currentItemBurnTime = j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        BlockPos pos = furnace.getPos();
        return player.world.getTileEntity(pos) == furnace && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    private boolean isOutputSlot(int i) {
        return i >= type.getFirstOutputSlot(0) && i <= type.getLastOutputSlot(type.parallelSmelting - 1);
    }

    private boolean isInputSlot(int i) {
        return i >= type.getFirstInputSlot(0) && i <= type.getLastInputSlot(type.parallelSmelting - 1);
    }

    private boolean isFuelSlot(int i) {
        return i >= type.getFirstFuelSlot() && i <= type.getLastFuelSlot();
    }

    protected boolean transferStackInSlot(Slot slot, int index, ItemStack stack1, ItemStack stack) {
        if (isOutputSlot(index)) {
            if (!this.mergeItemStack(stack1, type.getNumSlots(), type.getNumSlots() + 36, true))
                return true;

            slot.onSlotChange(stack1, stack);
        } else if (!isInputSlot(index) && !isFuelSlot(index)) {
            if (!FurnaceRecipes.instance().getSmeltingResult(stack1).isEmpty()) {
                if (!this.mergeItemStack(stack1, 0, type.getFirstFuelSlot(), false))
                    return true;
            } else if (TileEntityFurnace.isItemFuel(stack1)) {
                if (!this.mergeItemStack(stack1, type.getFirstFuelSlot(), type.getFirstOutputSlot(0), false))
                    return true;
            } else if (index >= type.getNumSlots() && index < type.getNumSlots() + 27) {
                if (!this.mergeItemStack(stack1, type.getNumSlots() + 27, type.getNumSlots() + 36, false))
                    return true;
            } else if (index >= type.getNumSlots() + 27 && index < type.getNumSlots() + 36 && !this.mergeItemStack(stack1, type.getNumSlots(), type.getNumSlots() + 27, false))
                return true;
        } else if (!this.mergeItemStack(stack1, type.getNumSlots(), type.getNumSlots() + 36, false))
            return true;

        return false;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (transferStackInSlot(slot, index, stack1, stack))
                return ItemStack.EMPTY;

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack1);
        }
        return stack;
    }
}
