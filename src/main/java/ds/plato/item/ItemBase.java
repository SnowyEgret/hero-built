package ds.plato.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.util.StringUtils;

public abstract class ItemBase extends Item implements IItem {

	private final String modelPath = "models/" + StringUtils.toCamelCase(getClass());
	private final ResourceLocation modelLocation = new ResourceLocation(Plato.ID, modelPath + ".obj");
	private final ResourceLocation textureLocation = new ResourceLocation(Plato.ID, modelPath + ".png");

	// No PlayerInteractEvent.Action.LEFT_CLICK_AIR in ForgeEventHandler.onPlayerInteractEvent
//	@Override
//	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
//		World w = entityLiving.worldObj;
//		// Select server side call
//		if (w.isRemote) {
//			return true;
//		}
//		MovingObjectPosition cursor = Minecraft.getMinecraft().objectMouseOver;
//		if (cursor.typeOfHit == MovingObjectType.MISS) {
//			IPlayer player = Player.instance((EntityPlayer) entityLiving);
//			ISelect selectionManager = player.getSelectionManager();
//			selectionManager.clearSelections(player);
//		}
//		return true;
//	}

	@Override
	public ResourceLocation getModelResourceLocation() {
		return modelLocation;
	}

	@Override
	public ResourceLocation getTextureResourceLocation() {
		return textureLocation;
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public boolean hasRecipe() {
		return getRecipe() != null;
	}

}
