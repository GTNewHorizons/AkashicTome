package vazkii.akashictome;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import vazkii.akashictome.proxy.CommonProxy;

@Mod(
        modid = AkashicTome.MOD_ID,
        name = "Akashic Tome",
        version = AkashicVersion.VERSION,
        dependencies = "after:NotEnoughItems",
        guiFactory = "vazkii.akashictome.client.GuiFactory",
        acceptedMinecraftVersions = "[1.7.10]")
public class AkashicTome {

    public static final String MOD_ID = "akashictome";

    @SidedProxy(
            clientSide = "vazkii.akashictome.proxy.ClientProxy",
            serverSide = "vazkii.akashictome.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        proxy.preInit();
    }

}
