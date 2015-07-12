package org.snowyegret.mojo.item.staff;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class StaffModel implements ISmartItemModel {

	private IBakedModel baseStaffModel;
	private IBakedModel spellModel;
	private final int COLOR = new Color(200, 200, 255).getRGB();

	public StaffModel(IBakedModel model) {
		this.baseStaffModel = model;
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return baseStaffModel.getFaceQuads(side);
	}

	@Override
	public List getGeneralQuads() {
		// List<BakedQuad> combinedQuads = new ArrayList(baseStaffModel.getGeneralQuads());
		// combinedQuads.addAll(spellModel.getGeneralQuads());
		// return combinedQuads;

		List<BakedQuad> quads = new ArrayList<>();
		if (spellModel == null) {
			return baseStaffModel.getGeneralQuads();
		}
		List<BakedQuad> spellQuads = spellModel.getGeneralQuads();
		for (BakedQuad q : spellQuads) {
			// TODO why doesn't this tint?
			// Tint the spell to color of staff
			quads.add(new BakedQuad(tint(q.getVertexData()), 0, q.getFace()));
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseStaffModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseStaffModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return baseStaffModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseStaffModel.getItemCameraTransforms();
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack != null) {
			Item item = stack.getItem();
			Staff staff = (Staff) item;
			Spell spell = staff.getSpell(stack);
			if (spell != null) {
				ModelResourceLocation spellLocation = ModelResourceLocations.get(spell.getClass());
				spellModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager()
						.getModel(spellLocation);
			}
		}
		return this;
	}

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
