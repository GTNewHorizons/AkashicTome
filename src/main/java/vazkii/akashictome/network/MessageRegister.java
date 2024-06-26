package vazkii.akashictome.network;

import cpw.mods.fml.relauncher.Side;
import vazkii.akashictome.network.message.MessageMorphTome;
import vazkii.akashictome.network.message.MessageUnmorphTome;

public class MessageRegister {

    public static void init() {
        NetworkHandler.register(MessageMorphTome.class, Side.SERVER);
        NetworkHandler.register(MessageUnmorphTome.class, Side.SERVER);
    }

}
