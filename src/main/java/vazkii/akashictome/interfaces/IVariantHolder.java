package vazkii.akashictome.interfaces;

import net.minecraft.client.renderer.ItemRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IVariantHolder {

    public String[] getVariants();

    @SideOnly(Side.CLIENT)
    public ItemRenderer getCustomMeshDefinition();

    public default String getUniqueModel() {
        return null;
    }

    public String getModNamespace();

    public default String getPrefix() {
        return getModNamespace() + ":";
    }
}
