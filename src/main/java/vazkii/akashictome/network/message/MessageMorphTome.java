package vazkii.akashictome.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import vazkii.akashictome.ModItems;
import vazkii.akashictome.MorphingHandler;

public class MessageMorphTome implements IMessage, IMessageHandler<MessageMorphTome, IMessage> {

    public String modid;

    public MessageMorphTome() {}

    public MessageMorphTome(String modid) {
        this.modid = modid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        modid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, modid);
    }

    @Override
    public IMessage onMessage(MessageMorphTome message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack tomeStack = player.getHeldItem();

        boolean hasTome = tomeStack != null && tomeStack.getItem() == ModItems.tome;

        if (!hasTome) return null;

        ItemStack newStack = MorphingHandler.getShiftStackForMod(tomeStack, message.modid);
        player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
        return null;
    }
}
