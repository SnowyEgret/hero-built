package ds.plato.event;

import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.api.IPick;
import ds.plato.api.IPlayer;
import ds.plato.api.ISelect;
import ds.plato.api.ISpell;
import ds.plato.core.Player;
import ds.plato.gui.Overlay;
import ds.plato.item.staff.Staff;
import ds.plato.pick.Pick;
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {
		MovingObjectPosition pos = e.target;

		if (spell != null) {
			Point3i p = null;
			Pick pick = pickManager.lastPick();
			if (pick != null) {
				p = pick.point3i();
			}
			if (p == null) {
				Selection s = selectionManager.firstSelection();
				if (s != null) {
					p = s.point3i();
				}
			}
			if (p != null) {
				Vector3d d = new Vector3d();
				d.x = p.x - pos.blockX;
				d.y = p.y - pos.blockY;
				d.z = p.z - pos.blockZ;
				overlay.setDisplacement(d);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e) {
		World w = e.entity.worldObj;
		if (!w.isRemote) {
			return;
		}
		if (e.entity instanceof EntityPlayer) {
			ISpell s = Player.getPlayer().getSpell();
			if (s == null) {
				spell = null;
			} else {
				if (s != spell) {
					spell = s;
					s.reset();
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
}
