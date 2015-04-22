package ds.plato.item.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

//Based on http://greyminecraftcoder.blogspot.com.au/2013/09/custom-item-rendering-using.html
public class SpellRenderer implements IItemRenderer {

	private IModelCustom model;
	private ResourceLocation textureResourceLocation;

	public SpellRenderer(Spell spell) {
		model = spell.getModel();
		textureResourceLocation = spell.getTextureResourceLocation();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		switch (type) {
		case ENTITY:
			return (helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.BLOCK_3D);
		case EQUIPPED:
			return (helper == ItemRendererHelper.BLOCK_3D || helper == ItemRendererHelper.EQUIPPED_BLOCK);
		case EQUIPPED_FIRST_PERSON:
			return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
		case INVENTORY:
			return (helper == ItemRendererHelper.INVENTORY_BLOCK);
		default:
			return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (model != null) {
			GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(textureResourceLocation);
			model.renderAll();
			GL11.glPopMatrix();
		}
	}

}
