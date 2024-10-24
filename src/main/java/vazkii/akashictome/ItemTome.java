package vazkii.akashictome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

import cpw.mods.fml.common.registry.GameRegistry;
import vazkii.akashictome.item.ItemMod;
import vazkii.akashictome.utils.ItemNBTHelper;

public class ItemTome extends ItemMod {

    public ItemTome() {
        super("tome");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);

        GameRegistry.addRecipe(new AttachementRecipe());
        RecipeSorter.register("akashictome:attachment", AttachementRecipe.class, RecipeSorter.Category.SHAPELESS, "");
    }

    @Override
    public boolean onItemUse(ItemStack needed, EntityPlayer playerIn, World worldIn, int aX, int aY, int aZ, int facing,
            float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem();
        if (playerIn.isSneaking()) {
            String mod = MorphingHandler.getModFromBlock(worldIn.getBlock(aX, aY, aZ));
            ItemStack newStack = MorphingHandler.getShiftStackForMod(stack, mod);
            if (!ItemStack.areItemStacksEqual(newStack, stack)) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, newStack);
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemIn, World worldIn, EntityPlayer playerIn) {
        ItemStack stack = playerIn.getHeldItem();
        if (itemIn == stack) {
            AkashicTome.proxy.openTomeGUI(playerIn, stack);
            return stack;
        }
        return itemIn;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(MorphingHandler.TAG_TOME_DATA)) return;

        NBTTagCompound data = stack.getTagCompound().getCompoundTag(MorphingHandler.TAG_TOME_DATA);
        if (data.func_150296_c().isEmpty()) return;
        tooltipIfShift(tooltip, () -> {
            List<String> keys = new ArrayList<>(data.func_150296_c());
            Collections.sort(keys);
            String currMod = "";

            for (String s : keys) {
                NBTTagCompound cmp = data.getCompoundTag(s);
                if (cmp != null) {
                    ItemStack modStack = ItemNBTHelper.loadItemStackFromNBT(cmp);
                    if (modStack != null) {
                        String name = modStack.getDisplayName();
                        if (modStack.hasTagCompound()
                                && modStack.getTagCompound().hasKey(MorphingHandler.TAG_TOME_DISPLAY_NAME))
                            name = modStack.getTagCompound().getString(MorphingHandler.TAG_TOME_DISPLAY_NAME);
                        String mod = MorphingHandler.getModFromStack(modStack);

                        if (!currMod.equals(mod))
                            tooltip.add(EnumChatFormatting.AQUA + MorphingHandler.getModNameForId(mod));
                        tooltip.add(" \u2520 " + name);

                        currMod = mod;
                    }
                }
            }
        });
    }

    @Override
    public String getModNamespace() {
        return AkashicTome.MOD_ID;
    }

}
