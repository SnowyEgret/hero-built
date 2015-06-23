package org.snowyegret.mojo.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockPickedModel;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.BlockSelectedModel;
import org.snowyegret.mojo.gui.Overlay;
import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.item.spell.other.SpellTrail;
import org.snowyegret.mojo.item.spell.transform.SpellFill;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.network.ClearManagersMessage;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.PlayerProperties;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.world.IWorld;

public class EventHandler {

	private ISpell spell = null;
	private Overlay overlay = new Overlay();

	// http://jabelarminecraft.blogspot.ca/p/minecraft-forge-172-event-handling.html
	// Due to the danger of other mods canceling events you might want to intercept, and also useful in some specific
	// cases, you can force a subscribed method to still get events that have been canceled. This is by using the
	// receiveCanceled=true parameter in the @Subscribe annotation.

	// When the cursor falls on a new block update the overlay so that when it is rendered
	// in onRenderGameOverlayEvent below it will show the distance from the first pick or selection.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {
		if (spell != null) {
			BlockPos pos = null;
			// Pick pick = pickManager.lastPick();
			BlockPos lastPickPos = MoJo.pickInfo.getLastPos();
			if (lastPickPos != null) {
				// pos = pick.getPos();
				pos = lastPickPos;
			}
			if (pos == null) {
				// Selection s = selectionManager.firstSelection();
				BlockPos firstSelectionPos = MoJo.selectionInfo.getFirstPos();
				if (firstSelectionPos != null) {
					// p = s.getPos();
					pos = firstSelectionPos;
				}
			}
			if (pos != null) {
				Vec3 d = e.target.hitVec;
				overlay.setDisplacement(pos.subtract(new Vec3i(d.xCoord, d.yCoord, d.zCoord)));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e) {

		// This is coming in twice, sometimes both on server, both on client, or one on each.
		//System.out.println("world=" + e.world);
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

		IPlayer player = Player.instance(e.entityPlayer);

		// Return if player is holding nothing
		ItemStack stack = player.getHeldItemStack();
		//System.out.println("stack=" + stack);
		if (stack == null) {
			return;
		}

		// Tried fixing Filling selections by right clicking with a plant does nothing (works with FillSpell) #169
		// This has to be here and not in MouseClickMessageHandler, but I can't remember why.
		// Right clicking with a flower results in Action.RIGHT_CLICK_AIR when block is not plantable
		// System.out.println("action=" + e.action);
		Item heldItem = stack.getItem();
		//System.out.println("heldItem=" + heldItem);
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

		IPlayer player = Player.instance((EntityPlayer) e.entity);
		IWorld world = player.getWorld();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		ISpell s = player.getSpell();

		// The player may have changed spells on a staff. Reset picking on the spell.
		if (s == null) {
			spell = null;
			return;
		} else {
			// If the spell has changed reset it.
			if (s != spell) {
				spell = s;
				s.reset(player);
			}
		}

		// Select blocks under foot for SpellTrail
		if (s instanceof SpellTrail && !player.isFlying()) {
			// if (s.isPicking()) {
			if (pickManager.isPicking()) {
				BlockPos pos = player.getPosition();
				Block b = world.getBlock(pos.down());
				// Try second block down when block underneath is air if the player is jumping or stepping on a plant
				if (b == Blocks.air || !b.isNormalCube()) {
					pos = pos.down();
				}
				selectionManager.select(player, pos.down());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		IPlayer player = Player.instance();
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			if (spell != null) {
				overlay.drawSpell(spell, player);
			} else {
				Staff staff = player.getStaff();
				if (staff != null) {
					overlay.drawStaff(staff, player);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		IRegistry r = event.modelRegistry;
		r.putObject(BlockSelected.modelResourceLocation, new BlockSelectedModel());
		r.putObject(BlockPicked.modelResourceLocation, new BlockPickedModel());
	}

	// Clears selections and picks when quitting game
	// http://www.minecraftforge.net/forum/index.php/topic,30987.msg161224.html
	@SubscribeEvent
	public void onGuiIngameMenuQuit(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.gui instanceof GuiIngameMenu && event.button.id == 1) {
			MoJo.network.sendToServer(new ClearManagersMessage());
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(PlayerProperties.NAME, new PlayerProperties());
		}
	}
}
