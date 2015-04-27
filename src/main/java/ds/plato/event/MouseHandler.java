package ds.plato.event;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.api.IItem;
import ds.plato.api.IPick;
import ds.plato.api.IPlayer;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.core.Player;
import ds.plato.item.spell.transform.SpellFill;

public class MouseHandler {

	private IUndo undoManager;
	private ISelect selectionManager;
	private IPick pickManager;

	public MouseHandler(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		this.undoManager = undoManager;
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent e) {

		// Fix for clicking back to game selects a block. Issue #100
		// TODO Seems not to work in multiplayer
		if (Minecraft.getMinecraft().isGamePaused()) {
			return;
		}

		IPlayer player = Player.getPlayer();
		IWorld w = player.getWorld();
		MovingObjectPosition objectPosition = Minecraft.getMinecraft().objectMouseOver;

		if (objectPosition.typeOfHit == MovingObjectType.MISS) {
		
			if (e.button == 0) {
				selectionManager.clearSelections(w);
			} else if (e.button == 1) {
				//TODO pass w to clearPicks like clearSelections
				pickManager.clearPicks();
			}
			return;
			// Do not cancel event. Fix for Left click stuck in loop when block is broken against sky #60
		}
		
		if (objectPosition.typeOfHit == MovingObjectType.BLOCK) {

			ItemStack stack = player.getHeldItemStack();
			if (stack == null) {
				//Do nothing if player is holding nothing.
				return;
			}
			Item item = stack.getItem();

			if (item instanceof IItem) {

				if (e.button == 0) {
					// Select
					((IItem) item).onMouseClickLeft(stack, objectPosition.getBlockPos(), objectPosition.sideHit);
					e.setCanceled(true);

				} 
				// Picking is handled by onItemUse. Do not cancel event.
				//else if (e.button == 1) {
				//}

			} else if (item instanceof ItemBlock) {
				if (e.button == 1) {
					// Fill the selections with the block in hand
					BlockPos pos = objectPosition.getBlockPos();
					if (selectionManager.isSelected(pos)) {
						Block b = ((ItemBlock) item).getBlock();
						new SpellFill(undoManager, selectionManager, pickManager).invoke(w, new HotbarSlot(b, 0));
						e.setCanceled(true);
					}
				} 
				//else if (e.button == 0) {
				//	System.out.println("[MouseHandler.onMouseEvent] Left mouse button with block in hand");
				//}
			}
		}
	}
}
