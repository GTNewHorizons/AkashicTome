package vazkii.akashictome.client;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import vazkii.akashictome.AkashicTome;
import vazkii.akashictome.ConfigHandler;

public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
        // NO-OP
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class ConfigGui extends GuiConfig {

        public ConfigGui(GuiScreen parentScreen) {
            super(
                    parentScreen,
                    new ConfigElement<>(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL))
                            .getChildElements(),
                    AkashicTome.MOD_ID,
                    false,
                    false,
                    GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
        }

    }

}
