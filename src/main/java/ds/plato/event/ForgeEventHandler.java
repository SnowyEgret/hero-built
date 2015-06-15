package ds.plato.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import ds.plato.Plato;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockPickedModel;
import ds.plato.block.BlockSelected;
import ds.plato.block.BlockSelectedModel;
import ds.plato.gui.Overlay;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.other.SpellTrail;
import ds.plato.item.spell.transform.SpellFill;
import ds.plato.item.staff.IStaff;
import ds.plato.item.staff.Staff;
import ds.plato.network.ClearManagersMessage;
import ds.plato.network.SelectionMessage;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.player.PlayerProperies;
import ds.plato.select.ISelect;
import ds.plato.world.IWorld;

public class ForgeEventHandler {

	private ISpell spell = null;
	private Overlay overlay;

	public ForgeEventHandler(Overlay overlay) {
		this.overlay = overlay;
	}

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
			BlockPos lastPickPos = Plato.pickInfo.getLastPos();
			if (lastPickPos != null) {
				// pos = pick.getPos();
				pos = lastPickPos;
			}
			if (pos == null) {
				// Selection s = selectionManager.firstSelection();
				BlockPos firstSelectionPos = Plato.selectionInfo.getFirstPos();
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

	// World is never remote but we have subscribe on both sides
	// @SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e) {

		IPlayer player = Player.instance(e.entityPlayer);
		IWorld world = player.getWorld();
		ISelect selectionManager = player.getSelectionManager();

		// Return if player is holding nothing
		ItemStack stack = player.getHeldItemStack();
		if (stack == null) {
			return;
		}

		Item heldItem = stack.getItem();

		switch (e.action) {
		// Left click air handled in ItemBase.onEntitySwing
		case LEFT_CLICK_BLOCK:
			// Select
			if (heldItem instanceof IStaff || heldItem instanceof ISpell) {
				// Call private method in this class
				select(world, selectionManager, e.pos, player);
				e.setCanceled(true);
				return;
			}
			break;
		case RIGHT_CLICK_AIR:
			// Deselect
			player.getPickManager().clearPicks(player);
			e.setCanceled(true);
			return;
		case RIGHT_CLICK_BLOCK:
			if (heldItem instanceof ItemBlock && selectionManager.isSelected(e.pos)) {
				// Fill selections
				Block b = ((ItemBlock) heldItem).getBlock();
				int meta = heldItem.getDamage(stack);
				IBlockState state = b.getStateFromMeta(meta);
				new SpellFill().invoke(world, player, state);
				e.setCanceled(true);
				return;
			}
			break;
		default:
			break;
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
				s.reset(world, pickManager);
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
				selectionManager.select(world, pos.down());
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
			Plato.network.sendToServer(new ClearManagersMessage());
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(PlayerProperies.NAME, new PlayerProperies());
		}
	}

	// Private-------------------------------------------------------------------------------------

	// Called by onPlayerInteractEvent on server side only.
	private void select(IWorld w, ISelect selectionManager, BlockPos pos, IPlayer player) {

		Modifiers modifiers = player.getModifiers();

		// Shift replaces the current selections with a region.
		if (modifiers.isPressed(Modifier.SHIFT) && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			IBlockState firstState = selectionManager.firstSelection().getState();
			selectionManager.clearSelections(player);

			Iterable<BlockPos> allInBox = BlockPos.getAllInBox(lastPos, pos);
			ArrayList<BlockPos> positions = Lists.newArrayList();
			for (BlockPos p : allInBox) {
				if (modifiers.isPressed(Modifier.ALT)) {
					IBlockState state = w.getActualState(p);
					if (state == firstState) {
						positions.add(p);
					}
				} else {
					positions.add(p);
				}
			}
			selectionManager.select(player, positions);
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (modifiers.isPressed(Modifier.CTRL)) {
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(player, pos);
			} else {
				selectionManager.select(player, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections(player);
		selectionManager.select(player, pos);
	}
}
