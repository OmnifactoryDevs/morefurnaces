package dan.morefurnaces;

import dan.morefurnaces.blocks.BlockMoreFurnaces;
import dan.morefurnaces.items.ItemMoreFurnaces;
import dan.morefurnaces.items.ItemUpgrade;
import dan.morefurnaces.proxy.CommonProxy;
import dan.morefurnaces.proxy.GuiProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(   modid = MoreFurnaces.MODID,
        name = MoreFurnaces.NAME,
        version = MoreFurnaces.VERSION,
        acceptedMinecraftVersions = "[1.12,)",
        useMetadata = true)
public class MoreFurnaces {

    public static final String MODID = "morefurnaces";
    public static final String NAME = "GRADLE:MODNAME";
    public static final String VERSION = "GRADLE:VERSION";

    public static BlockMoreFurnaces blockFurnaces;
    private static ItemMoreFurnaces itemBlock;
    public static ItemUpgrade upgrade;

    @SidedProxy(clientSide = "dan.morefurnaces.proxy.ClientProxy", serverSide = "dan.morefurnaces.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static MoreFurnaces instance;

    public MoreFurnaces() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MFLog.init(event.getModLog());
        Config.init(event.getSuggestedConfigurationFile());

        blockFurnaces = new BlockMoreFurnaces();
        itemBlock = (ItemMoreFurnaces) new ItemMoreFurnaces(blockFurnaces).setRegistryName(blockFurnaces.getRegistryName());
        upgrade = new ItemUpgrade();
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(blockFurnaces);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(upgrade, itemBlock);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        proxy.registerRenderInformation();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        for (FurnaceType typ : FurnaceType.values()) {
            GameRegistry.registerTileEntity(typ.clazz, "CubeX2 " + typ.friendlyName);
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }
}
