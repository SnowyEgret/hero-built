package ds.plato.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
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

import ds.plato.Plato;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockPickedModel;
import ds.plato.block.BlockSelected;
import ds.plato.block.BlockSelectedModel;
import ds.plato.gui.Overlay;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.other.SpellTrail;
import ds.plato.item.spell.transform.SpellFill;
import ds.plato.item.staff.IStaff;
import ds.plato.item.staff.Staff;
import ds.plato.network.ClearManagersMessage;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.pick.PickManager;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.player.PlayerProperies;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.select.SelectionManager;
import ds.plato.undo.IUndo;
import ds.plato.undo.UndoManager;
import ds.plato.world.IWorld;
import ds.plato.world.WorldWrapper;

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
		// FIXME We are only getting this event on client side
		IPlayer player = Player.instance(e.player);
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		if (spell != null) {
			BlockPos p = null;
			Pick pick = pickManager.lastPick();
			if (pick != null) {
				p = pick.getPos();
			}
			if (p == null) {
				Selection s = selectionManager.firstSelection();
				if (s != null) {
					p = s.getPos();
				}
			}
			if (p != null) {
				Vec3 d = e.target.hitVec;
				overlay.setDisplacement(p.subtract(new Vec3i(d.xCoord, d.yCoord, d.zCoord)));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e) {

		System.out.println(e);

		IPlayer player = Player.instance(e.entityPlayer);
		// TODO
		// IPlayer player = new PlayerWrapper((e.entityPlayer);
		IWorld world = new WorldWrapper(e.world);
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

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
				select(world, selectionManager, e.pos);
				// TODO Maybe eliminate this method
				// ((IItem) heldItem).onMouseClickLeft(stack, e.pos, e.face);
				e.setCanceled(true);
				return;
			}
			break;
		case RIGHT_CLICK_AIR:
			// Deselect
			pickManager.clearPicks(world);
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
			// TODO We are on client side Must send message here
			IPlayer player = Player.instance();
			IWorld world = player.getWorld();
			ISelect selectionManager = player.getSelectionManager();
			IPick pickManager = player.getPickManager();
			selectionManager.clearSelections(world);
			pickManager.clearPicks(world);
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(PlayerProperies.NAME, new PlayerProperies());
		}
	}

	// Private-------------------------------------------------------------------------------------

	// TODO
	// Copied here from Spell.onMouseClickLeft. Might be simpler this way
	private void select(IWorld w, ISelect selectionManager, BlockPos pos) {

		// Shift replaces the current selections with a region.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			IBlockState firstState = selectionManager.firstSelection().getState();
			// Fix for: MultiPlayer: First selection is not included when shift selecting a region #75
			// In MP selections block was not set fast enough so position was rejected
			// in SelectionManager.select in test for block instanceof BlockSelected
			// resulting in the first selection being left unselected. Only clear if we
			// need to.
			if (selectionManager.size() > 1) {
				selectionManager.clearSelections(w);
			}

			for (Object o : BlockPos.getAllInBox(lastPos, pos)) {
				BlockPos p = (BlockPos) o;
				if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
					// Only select blocks similar to first block
					IBlockState state = w.getActualState(p);
					if (state == firstState) {
						selectionManager.select(w, p);
					}
				} else {
					selectionManager.select(w, p);
				}
			}
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(w, pos);
			} else {
				selectionManager.select(w, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections(w);
		selectionManager.select(w, pos);
	}

}
