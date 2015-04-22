package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import org.lwjgl.opengl.GL11;

public class BlockModelRenderer implements ISimpleBlockRenderingHandler {

	static int id = RenderingRegistry.getNextAvailableRenderId();
	private IModelCustom model;

	public BlockModelRenderer() {
		//model = AdvancedModelLoader.loadModel(new ResourceLocation("plato", "models/sphere.obj"));
		//model = AdvancedModelLoader.loadModel(new ResourceLocation("plato", "models/bunny.obj"));
	}

	@Override
	public int getRenderId() {
		return id;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		model.renderAll();
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		Tessellator tes = Tessellator.instance;
		// Call draw() to solve "already rendering" exception. See http://www.minecraftforge.net/forum/index.php?topic=16380.0
		tes.draw();
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .5, z + .5);
		// GL11.glScaled(.5, .5, .5);
		try {
			// TODO get model from blocks tile entity
			model.renderAll();
		} catch (Exception e) {
			System.out.println("[BlockModelRenderer.renderWorldBlock] e=" + e);
			e.printStackTrace();
		}
		GL11.glPopMatrix();
		tes.startDrawingQuads();
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	// TODO move to tile entity
	public void setModel(String modelName) {
		model = AdvancedModelLoader.loadModel(new ResourceLocation("plato", "models/" + modelName + ".obj"));
	}

}
