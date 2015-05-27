package ds.plato.event;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockPickedModel;
import ds.plato.block.BlockSelected;
import ds.plato.block.BlockSelectedModel;
import ds.plato.gui.Overlay;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.other.SpellTrail;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;

public class ForgeEventHandler {

	private ISpell spell = null;
	private ISelect selectionManager;
	private IPick pickManager;
	private Overlay overlay;

	public ForgeEventHandler(ISelect selectionManager, IPick pickManager, Overlay overlay) {
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
		this.overlay = overlay;
	}

	// When the cursor falls on a new block update the overlay so that when it is rendered
	// in onRenderGameOverlayEvent below it will show the distance from the first pick or selection.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {
		// We are only getting this event on client side
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

	// The player may have changed spells on a staff. Reset picking on the spell and update field spell.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e) {
		World w = e.entity.worldObj;
		if (!w.isRemote) {
			return;
		}
		if (e.entity instanceof EntityPlayer) {
			IPlayer player = Player.getPlayer();

			ISpell s = player.getSpell();
			if (s == null) {
				spell = null;
				return;
			} else {
				// If the spell has changed reset it.
				if (s != spell) {
					spell = s;
					s.reset();
				}
			}

			// Select blocks under foot. 
			if (s instanceof SpellTrail && !player.isFlying()) {
				if (s.isPicking()) {
					BlockPos pos = player.getPosition();
					Block b = player.getWorld().getBlock(pos.down());
					//Try second block down when block underneath is air because the player is jumping or stepping on a plant
					if (b == Blocks.air || !b.isNormalCube()) {
						pos = pos.down();
					}
					Selection sel = selectionManager.select(player.getWorld(), pos.down());
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			if (spell != null) {
				overlay.drawSpell(spell);
			} else {
				IPlayer p = Player.getPlayer();
				Staff staff = p.getStaff();
				if (staff != null) {
					overlay.drawStaff(staff, p.getHeldItemStack());
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

}
