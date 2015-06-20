package org.snowyegret.plato.event;

import org.snowyegret.plato.Plato;
import org.snowyegret.plato.item.spell.ISpell;
import org.snowyegret.plato.item.staff.IStaff;
import org.snowyegret.plato.network.MouseClickMessage;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.player.Player;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

		// Orbit if middle mouse button is down
		if (e.button == -1) {
			if (isOrbiting) {
				player.orbitAround(centroid, e.dx, e.dy);
			}
			// Do not cancel
			return;
		}

		// Set orbiting and orbit centroid
		if (e.button == 2) {
			if (e.buttonstate) {
				if (Plato.selectionInfo.getSize() != 0) {
					isOrbiting = true;
					// TODO method on selectionInfo for centroid;
					// BlockPos pos = Plato.selectionInfo.centroid();
					BlockPos pos = Plato.selectionInfo.getFirstPos();
					centroid = new Vec3(pos.getX(), pos.getY(), pos.getZ());
				}
			} else {
				isOrbiting = false;
			}
			e.setCanceled(true);
			return;
		}

		Item heldItem = stack.getItem();
		if (heldItem instanceof IStaff || heldItem instanceof ISpell || cursor.typeOfHit == MovingObjectType.MISS) {
			if (e.buttonstate) {
				if (e.button == 1 && cursor.typeOfHit == MovingObjectType.BLOCK) {
					// Right clicking on a block handled by onItemUse
					return;
				}
				Plato.network.sendToServer(new MouseClickMessage(e.button, cursor.getBlockPos(), cursor.typeOfHit));
				e.setCanceled(true);
				return;
			}
		}
	}

}
