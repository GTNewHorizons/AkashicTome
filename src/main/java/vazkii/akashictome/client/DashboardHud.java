package vazkii.akashictome.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.dashboard.api.HudBounds;
import com.gtnewhorizons.dashboard.api.HudElement;
import com.gtnewhorizons.dashboard.api.HudRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vazkii.akashictome.ModItems;

/**
 * Lets players move, resize and hide the book hint in Dashboard's HUD editor. The only class using Dashboard, so only
 * call it while Dashboard is loaded.
 */
@SideOnly(Side.CLIENT)
public final class DashboardHud {

    private static final HudElement BOOK_HINT = new HudElement("akashictome:book_hint", DashboardHud::getBounds);

    private static ItemStack sample;

    private DashboardHud() {}

    public static void register() {
        BOOK_HINT.setPreview(
                (screenWidth, screenHeight) -> HudRegistry.render(
                        BOOK_HINT,
                        () -> HUDHandler.draw(
                                getSample(),
                                getSample().getDisplayName(),
                                getClickLine(),
                                HudRegistry.getResolution())));
        HudRegistry.register(BOOK_HINT);
    }

    /** False means the player hid the hint, so skip drawing it and do not call {@link #endRender()}. */
    static boolean beginBookHint(ScaledResolution resolution) {
        return HudRegistry.beginRender(BOOK_HINT, resolution);
    }

    static void endRender() {
        HudRegistry.endRender();
    }

    /** The book icon and its two lines, as wide as the line under the book name, which is the longest one. */
    private static HudBounds getBounds(int screenWidth, int screenHeight) {
        int width = 25 + Minecraft.getMinecraft().fontRenderer.getStringWidth(getClickLine());
        return new HudBounds(screenWidth / 2 - 17, screenHeight / 2 + 2, width, 22);
    }

    private static String getClickLine() {
        return EnumChatFormatting.GRAY + I18n.format("akashictome.clickMorph");
    }

    /** Made on first use, once the items exist. */
    private static ItemStack getSample() {
        if (sample == null) {
            sample = new ItemStack(ModItems.tome);
        }
        return sample;
    }
}
