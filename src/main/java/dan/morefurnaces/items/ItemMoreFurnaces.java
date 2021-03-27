package dan.morefurnaces.items;

import dan.morefurnaces.FurnaceType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMoreFurnaces extends ItemBlock {

    public ItemMoreFurnaces(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "tile." + FurnaceType.values()[stack.getItemDamage()].toString().toLowerCase() + "_furnace";
    }
}
