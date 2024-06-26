package vazkii.akashictome.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import vazkii.akashictome.AkashicTome;
import vazkii.akashictome.ModItems;
import vazkii.akashictome.MorphingHandler;
import vazkii.akashictome.network.NetworkMessage;

public class MessageUnmorphTome extends NetworkMessage {

    public MessageUnmorphTome() {}

    @Override
    public IMessage handleMessage(MessageContext context) {
        EntityPlayer player = context.getServerHandler().playerEntity;
        ItemStack stack = player.getHeldItem();
        if (stack != null && MorphingHandler.isAkashicTome(stack) && stack.getItem() != ModItems.tome) {
            ItemStack newStack = MorphingHandler.getShiftStackForMod(stack, MorphingHandler.MINECRAFT);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
            AkashicTome.proxy.updateEquippedItem();
        }

        return null;
    }

}
