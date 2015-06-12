package ds.plato.event;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.SelectionManager;
import ds.plato.world.IWorld;

public class MouseHandler {

	public static boolean isOrbiting;
	private Vec3 centroid;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent e) {

		// Do nothing if returning from a gui
		// TODO Seems not to work in multiplayer
		if (Minecraft.getMinecraft().isGamePaused()) {
			return;
		}

		IPlayer player = Player.instance();
		IWorld world = player.getWorld();
		SelectionManager selectionManager = player.getSelectionManager();
		MovingObjectPosition cursor = Minecraft.getMinecraft().objectMouseOver;
		// Do nothing if player clicks on a mob
		if (cursor.typeOfHit == MovingObjectType.ENTITY) {
			return;
		}

		// Return if player is holding nothing
		ItemStack stack = player.getHeldItemStack();
		if (stack == null) {
			return;
		}
		
		// Fill the selections on mouse click right on a selected block when player has a block in hand
		// Item heldItem = stack.getItem();
		// if (!(heldItem instanceof IItem)) {
		// if (heldItem instanceof ItemBlock) {
		// if (e.button == 1) {
		// BlockPos pos = cursor.getBlockPos();
		// if (selectionManager.isSelected(pos)) {
		//
		// Plato.network.sendToServer(new SpellFillMessage());
		//
		// // Block b = ((ItemBlock) heldItem).getBlock();
		// // int meta = heldItem.getDamage(stack);
		// // IBlockState state = b.getStateFromMeta(meta);
		// // // FIXME not reselecting in MP
		// // new SpellFill(undoManager, selectionManager, pickManager).invoke(world, player, state);
		//
		// e.setCanceled(true);
		// }
		// }
		// return;
		// }
		// }

		// Orbit if middle mouse button is down
		if (e.button == -1) {
			if (isOrbiting) {
				player.orbitAround(centroid, e.dx, e.dy);
			}
			// Do not cancel
			return;
		}

		// Clear selections or picks if player clicks on sky
		// Do not cancel event. Fix for Left click stuck in loop when block is broken against sky #60
		// if (cursor.typeOfHit == MovingObjectType.MISS) {
		// if (e.button == 0) {
		// selectionManager.clearSelections(world);
		// e.setCanceled(true);
		// return;
		// }
		// if (e.button == 1) {
		// pickManager.clearPicks(world);
		// e.setCanceled(true);
		// return;
		// }
		// }

		// Select on mouse click left when player is holding a staff or spell
		// Picking is handled by onItemUse. Do not cancel event on mouse click right.
		// if (e.button == 0) {
		// if (e.buttonstate) {
		// ((IItem) heldItem).onMouseClickLeft(stack, cursor.getBlockPos(), cursor.sideHit);
		// }
		// e.setCanceled(true);
		// return;
		// }

		// Set orbiting and orbit centroid
		if (e.button == 2) {
			if (e.buttonstate) {
				if (selectionManager.size() != 0) {
					isOrbiting = true;
					centroid = selectionManager.getCentroid();
				}
			} else {
				isOrbiting = false;
			}
			e.setCanceled(true);
			return;
		}
	}
}
