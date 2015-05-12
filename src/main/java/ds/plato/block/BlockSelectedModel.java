package ds.plato.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockSelectedModel implements ISmartBlockModel {

	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockSelected");
	private IBakedModel defaultModel, model;
	private IUnlistedProperty selectedBlockProperty;
	//private final int tint = Color.RED.getRed();
	private final int tint = new Color(255, 200, 200).getRGB();

	public BlockSelectedModel(IBakedModel defaultModel, PropertySelectedBlock selectedBlockProperty) {
		this.defaultModel = defaultModel;
		this.selectedBlockProperty = selectedBlockProperty;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		assert IExtendedBlockState.class.isAssignableFrom(state.getClass());
		IBlockState s = ((IExtendedBlockState) state).getValue(selectedBlockProperty);
		model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(s);
		return this;
	}

	@Override
	public List getFaceQuads(EnumFacing face) {
		List<BakedQuad> quads = new ArrayList<>();
		List<BakedQuad> faceQuads = model.getFaceQuads(face);
		for (BakedQuad q : faceQuads) {
			quads.add(new BakedQuad(tint(q.getVertexData()), 0, face));
		}
		return quads;
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> quads = new ArrayList<>();
		List<BakedQuad> generalQuads = model.getGeneralQuads();
		for (BakedQuad q : generalQuads) {
			quads.add( new BakedQuad(tint(q.getVertexData()), 0, q.getFace()));
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return model.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}

	// Private-----------------------------------------------------

	private int[] tint(int[] vertexData) {
		int[] vd = new int[vertexData.length];
		System.arraycopy(vertexData, 0, vd, 0, vertexData.length);
		vd[3] = tint;
		vd[10] = tint;
		vd[17] = tint;
		vd[24] = tint;
		return vd;
	}

}
