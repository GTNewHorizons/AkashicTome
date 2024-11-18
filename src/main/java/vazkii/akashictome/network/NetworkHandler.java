package vazkii.akashictome.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import vazkii.akashictome.AkashicTome;
import vazkii.akashictome.network.message.MessageMorphTome;
import vazkii.akashictome.network.message.MessageUnmorphTome;

public class NetworkHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AkashicTome.MOD_ID);

    private static int i = 0;

    public static void init() {
        INSTANCE.registerMessage(MessageMorphTome.class, MessageMorphTome.class, i++, Side.SERVER);
        INSTANCE.registerMessage(MessageUnmorphTome.class, MessageUnmorphTome.class, i++, Side.SERVER);
    }
}
