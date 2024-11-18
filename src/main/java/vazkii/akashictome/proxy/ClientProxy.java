package vazkii.akashictome.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import vazkii.akashictome.MorphingHandler;
import vazkii.akashictome.client.GuiTome;
import vazkii.akashictome.client.HUDHandler;
import vazkii.akashictome.network.NetworkHandler;
import vazkii.akashictome.network.message.MessageUnmorphTome;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new HUDHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void openTomeGUI(EntityPlayer player, ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == player) mc.displayGuiScreen(new GuiTome(stack));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onMouseInput(MouseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.button != 0 || !mc.thePlayer.isSneaking()) return;
        if (MorphingHandler.isMorphedTome(mc.thePlayer.getHeldItem())) {
            NetworkHandler.INSTANCE.sendToServer(new MessageUnmorphTome());
            event.setCanceled(true);
        }
    }
}
