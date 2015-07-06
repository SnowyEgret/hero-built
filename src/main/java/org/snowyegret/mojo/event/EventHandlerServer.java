package org.snowyegret.mojo.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.other.SpellTrail;
import org.snowyegret.mojo.item.spell.transform.SpellFill;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.PlayerProperties;
import org.snowyegret.mojo.world.IWorld;

public class EventHandlerServer {

	// http://jabelarminecraft.blogspot.ca/p/minecraft-forge-172-event-handling.html
	// Due to the danger of other mods canceling events you might want to intercept, and also useful in some specific
	// cases, you can force a subscribed method to still get events that have been canceled. This is by using the
	// receiveCanceled=true parameter in the @Subscribe annotation.

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e) {

		// This is coming in twice, sometimes both on server, both on client, or one on each.
		// System.out.println("world=" + e.world);
		if (e.world.isRemote) {
			return;
		}

		// TODO is this necessary? Remove if this println never comes up
		if (e.action == null) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("e.action=" + e.action);
			e.setCanceled(true);
			return;
		}

		Player player = new Player(e.entityPlayer);

		// Return if player is holding nothing
		ItemStack stack = player.getHeldItemStack();
		// System.out.println("stack=" + stack);
		if (stack == null) {
			return;
		}

		// Tried fixing Filling selections by right clicking with a plant does nothing (works with FillSpell) #169
		// This has to be here and not in MouseClickMessageHandler, but I can't remember why.
		// Right clicking with a flower results in Action.RIGHT_CLICK_AIR when block is not plantable
		// System.out.println("action=" + e.action);
		Item heldItem = stack.getItem();
		// System.out.println("heldItem=" + heldItem);
		if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
			// System.out.println("heldItem=" + heldItem);
			// When clicking with a flower event comes in twice on client side, so selections are empty.
			if (player.getSelectionManager().isSelected(e.pos)) {
				// System.out.println("heldItem=" + heldItem);
				// if (heldItem instanceof ItemBlock || heldItem instanceof IPlantable) {
				if (heldItem instanceof ItemBlock) {
					// Fill selections
					Block b = ((ItemBlock) heldItem).getBlock();
					// System.out.println("b=" + b);
					int meta = heldItem.getDamage(stack);
					IBlockState state = b.getStateFromMeta(meta);
					new SpellFill().invoke(player, state);
					e.setCanceled(true);
					return;
				}
			}
		}
	}

	// Server side
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e) {

		// If this is run on the server side the overlay does not update while switching slots in GuiStaff
		if (e.entity.worldObj.isRemote) {
			return;
		}

		if (!(e.entity instanceof EntityPlayer)) {
			return;
		}

		Player player = new Player((EntityPlayer) e.entity);
		Spell lastSpell = player.getLastSpell();
		Spell spell = player.getSpell();

		// The player may have changed spells on a staff or may have a spell in hand. Reset picking on the spell.
		if (spell == null) {
			player.setLastSpell(null);
			return;
		} else {
			// If the spell has changed reset it.
			if (!spell.equals(lastSpell)) {
				player.setLastSpell(spell);
				player.resetPicks(spell);
			}
		}

		// Select blocks under foot for SpellTrail
		if (spell instanceof SpellTrail && !player.isFlying()) {
			// if (player.isPicking()) {
			if (player.getPickManager().isPicking()) {
				BlockPos pos = player.getPosition();
				Block b = player.getWorld().getBlock(pos.down());
				// Try second block down when block underneath is air if the player is jumping or stepping on a plant
				if (b == Blocks.air || !b.isNormalCube()) {
					pos = pos.down();
				}
				player.getSelectionManager().select(player, pos.down());
			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(PlayerProperties.NAME, new PlayerProperties(
					(EntityPlayer) event.entity));
		}
	}
}
