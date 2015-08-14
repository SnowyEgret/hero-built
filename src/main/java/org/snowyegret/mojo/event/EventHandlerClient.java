package org.snowyegret.mojo.event;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockHighlight;
import org.snowyegret.mojo.block.BlockHighlightSmartModel;
import org.snowyegret.mojo.block.BlockMaquette;
import org.snowyegret.mojo.block.BlockMaquetteSmartModel;
import org.snowyegret.mojo.gui.IOverlayable;
import org.snowyegret.mojo.gui.Overlay;
import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.gui.SelectionInfo;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.item.staff.StaffAcacia;
import org.snowyegret.mojo.item.staff.StaffBirch;
import org.snowyegret.mojo.item.staff.StaffDraw;
import org.snowyegret.mojo.item.staff.StaffOak;
import org.snowyegret.mojo.item.staff.StaffSelect;
import org.snowyegret.mojo.item.staff.StaffSmartModel;
import org.snowyegret.mojo.item.staff.StaffTransform;
import org.snowyegret.mojo.message.server.ClearManagersMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class EventHandlerClient {

	// Client side message handlers set these fields
	public static SelectionInfo selectionInfo = new SelectionInfo();
	public static PickInfo pickInfo = new PickInfo();
	public static Overlay overlay = new Overlay();
	private BlockMaquette blockMaquetteUnderCursor;

	// When the cursor falls on a new block update the overlay so that when it is rendered
	// in onRenderGameOverlayEvent below it will show the distance from the first pick or selection.
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockHightlight(DrawBlockHighlightEvent e) {

		BlockPos cursorPos = e.target.getBlockPos();
		if (cursorPos != null) {
			Block b = Minecraft.getMinecraft().theWorld.getBlockState(cursorPos).getBlock();
			if (b instanceof BlockMaquette) {
				blockMaquetteUnderCursor = (BlockMaquette) b;
			} else {
				blockMaquetteUnderCursor = null;
			}
		}

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
				// BlockPos p = e.target.getBlockPos();
				if (cursorPos != null) {
					overlay.setDisplacement(pos.subtract(cursorPos));
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

			IOverlayable overlayable = player.getHeldOverlayable();
			if (overlayable != null) {
				overlay.draw(overlayable, player);
				return;
			}

			if (blockMaquetteUnderCursor != null) {
				overlay.draw(blockMaquetteUnderCursor, player);
			}
		}
	}

	// Put ISmartModels on model registry
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		IRegistry r = event.modelRegistry;
		r.putObject(ModelResourceLocations.forClass(BlockHighlight.class), new BlockHighlightSmartModel());
		// r.putObject(ModelResourceLocations.forClass(BlockMaquette.class), new BlockMaquetteSmartModel());

		// From MBE04 ModelBakeEventHandler:
		// IBakedModel existingModel = (IBakedModel)object;
		// CamouflageISmartBlockModelFactory customModel = new CamouflageISmartBlockModelFactory(existingModel);
		// event.modelRegistry.putObject(CamouflageISmartBlockModelFactory.modelResourceLocation, customModel);
		// From MBEO4 CamouflageISmartBlockModelFactory:
		// public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
		// "minecraftbyexample:mbe04_block_camouflage");
		r.putObject(new ModelResourceLocation("mojo:block_maquette"), new BlockMaquetteSmartModel());
		// r.putObject(new ModelResourceLocation("mojo:block_maquette", "inventory"), new BlockMaquetteSmartModel());

		// Prepared in ClientProxy.registerItemModels
		IBakedModel baseModel = (IBakedModel) r.getObject(ModelResourceLocations.forClass(Staff.class));

		r.putObject(ModelResourceLocations.forClass(StaffDraw.class), new StaffSmartModel(baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffSelect.class), new StaffSmartModel(baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffTransform.class), new StaffSmartModel(baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffOak.class), new StaffSmartModel(baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffBirch.class), new StaffSmartModel(baseModel));
		r.putObject(ModelResourceLocations.forClass(StaffAcacia.class), new StaffSmartModel(baseModel));

		// TODO This is not a smart model
		// Try using a GeneratedModel for a spell
		// Sphere sphere = new Sphere(new Point3d(0, 0, 0), new Point3d(6, 0, 0), false);
		// IBakedModel model = new GeneratedModel(sphere, Blocks.stone.getDefaultState());
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>model=" + model);
		// r.putObject(ModelResourceLocations.forClass(SpellSphere.class), model);
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
