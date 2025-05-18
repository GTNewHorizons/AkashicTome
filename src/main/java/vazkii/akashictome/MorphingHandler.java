package vazkii.akashictome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import vazkii.akashictome.utils.ItemNBTHelper;

public final class MorphingHandler {

    public static final MorphingHandler INSTANCE = new MorphingHandler();

    public static final String MINECRAFT = "minecraft";

    public static final String TAG_MORPHING = "akashictome:is_morphing";
    public static final String TAG_TOME_DATA = "akashictome:data";
    public static final String TAG_TOME_DISPLAY_NAME = "akashictome:displayName";
    public static final String TAG_ITEM_DEFINED_MOD = "akashictome:definedMod";

    @SubscribeEvent
    public void onItemDropped(ItemTossEvent event) {
        if (!event.player.isSneaking()) return;

        EntityItem e = event.entityItem;
        ItemStack stack = e.getEntityItem();
        if (isMorphedTome(stack)) {
            NBTTagCompound morphData = (NBTTagCompound) stack.getTagCompound().getCompoundTag(TAG_TOME_DATA).copy();
            String currentMod = ItemNBTHelper.getString(stack, TAG_ITEM_DEFINED_MOD, getModFromStack(stack));

            ItemStack morph = makeMorphedStack(stack, MINECRAFT, morphData);
            NBTTagCompound newMorphData = morph.getTagCompound().getCompoundTag(TAG_TOME_DATA);
            newMorphData.removeTag(currentMod);

            if (!e.worldObj.isRemote) {
                EntityItem newItem = new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, morph);
                e.worldObj.spawnEntityInWorld(newItem);
            }

            ItemStack original = stack.copy();
            NBTTagCompound tome = original.getTagCompound();
            if (tome == null) {
                tome = new NBTTagCompound();
                original.setTagCompound(tome);
            }

            tome.removeTag("display");
            String displayName = tome.getString(TAG_TOME_DISPLAY_NAME);
            if (!displayName.isEmpty() && !displayName.equals(original.getDisplayName()))
                original.setStackDisplayName(displayName);

            tome.removeTag(TAG_MORPHING);
            tome.removeTag(TAG_TOME_DISPLAY_NAME);
            tome.removeTag(TAG_TOME_DATA);

            e.setEntityItemStack(original);
        }
    }

    public static String getModFromBlock(Block state) {
        String[] mod = state.delegate.name().split(":");
        return getModOrAlias(mod[0]);
    }

    public static String getModFromStack(@Nullable ItemStack stack) {
        if (stack == null) return MINECRAFT;
        String[] mod = stack.getItem().delegate.name().split(":");
        return getModOrAlias(mod[0]);
    }

    public static String getModOrAlias(String mod) {
        return ConfigHandler.aliases.getOrDefault(mod, mod);
    }

    public static boolean doesStackHaveModAttached(ItemStack stack, String mod) {
        if (!stack.hasTagCompound()) return false;

        NBTTagCompound morphData = stack.getTagCompound().getCompoundTag(TAG_TOME_DATA);
        return morphData.hasKey(mod);
    }

    public static ItemStack getShiftStackForMod(ItemStack stack, String mod) {
        if (!stack.hasTagCompound()) return stack;

        String currentMod = getModFromStack(stack);
        if (mod.equals(currentMod)) return stack;

        NBTTagCompound morphData = stack.getTagCompound().getCompoundTag(TAG_TOME_DATA);
        return makeMorphedStack(stack, mod, morphData);
    }

    public static boolean containsItem(ItemStack tome, ItemStack stack) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        if (tome.hasTagCompound()) {
            NBTTagCompound data = tome.getTagCompound().getCompoundTag(MorphingHandler.TAG_TOME_DATA);
            List<String> keys = new ArrayList<>(data.func_150296_c());
            Collections.sort(keys);

            for (String s : keys) {
                NBTTagCompound cmp = data.getCompoundTag(s);
                if (cmp != null) {
                    ItemStack modStack = ItemNBTHelper.loadItemStackFromNBT(cmp);
                    if (modStack != null) {
                        stacks.add(modStack);
                    }
                }
            }
        }
        return stacks.stream().anyMatch(itemStack -> itemStack.isItemEqual(stack));
    }

    public static ItemStack makeMorphedStack(ItemStack currentStack, String targetMod, NBTTagCompound morphData) {
        String currentMod = ItemNBTHelper.getString(currentStack, TAG_ITEM_DEFINED_MOD, getModFromStack(currentStack));

        NBTTagCompound currentCmp = ItemNBTHelper.saveItemStackToNBT(currentStack);
        if (currentCmp.hasKey("tag")) currentCmp.getCompoundTag("tag").removeTag(TAG_TOME_DATA);

        if (!currentMod.equalsIgnoreCase(MINECRAFT) && !currentMod.equalsIgnoreCase(AkashicTome.MOD_ID))
            morphData.setTag(currentMod, currentCmp);

        ItemStack stack;
        if (targetMod.equals(MINECRAFT)) stack = new ItemStack(ModItems.tome);
        else {
            NBTTagCompound targetCmp = morphData.getCompoundTag(targetMod);
            morphData.removeTag(targetMod);

            stack = ItemNBTHelper.loadItemStackFromNBT(targetCmp);
            if (stack == null) stack = new ItemStack(ModItems.tome);
        }

        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound stackCmp = stack.getTagCompound();
        stackCmp.setTag(TAG_TOME_DATA, morphData);
        stackCmp.setBoolean(TAG_MORPHING, true);

        if (stack.getItem() != ModItems.tome) {
            String displayName = stack.getDisplayName();
            if (stackCmp.hasKey(TAG_TOME_DISPLAY_NAME)) displayName = stackCmp.getString(TAG_TOME_DISPLAY_NAME);
            else stackCmp.setString(TAG_TOME_DISPLAY_NAME, displayName);

            stack.setStackDisplayName(
                    EnumChatFormatting.RESET + StatCollector.translateToLocalFormatted(
                            "akashictome.sudo_name",
                            EnumChatFormatting.GREEN + displayName + EnumChatFormatting.RESET));
        }

        stack.stackSize = 1;

        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(currentStack);
        if (!enchantments.isEmpty()) {
            EnchantmentHelper.setEnchantments(enchantments, stack);
            // We can assume that there is a tag compound if the enchantment map is not null
            if (currentStack.getTagCompound().hasKey("RepairCost", Constants.NBT.TAG_INT)) {
                stack.getTagCompound().setInteger("RepairCost", currentStack.getTagCompound().getInteger("RepairCost"));
            }
        }

        return stack;
    }

    private static final Map<String, String> modNames = new HashMap<>();

    static {
        for (Map.Entry<String, ModContainer> modEntry : Loader.instance().getIndexedModList().entrySet())
            modNames.put(modEntry.getKey().toLowerCase(Locale.ENGLISH), modEntry.getValue().getName());
    }

    public static String getModNameForId(String modId) {
        modId = modId.toLowerCase(Locale.ENGLISH);
        return modNames.getOrDefault(modId, modId);
    }

    public static boolean isMorphedTome(ItemStack stack) {
        return isAkashicTome(stack) && stack.getItem() != ModItems.tome;
    }

    public static boolean isAkashicTome(ItemStack stack) {
        if (stack == null) return false;

        if (stack.getItem() == ModItems.tome) return true;

        return stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_MORPHING);
    }

}
