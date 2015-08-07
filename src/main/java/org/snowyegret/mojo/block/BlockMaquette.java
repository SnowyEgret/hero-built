package org.snowyegret.mojo.block;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.CommonProxy;
import org.snowyegret.mojo.item.spell.other.SpellMaquette;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class BlockMaquette extends Block implements ITileEntityProvider {

	public static final PropertyPath PROPERTY_PATH = new PropertyPath();
	public static final String KEY_PATH = "path";
	public static final IUnlistedProperty PROPERTY_TAG = new PropertyTag();
	// public static final String KEY_TAG = "tag";
	public static final String KEY_TAG = "BlockEntityTag";

	public BlockMaquette() {
		super(Material.clay);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BlockMaquetteTileEntity();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	// Prevents sky blue base when rendered
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	// Prevents ambient occlusion when rendered
	@Override
	public boolean isFullCube() {
		return false;
	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe05_block_smartblockmodel2/Block3DWeb.java
	// Allows player to walk through block
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PROPERTY_PATH, PROPERTY_TAG });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) world.getTileEntity(pos);
		System.out.println("te=" + te);
		String path = null;
		NBTTagCompound tag = null;
		if (te != null) {
			path = te.getPath();
			tag = te.getTag();
		} else {
			System.out.println("Could not get path and tag. te=" + te);
		}
		System.out.println("path=" + path);
		System.out.println("tag=" + tag);
		IBlockState extendedState = ((IExtendedBlockState) state).withProperty(PROPERTY_PATH, path);
		extendedState = ((IExtendedBlockState) extendedState).withProperty(PROPERTY_TAG, tag);
		System.out.println("extendedState=" + extendedState.getProperties());
		return extendedState;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	// @Override
	// public CreativeTabs getCreativeTabToDisplayOn() {
	// return CommonProxy.tabMoJo;
	// }

	// @Override
	// public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
	// // TODO Auto-generated method stub
	// super.onBlockDestroyedByPlayer(worldIn, pos, state);
	// }

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
			System.out.println("Could not read file. e=" + e);
			return;
		}
		// System.out.println("tag=" + tag);

		int size = tag.getInteger(SpellMaquette.KEY_SIZE);
		BlockPos origin = BlockPos.fromLong(tag.getLong(SpellMaquette.KEY_ORIGIN));
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

	// These methods (getDrops, removedByPlayer, harvestBlock) delay deletion of tile entity until block is picked up.
	// http://www.minecraftforge.net/forum/index.php/topic,32477.msg169713.html
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> itemStacks = super.getDrops(world, pos, state, fortune);
		TileEntity te = world.getTileEntity(pos);
		if (te == null) {
			System.out.println("Could not write path to tag. te=" + te);
			return itemStacks;
		}
		if (!(te instanceof BlockMaquetteTileEntity)) {
			System.out.println("Could not write path to tag. TileEntity not a BlockSavedTileEntity. te=" + te);
			return itemStacks;
		}

		// Write BlockSavedTileEntity#path to the stack's tag
		// http://www.minecraftforge.net/forum/index.php/topic,32550.msg170141.html#msg170141
		// Side note: if you create a compound tag with the key "BlockEntityTag" in an ItemStack's compound tag and your
		// Block has a TileEntity, ItemBlock will call TileEntity#readFromNBT with it after placing the Block.
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		((BlockMaquetteTileEntity) te).writeToNBT(tag);

		itemStacks.add(stack);
		return itemStacks;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			// Delay deletion of the block until after getDrops
			return true;
		}
		// Implementation of issue: BlockSaved drops in creative mode #263
		// http://www.minecraftforge.net/forum/index.php/topic,32700.0.html
		if (player.capabilities.isCreativeMode) {
			harvestBlock(world, player, pos, this.getDefaultState(), null);
		}
		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		super.harvestBlock(world, player, pos, state, te);
		world.setBlockToAir(pos);
	}
}
