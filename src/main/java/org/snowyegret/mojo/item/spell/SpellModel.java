package org.snowyegret.mojo.item.spell;

import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

@Deprecated
public class SpellModel implements ISmartItemModel {

	private IBakedModel baseSpellModel;

	public SpellModel(IBakedModel model) {
		this.baseSpellModel = model;
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return baseSpellModel.getFaceQuads(side);
	}

	@Override
	public List getGeneralQuads() {
		return baseSpellModel.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseSpellModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseSpellModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return baseSpellModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseSpellModel.getItemCameraTransforms();
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack != null) {
			Item item = stack.getItem();
			Spell spell = (Spell) item;
		}
		return this;
	}

}
