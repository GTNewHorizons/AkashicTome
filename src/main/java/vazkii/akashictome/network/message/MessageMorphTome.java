package vazkii.akashictome.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import vazkii.akashictome.ModItems;
import vazkii.akashictome.MorphingHandler;
import vazkii.akashictome.network.NetworkMessage;

public class MessageMorphTome extends NetworkMessage {

	public String modid;
	
	public MessageMorphTome() { }
	
	public MessageMorphTome(String modid) {
		this.modid = modid;
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer player = context.getServerHandler().playerEntity;
		ItemStack tomeStack = player.getHeldItem();
		
		boolean hasTome = tomeStack != null && tomeStack.getItem() == ModItems.tome;
		
		if(!hasTome)
			return null;
		
		ItemStack newStack = MorphingHandler.getShiftStackForMod(tomeStack, modid);
		player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
		
		return null;
	}
	
}
