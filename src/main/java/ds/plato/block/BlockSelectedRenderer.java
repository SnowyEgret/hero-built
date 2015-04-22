package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import ds.plato.api.ISelect;
import ds.plato.select.Selection;

public class BlockSelectedRenderer implements ISimpleBlockRenderingHandler {

	static int id = RenderingRegistry.getNextAvailableRenderId();
	private ISelect selectionManager;

	public BlockSelectedRenderer(ISelect selectionManager) {
		this.selectionManager = selectionManager;
	}

	@Override
	public int getRenderId() {
		return id;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks r) {

		// The type of the block before it was selected is maintained by the selection manager
		Block typeOfBlockBeforeSelection = null;
		Selection sel = selectionManager.selectionAt(x, y, z);
		if (sel != null) {
			typeOfBlockBeforeSelection = sel.block;
		} else {
			// Block has been set to BlockSelected but is not in the selection list.
			// Or block is in selection list of another player
			// This is usually the result of a crash where the selections were not cleared before quitting the game.
			// Render as dirt
			r.renderStandardBlock(Blocks.dirt, x, y, z);
			return true;
		}

		// Renders a standard 6 sided block with the texture of the typeOfBlockBeforeSelection with a different hue.
		r.renderStandardBlockWithColorMultiplier(typeOfBlockBeforeSelection, x, y, z, .9f, .7f, .7f);

		// Seems to be no way to render the block by render type with a color multiplier
		// Tessellator t = Tessellator.instance;
		// t.setBrightness(9999);
		// r.setOverrideBlockTexture(null);
		// r.renderBlockByRenderType(selectedBlock, x, y, z);

		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	// public boolean renderStandardBlockWithColorMultiplier(RenderBlocks renderer, Block block, int x, int y, int z,
	// float r, float g, float b) {
	// renderer.enableAO = false;
	// Tessellator tessellator = Tessellator.instance;
	// boolean flag = false;
	// float f3 = 0.5F;
	// float f4 = 1.0F;
	// float f5 = 0.8F;
	// float f6 = 0.6F;
	// float f7 = f4 * r;
	// float f8 = f4 * g;
	// float f9 = f4 * b;
	// float f10 = f3;
	// float f11 = f5;
	// float f12 = f6;
	// float f13 = f3;
	// float f14 = f5;
	// float f15 = f6;
	// float f16 = f3;
	// float f17 = f5;
	// float f18 = f6;
	//
	// if (block != Blocks.grass) {
	// f10 = f3 * r;
	// f11 = f5 * r;
	// f12 = f6 * r;
	// f13 = f3 * g;
	// f14 = f5 * g;
	// f15 = f6 * g;
	// f16 = f3 * b;
	// f17 = f5 * b;
	// f18 = f6 * b;
	// }
	//
	// int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0)) {
	// tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x, y - 1, z));
	// tessellator.setColorOpaque_F(f10, f13, f16);
	// renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z,
	// renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 0));
	// flag = true;
	// }
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1)) {
	// tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x, y + 1, z));
	// tessellator.setColorOpaque_F(f7, f8, f9);
	// renderer.renderFaceYPos(block, (double) x, (double) y, (double) z,
	// renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 1));
	// flag = true;
	// }
	//
	// IIcon iicon;
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)) {
	// tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x, y, z - 1));
	// tessellator.setColorOpaque_F(f11, f14, f17);
	// iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 2);
	// renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, iicon);
	//
	// if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
	// tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
	// renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, BlockGrass.getIconSideOverlay());
	// }
	//
	// flag = true;
	// }
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)) {
	// tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x, y, z + 1));
	// tessellator.setColorOpaque_F(f11, f14, f17);
	// iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3);
	// renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, iicon);
	//
	// if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
	// tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
	// renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, BlockGrass.getIconSideOverlay());
	// }
	//
	// flag = true;
	// }
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)) {
	// tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x - 1, y, z));
	// tessellator.setColorOpaque_F(f12, f15, f18);
	// iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 4);
	// renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, iicon);
	//
	// if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
	// tessellator.setColorOpaque_F(f12 * r, f15 * g, f18 * b);
	// renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, BlockGrass.getIconSideOverlay());
	// }
	//
	// flag = true;
	// }
	//
	// if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)) {
	// tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(
	// renderer.blockAccess, x + 1, y, z));
	// tessellator.setColorOpaque_F(f12, f15, f18);
	// iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 5);
	// renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, iicon);
	//
	// if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
	// tessellator.setColorOpaque_F(f12 * r, f15 * g, f18 * b);
	// renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, BlockGrass.getIconSideOverlay());
	// }
	//
	// flag = true;
	// }
	//
	// return flag;
	// }

}
