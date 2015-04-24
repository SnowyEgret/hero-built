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
import ds.plato.core.HotbarSlot;
import ds.plato.core.Player;
import ds.plato.item.spell.transform.SpellFill;

public class MouseHandler {

	// private long nanoseconds = 0;
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

		// Fix for Clicking back to game selects a block. Issue #100
		// TODO Seems not to work in multiplayer
		if (Minecraft.getMinecraft().isGamePaused()) {
			return;
		}

		IPlayer player = Player.getPlayer();
		MovingObjectPosition objectPosition = Minecraft.getMinecraft().objectMouseOver;
		// System.out.println("[MouseHandler.onMouseEvent] position.typeOfHit=" + position.typeOfHit +
		// ", e.button=" + e.button);

		if (objectPosition.typeOfHit == MovingObjectType.MISS) {
			if (e.button == 0) {
				selectionManager.clearSelections(player.getWorld());

			} else if (e.button == 1) {
				pickManager.clearPicks();
			}
			// Fix for Left click stuck in loop when block is broken against sky #60
			// Do not cancel event

		} else if (objectPosition.typeOfHit == MovingObjectType.BLOCK) {

			ItemStack stack = player.getHeldItemStack();
			if (stack == null) {
				return;
			}
			Item item = stack.getItem();

			// IClickable is either a staff or a spell
			// if (item instanceof ISelector) {
			// ISelector selector = (ISelector) item;
				if (item instanceof IItem) {
					IItem selector = (IItem) item;

				// MouseEvent is being sent twice. Throw out the second.
				// Commented out when switched to onItemUse instead of onMouseClickRight
				// if (e.nanoseconds - nanoseconds < 1000000000) {
				// nanoseconds = 0;
				// e.setCanceled(true);
				// return;
				// } else {
				// nanoseconds = e.nanoseconds;
				// }

				if (e.button == 0) {
					// Select
					selector.onMouseClickLeft(stack, objectPosition.getBlockPos(), objectPosition.sideHit);
					e.setCanceled(true);

				} else if (e.button == 1) {
					// Pick
					// Do not cancel event so that onItemUse is called
					// Block b = world.getBlock(p.blockX, p.blockY, p.blockZ);
					// if (isRightClickable(b)) {
					// }
				}

			} else if (item instanceof ItemBlock) {
				ItemBlock itemBlock = (ItemBlock) item;
				if (e.button == 1) {
					// Fill the selections with the block in hand
					//1.8
					BlockPos pos = objectPosition.getBlockPos();
					if (selectionManager.isSelected(pos)) {
						Block b = itemBlock.getBlock();
						int metadata = stack.getItemDamage();
						new SpellFill(undoManager, selectionManager, pickManager).invoke(player.getWorld(), new HotbarSlot(b,
								metadata, 0));
						e.setCanceled(true);
					}
				} else if (e.button == 0) {
					System.out.println("[MouseHandler.onMouseEvent] Left mouse button with block in hand");
				}
			}
		}
	}

//	private boolean isRightClickable(Block block) {
//		if (block instanceof BlockWorkbench)
//			return false;
//		if (block instanceof BlockContainer)
//			return false;
//		if (block instanceof BlockAnvil)
//			return false;
//		return true;
//	}

}
