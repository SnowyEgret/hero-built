package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.pick.Pick;
import ds.plato.select.Selection;

public class BlockPickedRenderer implements ISimpleBlockRenderingHandler {

	static int id = RenderingRegistry.getNextAvailableRenderId();
	private IPick pickManager;
	private ISelect selectionManager;

	public BlockPickedRenderer(ISelect selectionManager, IPick pickManager) {
		this.pickManager = pickManager;
		this.selectionManager = selectionManager;
	}

	@Override
	public int getRenderId() {
		return id;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		Block pickedBlock = null;
		Pick pick = pickManager.getPickAt(x, y, z);
		if (pick == null) {
			// Block has been set to BlockPicked but is not in the pickManager's list of picked blocks.
			// Or block is managed by pickManager of another player
			// This is usually the result of a crash where the selections were not cleared before quitting the game.
			// Render as dirt
			renderer.renderStandardBlock(Blocks.dirt, x, y, z);
			return true;
		} else {
			pickedBlock = pick.block;
		}

		// If the pick is selected we want to render the selection's block, not the BlockSelected. Fix for issue #6.
		if (pickedBlock instanceof BlockSelected) {
			Selection s = selectionManager.selectionAt(x, y, z);
			if (s != null) {
				pickedBlock = s.block;
			}
		}

		renderer.renderStandardBlockWithColorMultiplier(pickedBlock, x, y, z, .7f, .9f, .7f);
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

}
