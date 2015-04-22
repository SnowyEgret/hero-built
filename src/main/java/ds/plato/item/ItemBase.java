package ds.plato.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ds.plato.Plato;
import ds.plato.api.IItem;
import ds.plato.util.StringUtils;

public abstract class ItemBase extends Item implements IItem {

	private final String modelPath = "models/" + StringUtils.toCamelCase(getClass());
	private final ResourceLocation modelLocation = new ResourceLocation(Plato.ID, modelPath + ".obj");
	private final ResourceLocation modelTextureLocation = new ResourceLocation(Plato.ID, modelPath + ".png");
	protected IModelCustom model;

	protected ItemBase() {
		try {
			model = AdvancedModelLoader.loadModel(modelLocation);
		} catch (Exception e) {
			// ClientProxy.setCustomRenderers logs missing model
		}
	}

	@Override
	public int getSpriteNumber() {
		return model == null ? 1 : 0;
	}

	@Override
	public IModelCustom getModel() {
		return model;
	}

	@Override
	public ResourceLocation getTextureResourceLocation() {
		return modelTextureLocation;
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public boolean hasRecipe() {
		return getRecipe() != null;
	}

	@Override
	public abstract void onMouseClickLeft(ItemStack stack, int x, int y, int z, int side);

}
