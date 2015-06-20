package org.snowyegret.plato.item;

import org.snowyegret.plato.Plato;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.player.Player;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.util.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
