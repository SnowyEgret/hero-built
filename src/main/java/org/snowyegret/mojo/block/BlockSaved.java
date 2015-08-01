package org.snowyegret.mojo.block;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.snowyegret.mojo.CommonProxy;
import org.snowyegret.mojo.item.spell.other.SpellSave;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class BlockSaved extends Block implements ITileEntityProvider {

	// public static final PropertyPath PROPERTY_PATH = new PropertyPath();
	public static final String KEY_PATH = "path";

	public BlockSaved() {
		super(Material.clay);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BlockSavedTileEntity();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return CommonProxy.tabSpells;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		// System.out.println("world=" + world);
		if (world.isRemote) {
			return;
		}
		if (stack == null) {
			System.out.println("stack=" + stack);
			return;
		}
		NBTTagCompound t = stack.getTagCompound();
		if (t == null) {
			System.out.println("tag=" + t);
			return;
		}
		String path = t.getString(KEY_PATH);
		System.out.println("path=" + path);

		NBTTagCompound tag = null;
		try {
			tag = CompressedStreamTools.readCompressed(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// System.out.println("tag=" + tag);

		int size = tag.getInteger(SpellSave.KEY_SIZE);
		BlockPos origin = BlockPos.fromLong(tag.getLong(SpellSave.KEY_ORIGIN));
		origin = pos.subtract(origin);
		List<Selection> selections = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			NBTTagCompound tt = tag.getCompoundTag(String.valueOf(i));
			selections.add(Selection.fromNBT(tt));
		}

		List<IUndoable> undoables = Lists.newArrayList();
		Player player = new Player((EntityPlayer) placer);
		IWorld w = player.getWorld();
		for (Selection s : selections) {
			BlockPos p = s.getPos().add(origin);
			undoables.add(new UndoableSetBlock(p, w.getState(p), s.getState()));
		}
		player.getTransactionManager().doTransaction(undoables);
	}

	// If this returns null, super.getDrops in getDrops with get an empty list.
	// http://www.minecraftforge.net/forum/index.php/topic,32550.msg170136.html#msg170136
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	// These three methods (getDrops, removedByPlayer, harvestBlock) delay deletion of tile
	// entity until block is picked up.
	// http://www.minecraftforge.net/forum/index.php/topic,32477.msg169713.html
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> itemStacks = super.getDrops(world, pos, state, fortune);
		TileEntity te = world.getTileEntity(pos);
		if (te == null) {
			System.out.println("Could not write path to tag. te=" + te);
			return itemStacks;
		}
		if (!(te instanceof BlockSavedTileEntity)) {
			System.out.println("Could not write path to tag. TileEntity not a BlockSavedTileEntity. te=" + te);
			return itemStacks;
		}

		// Write BlockSavedTileEntity#path to the stack's tag
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		((BlockSavedTileEntity) te).writeToNBT(tag);
		itemStacks.add(stack);
		return itemStacks;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			// Delay deletion of the block until after getDrops
			return true;
		}
		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		super.harvestBlock(world, player, pos, state, te);
		world.setBlockToAir(pos);
	}
}
