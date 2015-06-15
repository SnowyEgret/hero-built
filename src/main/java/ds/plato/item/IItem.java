package ds.plato.item;

import net.minecraft.util.ResourceLocation;

public interface IItem {

	public ResourceLocation getModelResourceLocation();

	public ResourceLocation getTextureResourceLocation();

	public Object[] getRecipe();

	public boolean hasRecipe();
}