package vazkii.akashictome.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import vazkii.akashictome.client.GuiTome;
import vazkii.akashictome.client.HUDHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void updateEquippedItem() {
        Minecraft.getMinecraft().entityRenderer.itemRenderer.resetEquippedProgress();
    }

    @Override
    public void initHUD() {
        MinecraftForge.EVENT_BUS.register(new HUDHandler());
    }

    @Override
    public void openTomeGUI(EntityPlayer player, ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == player) mc.displayGuiScreen(new GuiTome(stack));
    }
}
