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

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public boolean hasRecipe() {
		return getRecipe() != null;
	}

}
