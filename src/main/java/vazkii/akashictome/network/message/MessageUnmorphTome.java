package vazkii.akashictome.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import vazkii.akashictome.MorphingHandler;

public class MessageUnmorphTome implements IMessage, IMessageHandler<MessageUnmorphTome, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageUnmorphTome message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack stack = player.getHeldItem();
        if (MorphingHandler.isMorphedTome(stack)) {
            ItemStack newStack = MorphingHandler.getShiftStackForMod(stack, MorphingHandler.MINECRAFT);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
            player.inventoryContainer.detectAndSendChanges();
        }

        return null;
    }
}
