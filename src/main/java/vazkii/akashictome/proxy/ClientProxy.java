package vazkii.akashictome.proxy;

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import vazkii.akashictome.client.GuiTome;
import vazkii.akashictome.client.HUDHandler;
import vazkii.akashictome.wiki.IWikiProvider;
import vazkii.akashictome.wiki.WikiHooks;

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

    @Override
    public boolean openWikiPage(World world, Block block, MovingObjectPosition pos) {
        IWikiProvider wiki = WikiHooks.getWikiFor(block);
        String url = wiki.getWikiURL(world, pos);
        if (url != null && !url.isEmpty()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

}
