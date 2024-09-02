package vazkii.akashictome.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import vazkii.akashictome.ModItems;
import vazkii.akashictome.MorphingHandler;
import vazkii.akashictome.utils.ItemNBTHelper;

public class HUDHandler {

    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (event.type != ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        MovingObjectPosition pos = mc.objectMouseOver;
        ScaledResolution res = event.resolution;

        if (pos != null && pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            ItemStack tomeStack = mc.thePlayer.getCurrentEquippedItem();

            boolean hasTome = tomeStack != null && tomeStack.getItem() == ModItems.tome;

            if (!hasTome) return;

            tomeStack = tomeStack.copy();

            Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);

            if (!block.isAir(mc.theWorld, pos.blockX, pos.blockY, pos.blockZ) && !(block instanceof BlockLiquid)) {
                ItemStack drawStack = null;
                String line1 = "";
                String line2 = "";

                String mod = MorphingHandler.getModFromBlock(block);
                ItemStack morphStack = MorphingHandler.getShiftStackForMod(tomeStack, mod);
                if (!ItemStack.areItemStacksEqual(morphStack, tomeStack)) {
                    drawStack = morphStack;
                    line1 = ItemNBTHelper.getString(morphStack, MorphingHandler.TAG_TOME_DISPLAY_NAME, "N/A");
                    line2 = EnumChatFormatting.GRAY + I18n.format("akashictome.clickMorph");
                }

                if (drawStack != null) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    int sx = res.getScaledWidth() / 2 - 17;
                    int sy = res.getScaledHeight() / 2 + 2;
                    RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, drawStack, sx, sy);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    mc.fontRenderer.drawStringWithShadow(line1, sx + 20, sy + 4, 0xFFFFFFFF);
                    mc.fontRenderer.drawStringWithShadow(line2, sx + 25, sy + 14, 0xFFFFFFFF);
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                }
            }
        }
    }

}
