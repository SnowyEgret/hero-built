package org.snowyegret.mojo.event;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockPickedModel;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.BlockSelectedModel;
import org.snowyegret.mojo.gui.Overlay;
import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.gui.SelectionInfo;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.item.staff.StaffAcacia;
import org.snowyegret.mojo.item.staff.StaffBirch;
import org.snowyegret.mojo.item.staff.StaffDraw;
import org.snowyegret.mojo.item.staff.StaffModel;
import org.snowyegret.mojo.item.staff.StaffOak;
import org.snowyegret.mojo.item.staff.StaffSelect;
import org.snowyegret.mojo.item.staff.StaffTransform;
import org.snowyegret.mojo.network.ClearManagersMessage;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class EventHandlerClient {

	public static SelectionInfo selectionInfo = new SelectionInfo();
	public static PickInfo pickInfo = new PickInfo();
	public static Overlay overlay = new Overlay();

	// When the cursor falls on a new block update the overlay so that when it is rendered
	// in onRenderGameOverlayEvent below it will show the distance from the first pick or selection.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {
		Spell spell = Player.instance().getSpell();
		if (spell != null) {
			BlockPos pos = null;
			BlockPos lastPickPos = pickInfo.getLastPos();
			if (lastPickPos != null) {
				pos = lastPickPos;
			}
			if (pos == null) {
				BlockPos firstSelectionPos = selectionInfo.getFirstPos();
				if (firstSelectionPos != null) {
					pos = firstSelectionPos;
				}
			}
			if (pos != null) {
				BlockPos p = e.target.getBlockPos();
				if (p != null) {
					overlay.setDisplacement(pos.subtract(p));
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		IPlayer player = Player.instance();
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			Spell spell = player.getSpell();
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
		r.putObject(ModelResourceLocations.get(BlockSelected.class), new BlockSelectedModel());
		r.putObject(ModelResourceLocations.get(BlockPicked.class), new BlockPickedModel());

		// Prepared in ClientProxy.registerItemModels
		Object baseModel = event.modelRegistry.getObject(ModelResourceLocations.get(Staff.class));
		r.putObject(ModelResourceLocations.get(StaffDraw.class), new StaffModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.get(StaffSelect.class), new StaffModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.get(StaffTransform.class), new StaffModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.get(StaffOak.class), new StaffModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.get(StaffBirch.class), new StaffModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.get(StaffAcacia.class), new StaffModel((IBakedModel) baseModel));
//		baseModel = event.modelRegistry.getObject(ModelResourceLocations.get(Spell.class));
//		r.putObject(ModelResourceLocations.get(SpellCircle.class), new SpellModel((IBakedModel) baseModel));
	}

	// Clears selections and picks when quitting game
	// http://www.minecraftforge.net/forum/index.php/topic,30987.msg161224.html
	@SubscribeEvent
	public void onGuiIngameMenuQuit(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.gui instanceof GuiIngameMenu && event.button.id == 1) {
			MoJo.network.sendToServer(new ClearManagersMessage());
		}
	}

}
