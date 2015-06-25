package org.snowyegret.mojo.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.util.StringUtils;

public class ModelResourceLocations {

	public static ModelResourceLocation get(Class c) {
		if (Item.class.isAssignableFrom(c)) {
			return new ModelResourceLocation(MoJo.ID + ":" + StringUtils.toCamelCase(c), "inventory");
		} else if (Block.class.isAssignableFrom(c)) {
			ModelResourceLocation l = new ModelResourceLocation(MoJo.ID + ":" + StringUtils.toCamelCase(c));
			return l;
		} else {
			throw new Error("Expected either an item or a block. Got a " + c);
		}
	}

}
