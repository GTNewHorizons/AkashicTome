package vazkii.akashictome.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.registry.GameRegistry;
import vazkii.akashictome.ModItems;
import vazkii.akashictome.MorphingHandler;
import vazkii.akashictome.network.NetworkHandler;

public class CommonProxy {

    public void preInit() {
        ModItems.init();

        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.tome),
                new ItemStack(Items.book),
                new ItemStack(Blocks.bookshelf));

        MinecraftForge.EVENT_BUS.register(MorphingHandler.INSTANCE);
        NetworkHandler.init();
    }

    public void openTomeGUI(EntityPlayer player, ItemStack stack) {
        // NO-OP
    }

}
