package dan.morefurnaces.tileentity;

import dan.morefurnaces.Config;
import dan.morefurnaces.FurnaceType;
import dan.morefurnaces.MoreFurnaces;
import dan.morefurnaces.inventory.ItemHandlerFurnace;
import dan.morefurnaces.inventory.ItemHandlerMoveStacks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityIronFurnace extends TileEntity implements ITickable {
    public int[] furnaceCookTime;
    public int furnaceBurnTime = 0;
    public int currentItemBurnTime = 0;

    private final FurnaceType type;
    private byte facing;
    private boolean isActive = false;

    private int ticksSinceSync = 0;

    private boolean updateLight = false;

    private final ItemHandlerFurnace itemHandler;

    public TileEntityIronFurnace() {
        this(FurnaceType.IRON);
    }

    protected TileEntityIronFurnace(FurnaceType type) {
        super();
        this.type = type;
        furnaceCookTime = new int[type.parallelSmelting];
        Arrays.fill(furnaceCookTime, 0);
        itemHandler = new ItemHandlerFurnace(this);
    }

    public ItemHandlerFurnace getItemHandler() {
        return itemHandler;
    }

    public void copyStateFrom(TileEntityIronFurnace furnace) {
        int minParallel = Math.min(type.parallelSmelting, furnace.type.parallelSmelting);

        System.arraycopy(furnace.furnaceCookTime, 0, furnaceCookTime, 0, minParallel);

        furnaceBurnTime = furnace.furnaceBurnTime;
        currentItemBurnTime = furnace.currentItemBurnTime;
        facing = furnace.facing;
        isActive = furnace.isActive;
        world.addBlockEvent(pos, MoreFurnaces.blockFurnaces, 2, (byte) (isActive ? 1 : 0));
    }

    public void copyStateFrom(TileEntityFurnace furnace, byte facing) {
        furnaceCookTime[0] = furnace.getField(2);
        furnaceBurnTime = furnace.getField(0);
        currentItemBurnTime = furnace.getField(1);
        setFacing(facing);
        isActive = furnace.isBurning();
        world.addBlockEvent(pos, MoreFurnaces.blockFurnaces, 2, (byte) (isActive ? 1 : 0));
    }

    public int getSpeed() {
        return Config.getFurnaceSpeed(type);
    }

    public float getConsumptionRate() {
        return Config.getConsumptionRate(type);
    }

    public byte getFacing() {
        return facing;
    }

    public void setFacing(byte value) {
        facing = value;
        world.addBlockEvent(pos, MoreFurnaces.blockFurnaces, 1, facing & 0xFF);
    }

    public boolean isActive() {
        return isActive;
    }

    public FurnaceType getType() {
        return type;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        itemHandler.deserializeNBT(nbtTagCompound);


        furnaceBurnTime = nbtTagCompound.getShort("BurnTime");
        currentItemBurnTime = getBurnTime(itemHandler.getStackInSlot(type.getFirstFuelSlot()));
        NBTTagList cookList = nbtTagCompound.getTagList("CookTimes", 10);
        furnaceCookTime = new int[type.parallelSmelting];
        for (int i = 0; i < cookList.tagCount(); ++i) {
            NBTTagCompound tag = cookList.getCompoundTagAt(i);
            byte cookId = tag.getByte("Id");
            int cookTime = tag.getInteger("Time");
            furnaceCookTime[cookId] = cookTime;

        }
        facing = nbtTagCompound.getByte("facing");
        isActive = nbtTagCompound.getBoolean("isActive");
        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setShort("BurnTime", (short) furnaceBurnTime);
        NBTTagList cookList = new NBTTagList();
        for (int i = 0; i < furnaceCookTime.length; i++) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("Id", (byte) i);
            tag.setInteger("Time", furnaceCookTime[i]);
            cookList.appendTag(tag);
        }

        nbtTagCompound.setTag("CookTimes", cookList);

        nbtTagCompound.setByte("facing", facing);
        nbtTagCompound.setBoolean("isActive", isActive);

        NBTTagCompound nbt = itemHandler.serializeNBT();
        nbtTagCompound.setTag("Items", nbt.getTag("Items"));
        nbtTagCompound.setInteger("Size", nbt.getInteger("Size"));

        return nbtTagCompound;
    }

    public float getCookProgress(int id) {
        return furnaceCookTime[id] / (float) getSpeed();
    }

    @SideOnly(Side.CLIENT)
    public float getBurnTimeRemaining() {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = getSpeed();
        }

        return furnaceBurnTime / (float) currentItemBurnTime;
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    @Override
    public void update() {
        if (++ticksSinceSync % 20 * 4 == 0) {
            world.addBlockEvent(pos, MoreFurnaces.blockFurnaces, 1, facing & 0xFF);
            world.addBlockEvent(pos, MoreFurnaces.blockFurnaces, 2, (byte) (isActive ? 1 : 0));
        }

        boolean wasBurning = this.isBurning();
        boolean dirty = false;

        if (this.isBurning() && type.fuelSlots > 0) {
            --furnaceBurnTime;
        }

        if (updateLight && world != null) {
            world.checkLightFor(EnumSkyBlock.SKY, pos);
            updateLight = false;
        }

        if (!world.isRemote) {
            moveStacks();

            if (furnaceBurnTime == 0 && canSmelt() && type.fuelSlots > 0) {
                dirty |= consumeFuel();
            }

            for (int i = 0; i < type.parallelSmelting; i++) {
                dirty |= progressCooking(i);
            }

            dirty |= updateBlockActiveState(wasBurning);
        }

        if (dirty) {
            this.markDirty();
        }
    }

    /**
     * Checks if any smelt line can smelt an item.
     */
    private boolean canSmelt() {
        for (int i = 0; i < type.parallelSmelting; i++) {
            if (canSmelt(i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the burning state of the furnace has changed. If it has changed update the block.
     *
     * @param wasBurning Whether the furnace was burning in the previous tick.
     * @return True if the state has changed, false if not.
     */
    private boolean updateBlockActiveState(boolean wasBurning) {
        boolean dirty = false;

        if (wasBurning != this.isBurning() && type.fuelSlots > 0) {
            dirty = true;
            isActive = this.isBurning();

            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        } else if (type.fuelSlots == 0) {
            if (isActive != isBurning()) {
                currentItemBurnTime = furnaceBurnTime = 3600;
                dirty = true;
                isActive = this.isBurning();

                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
            }
        }

        return dirty;
    }

    /**
     * Progresses the cook time and smelt item when ready of the smelt line with the given id.
     *
     * @return True if item has been smelted, false if not.
     */
    private boolean progressCooking(int id) {
        if (this.isBurning() && this.canSmelt(id)) {
            ++furnaceCookTime[id];

            if (furnaceCookTime[id] >= getSpeed()) {
                furnaceCookTime[id] = 0;
                this.smeltItem(id);
                return true;

            }
        } else {
            furnaceCookTime[id] = 0;
        }

        return false;
    }

    /**
     * Consumes the fuel in the fuel slot, if there is fuel in it.
     *
     * @return True if the fuel has been consumed, false if not.
     */
    private boolean consumeFuel() {
        int slot = type.getFirstFuelSlot();
        ItemStack stack = itemHandler.getStackInSlot(slot);
        currentItemBurnTime = furnaceBurnTime = getBurnTime(stack);
        if (this.isBurning()) {
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                stack.shrink(1);

                if (stack.isEmpty()) {
                    itemHandler.setStackInSlot(slot, item.getContainerItem(stack));
                }
            }

            return true;
        }

        return false;
    }

    private int getBurnTime(ItemStack stack) {
        return (int) (TileEntityFurnace.getItemBurnTime(stack) / getConsumptionRate());
    }

    private void moveStacks() {
        itemHandler.moveInputStacks();

        for (int id = 0; id < type.parallelSmelting; id++) {
            ItemHandlerMoveStacks outputHandler = itemHandler.getOutputHandlers()[id];

            ItemStack result = ItemStack.EMPTY;
            ItemStack input = itemHandler.getStackInSlot(type.getFirstInputSlot(id));
            if (!input.isEmpty()) {
                result = FurnaceRecipes.instance().getSmeltingResult(input);
            }
            if (!result.isEmpty()) {
                itemHandler.slotChecksEnabled = false;
                ItemStack remainder = outputHandler.insertItem(0, result, true);
                itemHandler.slotChecksEnabled = true;

                if (!remainder.isEmpty()) {
                    outputHandler.moveStacks();
                }
            }
        }

        itemHandler.moveFuelStacks();
    }

    @Override
    public boolean receiveClientEvent(int i, int j) {
        if (world != null && !world.isRemote) return true;
        if (i == 1) {
            facing = (byte) j;
            return true;
        } else if (i == 2) {
            isActive = j == 1;
            if (world != null)
                world.checkLightFor(EnumSkyBlock.BLOCK, pos);
            else
                updateLight = true;
            return true;
        }
        return super.receiveClientEvent(i, j);
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt(int id) {
        int inputIndex = type.getFirstInputSlot(id);
        int outputIndex = type.getFirstOutputSlot(id);

        ItemStack input = itemHandler.getStackInSlot(inputIndex);
        ItemStack output = itemHandler.getStackInSlot(outputIndex);

        if (input.isEmpty()) {
            return false;
        } else {
            ItemStack res = FurnaceRecipes.instance().getSmeltingResult(input);
            if (res.isEmpty())
                return false;
            if (output.isEmpty())
                return true;
            if (!output.isItemEqual(res))
                return false;
            int result = output.getCount() + res.getCount();
            return result <= itemHandler.getSlotLimit(outputIndex) && result <= res.getMaxStackSize();
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    private void smeltItem(int id) {
        if (this.canSmelt(id)) {
            int inputIndex = type.getFirstInputSlot(id);
            int outputIndex = type.getFirstOutputSlot(id);

            ItemStack input = itemHandler.getStackInSlot(inputIndex);
            ItemStack output = itemHandler.getStackInSlot(outputIndex);
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);

            if (output.isEmpty()) {
                itemHandler.setStackInSlot(outputIndex, result.copy());
            } else if (output.isItemEqual(result)) {
                output.grow(result.getCount());
            }

            if (input.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && input.getMetadata() == 1) {
                fillBucketInFuelSlots();
            }

            input.shrink(1);

            if (input.isEmpty()) {
                itemHandler.setStackInSlot(inputIndex, ItemStack.EMPTY);
            }
        }
    }

    private void fillBucketInFuelSlots() {
        int startIndex = type.getFirstFuelSlot();

        for (int i = 0; i < type.getNumFuelSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(startIndex + i);

            if (!stack.isEmpty() && stack.getItem() == Items.BUCKET) {
                itemHandler.setStackInSlot(startIndex + i, new ItemStack(Items.WATER_BUCKET));
                break;
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null)
                return (T) itemHandler;
            else
                return (T) itemHandler.getHandlerForSide(facing);
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}
