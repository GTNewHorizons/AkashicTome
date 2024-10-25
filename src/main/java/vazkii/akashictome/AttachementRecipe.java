package vazkii.akashictome;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import vazkii.akashictome.utils.ItemNBTHelper;

public class AttachementRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting var1, World var2) {
        boolean foundTool = false;
        boolean foundTarget = false;
        ItemStack tome = null;
        ItemStack tool = null;
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            if (stack != null && MorphingHandler.isAkashicTome(stack)) {
                tome = stack;
            } else if (stack != null) {
                tool = stack;
            }

            if (stack != null) {
                if (isTarget(stack)) {
                    if (foundTarget) return false;
                    foundTarget = true;
                } else if (stack.getItem() == ModItems.tome) {
                    if (foundTool) return false;
                    foundTool = true;
                } else return false;
            }
        }
        if (tome != null && tool != null) {
            String registryName = tool.getItem().delegate.name();
            if (MorphingHandler.containsItem(tome, tool)
                    && !(ConfigHandler.whitelistedDuplicatesItems.contains(registryName + ":" + tool.getItemDamage())
                            || ConfigHandler.whitelistedDuplicatesItems.contains(registryName + ":" + "*"))) {
                foundTarget = false;
            }
        }

        return foundTool && foundTarget;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        ItemStack tool = null;
        ItemStack target = null;

        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == ModItems.tome) tool = stack;
                else target = stack;
            }
        }

        ItemStack copy = tool.copy();
        NBTTagCompound cmp = copy.getTagCompound();
        if (cmp == null) {
            cmp = new NBTTagCompound();
            copy.setTagCompound(cmp);
        }

        if (!cmp.hasKey(MorphingHandler.TAG_TOME_DATA)) cmp.setTag(MorphingHandler.TAG_TOME_DATA, new NBTTagCompound());

        NBTTagCompound morphData = cmp.getCompoundTag(MorphingHandler.TAG_TOME_DATA);
        String mod = MorphingHandler.getModFromStack(target);
        String modClean = mod;
        int iter = 1;

        while (morphData.hasKey(mod)) {
            mod = modClean + iter;
            iter++;
        }

        ItemNBTHelper.setString(target, MorphingHandler.TAG_ITEM_DEFINED_MOD, mod);
        morphData.setTag(mod, ItemNBTHelper.saveItemStackToNBT(target));

        return copy;
    }

    public boolean isTarget(ItemStack stack) {
        if (stack == null || MorphingHandler.isAkashicTome(stack)) return false;

        String mod = MorphingHandler.getModFromStack(stack);
        if (mod.equals(MorphingHandler.MINECRAFT)) return false;

        if (ConfigHandler.allItems) return true;

        if (ConfigHandler.blacklistedMods.contains(mod) || mod.equals(AkashicTome.MOD_ID)) return false;

        String registryName = stack.getItem().delegate.name();

        if (ConfigHandler.blacklistedItems.contains(registryName)
                || ConfigHandler.blacklistedItems.contains(registryName + ":" + stack.getItemDamage())) {
            return false;
        }

        if (ConfigHandler.whitelistedItems.contains(registryName)
                || ConfigHandler.whitelistedItems.contains(registryName + ":" + stack.getItemDamage()))
            return true;

        String itemName = (registryName.toLowerCase() + ":" + stack.getItemDamage());
        for (String s : ConfigHandler.whitelistedNames) if (itemName.contains(s.toLowerCase())) return true;

        return false;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

}
