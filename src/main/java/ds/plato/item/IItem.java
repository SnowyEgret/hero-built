package ds.plato.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public interface IItem {

	public ResourceLocation getModelResourceLocation();

	public ResourceLocation getTextureResourceLocation();

	public Object[] getRecipe();

	public boolean hasRecipe();

	public void onMouseClickLeft(ItemStack stack, BlockPos pos, EnumFacing sideHit);
}