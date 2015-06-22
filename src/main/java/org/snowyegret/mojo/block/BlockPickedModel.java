package org.snowyegret.mojo.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockPickedModel implements ISmartBlockModel {

	private IBakedModel model;
	private final int COLOR = new Color(255, 200, 200).getRGB();

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		IBlockState s = ((IExtendedBlockState) state).getValue(BlockPicked.pickedBlockProperty);
		// Fix for Crash with infinite loop at BlockSelected/PickedModel.isAmbientOcclusion #172
		if (s != null && s.getBlock() instanceof BlockPicked) {
			s = null;
		}
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
			quads.add(new BakedQuad(tint(q.getVertexData()), 0, q.getFace()));
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
		vd[3] = COLOR;
		vd[10] = COLOR;
		vd[17] = COLOR;
		vd[24] = COLOR;
		return vd;
	}

}
