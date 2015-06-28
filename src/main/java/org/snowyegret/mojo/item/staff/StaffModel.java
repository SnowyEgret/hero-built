package org.snowyegret.mojo.item.staff;

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

	public StaffModel(IBakedModel model) {
		this.baseStaffModel = model;
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return baseStaffModel.getFaceQuads(side);
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> combinedQuads = new ArrayList(baseStaffModel.getGeneralQuads());
		combinedQuads.addAll(spellModel.getGeneralQuads());
		return combinedQuads;
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
			ModelResourceLocation spellLocation = ModelResourceLocations.get(spell.getClass());
			spellModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager()
					.getModel(spellLocation);
		}
		return this;
	}

}
