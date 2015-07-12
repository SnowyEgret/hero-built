package org.snowyegret.mojo.util;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import org.snowyegret.mojo.MoJo;

public class ModelResourceLocations {

	public static ModelResourceLocation get(Class c) {
		if (Item.class.isAssignableFrom(c)) {
			//return new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.nameFor(c), "inventory");
			return new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.underscoreNameFor(c), "inventory");
		} else if (Block.class.isAssignableFrom(c)) {
			//ModelResourceLocation l = new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.nameFor(c));
			ModelResourceLocation l = new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.underscoreNameFor(c));
			return l;
		} else {
			throw new Error("Expected either an item or a block. Got a " + c);
		}
	}

}
