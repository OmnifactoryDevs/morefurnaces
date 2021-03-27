package dan.morefurnaces.proxy;

import dan.morefurnaces.gui.GuiMoreFurnace;
import dan.morefurnaces.inventory.ContainerIronFurnace;
import dan.morefurnaces.tileentity.TileEntityIronFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityIronFurnace) {
            TileEntityIronFurnace furnaceTe = (TileEntityIronFurnace) te;
            return new ContainerIronFurnace(player.inventory, furnaceTe, furnaceTe.getType());
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityIronFurnace) {
            TileEntityIronFurnace furnaceTe = (TileEntityIronFurnace) te;
            return GuiMoreFurnace.GUI.buildGui(player.inventory, furnaceTe);
        }
        return null;
    }
}
