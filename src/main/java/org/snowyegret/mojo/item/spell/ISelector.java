package org.snowyegret.mojo.item.spell;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


@Deprecated
public interface ISelector {

	@Deprecated
	public void select(ItemStack stack, int x, int y, int z, int side);

}