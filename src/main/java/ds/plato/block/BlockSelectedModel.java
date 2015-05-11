package ds.plato.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockSelectedModel implements ISmartBlockModel {

	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockSelected");
	private IBakedModel defaultModel;
	private IUnlistedProperty prevBlockProperty;

	public BlockSelectedModel(IBakedModel defaultModel, PropertyPreviousBlock prevBlockProperty) {
		super();
		this.defaultModel = defaultModel;
		this.prevBlockProperty = prevBlockProperty;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		assert IExtendedBlockState.class.isAssignableFrom(state.getClass());
		IBlockState s = ((IExtendedBlockState) state).getValue(prevBlockProperty);
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getModelForState(s);
		
		IFlexibleBakedModel m = (IFlexibleBakedModel)model;

		TextureAtlasSprite t = m.getTexture();
		int[][] td = t.getFrameTextureData(0);
		System.out.println(Arrays.deepToString(td));
		
		td = modifyTextureData(td);
		List l = new ArrayList();
		l.add(td);
		t.setFramesTextureData(l);

		t = m.getTexture();
		td = t.getFrameTextureData(0);
		System.out.println(Arrays.deepToString(td));

		return m;
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getGeneralQuads() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return defaultModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		// TODO Auto-generated method stub
		return null;
	}

	// Private-------------------------------------------------------------

	private int[][] modifyTextureData(int[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = 0;
			}
		}
		return data;
	}

}
