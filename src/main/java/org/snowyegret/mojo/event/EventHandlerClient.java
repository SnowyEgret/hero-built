package org.snowyegret.mojo.event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.vecmath.Point3d;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.snowyegret.geom.surface.Sphere;
import org.snowyegret.mojo.ClientProxy;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockPickedSmartModel;
import org.snowyegret.mojo.block.BlockSaved;
import org.snowyegret.mojo.block.BlockSavedSmartModel;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.BlockSelectedSmartModel;
import org.snowyegret.mojo.block.GeneratedModel;
import org.snowyegret.mojo.gui.Overlay;
import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.gui.SelectionInfo;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.draw.SpellSphere;
import org.snowyegret.mojo.item.spell.other.SpellSave;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.item.staff.StaffAcacia;
import org.snowyegret.mojo.item.staff.StaffBirch;
import org.snowyegret.mojo.item.staff.StaffDraw;
import org.snowyegret.mojo.item.staff.StaffOak;
import org.snowyegret.mojo.item.staff.StaffSelect;
import org.snowyegret.mojo.item.staff.StaffSmartModel;
import org.snowyegret.mojo.item.staff.StaffTransform;
import org.snowyegret.mojo.message.server.ClearManagersMessage;
import org.snowyegret.mojo.message.server.KeyMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class EventHandlerClient {

	// Client side message handlers set these fields
	public static SelectionInfo selectionInfo = new SelectionInfo();
	public static PickInfo pickInfo = new PickInfo();
	public static Overlay overlay = new Overlay();

	// When the cursor falls on a new block update the overlay so that when it is rendered
	// in onRenderGameOverlayEvent below it will show the distance from the first pick or selection.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {
		Spell spell = new Player().getSpell();
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

	// Draws information about the current spell to the overlay
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		Player player = new Player();
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

	// Put ISmartModels on model registery
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		IRegistry r = event.modelRegistry;
		r.putObject(ModelResourceLocations.forClass(BlockSelected.class), new BlockSelectedSmartModel());
		r.putObject(ModelResourceLocations.forClass(BlockPicked.class), new BlockPickedSmartModel());
		r.putObject(ModelResourceLocations.forClass(BlockSaved.class), new BlockSavedSmartModel());

		// Prepared in ClientProxy.registerItemModels
		Object baseModel = event.modelRegistry.getObject(ModelResourceLocations.forClass(Staff.class));

		r.putObject(ModelResourceLocations.forClass(StaffDraw.class), new StaffSmartModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffSelect.class), new StaffSmartModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffTransform.class), new StaffSmartModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffOak.class), new StaffSmartModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffBirch.class), new StaffSmartModel((IBakedModel) baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffAcacia.class), new StaffSmartModel((IBakedModel) baseModel));

		// TODO This is not a smart model
		// Try using a GeneratedModel for a spell
		Sphere sphere = new Sphere(new Point3d(0, 0, 0), new Point3d(6, 0, 0), false);
		IBakedModel model = new GeneratedModel(sphere, Blocks.stone.getDefaultState());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>model=" + model);
		r.putObject(ModelResourceLocations.forClass(SpellSphere.class), model);
	}

	// TODO what if closed with x?
	// Clears selections and picks when quitting game
	// http://www.minecraftforge.net/forum/index.php/topic,30987.msg161224.html
	@SubscribeEvent
	public void onGuiIngameMenuQuit(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.gui instanceof GuiIngameMenu && event.button.id == 1) {
			MoJo.network.sendToServer(new ClearManagersMessage());
		}
	}

}
