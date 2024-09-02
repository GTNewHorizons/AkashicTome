package vazkii.akashictome.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vazkii.akashictome.interfaces.IVariantHolder;
import vazkii.akashictome.utils.IconHelper;
import vazkii.akashictome.utils.TooltipHandler;

public abstract class ItemMod extends Item implements IVariantHolder {

    public static final List<IVariantHolder> variantHolders = new ArrayList<>();
    private final String[] variants;
    private final String bareName;

    public ItemMod(String name, String... variants) {
        setUnlocalizedName(name);
        if (variants.length > 1) setHasSubtypes(true);

        if (variants.length == 0) variants = new String[] { name };

        bareName = name;
        this.variants = variants;
        variantHolders.add(this);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        GameRegistry.registerItem(this, name);
        return super.setUnlocalizedName(name);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int dmg = par1ItemStack.getItemDamage();
        String[] variants = getVariants();

        String name;
        if (dmg >= variants.length) name = bareName;
        else name = variants[dmg];

        return "item." + getPrefix() + name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iReg) {
        itemIcon = IconHelper.forItem(iReg, this);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < getVariants().length; i++) subItems.add(new ItemStack(itemIn, 1, i));
    }

    @Override
    public String[] getVariants() {
        return variants;
    }

    @Override
    public ItemRenderer getCustomMeshDefinition() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void tooltipIfShift(List<String> tooltip, Runnable r) {
        TooltipHandler.tooltipIfShift(tooltip, r);
    }

    @SideOnly(Side.CLIENT)
    public static void addToTooltip(List<String> tooltip, String s, Object... format) {
        TooltipHandler.addToTooltip(tooltip, s, format);
    }

    @SideOnly(Side.CLIENT)
    public static String local(String s) {
        return TooltipHandler.local(s);
    }
}
