package ds.plato.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IItem {

	public IModelCustom getModel();

	public ResourceLocation getTextureResourceLocation();

	public Object[] getRecipe();

	public boolean hasRecipe();

	public void onMouseClickLeft(ItemStack stack, int x, int y, int z, int side);
}