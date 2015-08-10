package org.snowyegret.mojo.event;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.snowyegret.mojo.CommonProxy;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockMaquette;
import org.snowyegret.mojo.block.BlockMaquetteTileEntity;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.other.SpellTrail;
import org.snowyegret.mojo.item.spell.transform.SpellFill;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.PlayerProperties;

public class EventHandlerServer {

	// http://jabelarminecraft.blogspot.ca/p/minecraft-forge-172-event-handling.html
	// Due to the danger of other mods canceling events you might want to intercept, and also useful in some specific
	// cases, you can force a subscribed method to still get events that have been canceled. This is by using the
	// receiveCanceled=true parameter in the @Subscribe annotation.

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e) {

		if (e.world.isRemote) {
			return;
		}

		// TODO is this necessary? Remove if this println never comes up
		// if (e.action == null) {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// System.out.println("e.action=" + e.action);
		// e.setCanceled(true);
		// return;
		// }

		Player player = new Player(e.entityPlayer);

		// Return if player is holding nothing
		ItemStack stack = player.getHeldItemStack();
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
				player.getSelectionManager().select(pos.down());
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

	// TODO Not necessary anymore. Tag is deleted with ItemStack
	// @SubscribeEvent
	// public void onItemExpire(ItemExpireEvent event) {
	// ItemStack stack = event.entityItem.getEntityItem();
	// if (stack.getItem() instanceof ItemBlock) {
	// if (((ItemBlock) stack.getItem()).getBlock() instanceof BlockMaquette) {
	// NBTTagCompound tag = stack.getTagCompound();
	// String path = tag.getString(BlockMaquette.KEY_PATH);
	// System.out.println("path=" + path);
	// // TODO delete file
	// // After all this, do we really want to delete the file?
	// // What if the player has died with the only copy of a complex construction in his inventory?
	// // If we allow overwrite of maquette name, the file will be deleted.
	// // Is it really a problem if lots of unused maquette files accumulate?
	// // Every world has its own folder of maquettes
	// }
	// }
	// }

	// Import any maquettes found in imports folder
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World w = event.world;
		if (w.isRemote) {
			return;
		}
		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}
		final Player player = new Player((EntityPlayer) event.entity);

		// Action for each .save file
		Consumer<? super Path> action = new Consumer<Path>() {
			@Override
			public void accept(Path path) {
				if (!Files.isRegularFile(path)) {
					return;
				}
				if (!path.getFileName().toString().endsWith(BlockMaquette.EXTENTION)) {
					System.out.println("Expected extension " + BlockMaquette.EXTENTION + ". path=" + path);
					return;
				}

				// Read tag from file
				NBTTagCompound tag = null;
				try {
					tag = CompressedStreamTools.readCompressed(new FileInputStream(path.toString()));
				} catch (IOException e) {
					System.out.println("Could not read file. e=" + e);
					return;
				}
				System.out.println("tag=" + tag);

				ItemStack stack = new ItemStack(MoJo.blockMaquette);
				// TODO Is this right?
				// See BlockMaquette:getDrops
				NBTTagCompound t = new NBTTagCompound();
				stack.setTagCompound(t);
				t.setTag("BlockEntityTag", tag);

				//stack.setStackDisplayName(path.getFileName().toString());
				stack.setStackDisplayName(tag.getString(BlockMaquetteTileEntity.KEY_NAME));

				boolean stackAdded = player.getPlayer().inventory.addItemStackToInventory(stack);
				if (!stackAdded) {
					System.out.println("No room in player's inventory.");
					return;
				}
				System.out.println("inventory=" + player.getPlayer().inventory);

				// TODO Delete file from import folder
				// try {
				// // Files.move(path, path.resolve(CommonProxy.PATH_SAVES));
				// Files.move(path, Paths.get(CommonProxy.PATH_SAVES.toString(), path.getFileName().toString()));
				// } catch (IOException e) {
				// System.out.println("Could not move file. e=" + e);
				// }
			}
		};

		try {
			Files.walk(CommonProxy.PATH_EXPORT).forEach(action);
		} catch (IOException e) {
			System.out.println("e=" + e);
		}
	}
}
