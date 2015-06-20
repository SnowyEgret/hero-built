package org.snowyegret.mojo.item;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.util.StringUtils;

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
