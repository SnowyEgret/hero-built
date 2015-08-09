package org.snowyegret.mojo.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class BlockMaquette extends Block implements ITileEntityProvider {

	// public static final IUnlistedProperty PROPERTY_TAG = new PropertyTag();
	public static final IUnlistedProperty PROPERTY_SELECTIONS = new PropertySelections();
	public static final IUnlistedProperty PROPERTY_NAME = new PropertyName();

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
		// return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PROPERTY_TAG });
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PROPERTY_SELECTIONS,
				PROPERTY_NAME });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) world.getTileEntity(pos);
		System.out.println("te=" + te);

		// NBTTagCompound tag = null;
		// if (te != null) {
		// tag = te.getTag();
		// } else {
		// System.out.println("Could not get path and tag. te=" + te);
		// }
		// System.out.println("tag=" + tag);
		// return ((IExtendedBlockState) state).withProperty(PROPERTY_TAG, tag);

		Iterable<Selection> selections = null;
		String name = null;
		if (te != null) {
			selections = te.getSelections();
			name = te.getName();
		} else {
			System.out.println("Could not get name and selections. te=" + te);
		}
		IBlockState extendedState = ((IExtendedBlockState) state).withProperty(PROPERTY_SELECTIONS, selections);
		extendedState = ((IExtendedBlockState) extendedState).withProperty(PROPERTY_NAME, name);
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

		Player player = new Player((EntityPlayer) placer);
		Modifiers modifiers = player.getModifiers();
		if (modifiers.isPressed(Modifier.CTRL)) {
			System.out.println("stack.getTagCompound()=" + stack.getTagCompound());
			// BlockMaquetTileEntity#readFromNBT will be called because the stack has a tag with tag with a key
			// BlockEntityTag
			// FIXME BlockMaquetTileEntity#writeToNBT cannot write to tag
			return;
		}

		// TODO modifier to export
		// Write tag to file
		// Path path = null;
		// try {
		// // path = Files.createFile(Paths.get(ClientProxy.PATH_SAVES, text + origin.toLong() + EXTENTION));
		// // TODO if file exists
		// // player.sendMessage(new OpenGuiMessage(GuiHandler.FILE_OVERWRITE_DIALOG));
		// path = Files.createFile(Paths.get(ClientProxy.PATH_SAVES.toString(), text + origin.toLong() + EXTENTION));
		// CompressedStreamTools.writeCompressed(tag, new FileOutputStream(path.toFile()));
		// } catch (IOException e) {
		// System.out.println(e);
		// player.clearSelections();
		// player.clearPicks();
		// return;
		// }
		// System.out.println("path=" + path);

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			System.out.println("Could not read tag. tag=" + tag);
			return;
		}
		//tag = tag.getCompoundTag(BlockMaquetteTileEntity.KEY_TAG);
		System.out.println("tag=" + tag);

		int size = tag.getInteger(BlockMaquetteTileEntity.KEY_SIZE);
		BlockPos origin = BlockPos.fromLong(tag.getLong(BlockMaquetteTileEntity.KEY_ORIGIN));
		origin = pos.subtract(origin);
		List<Selection> selections = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			NBTTagCompound tt = tag.getCompoundTag(String.valueOf(i));
			selections.add(Selection.fromNBT(tt));
		}
		System.out.println("selections=" + selections);

		List<IUndoable> undoables = Lists.newArrayList();
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
		tag.setTag(BlockMaquetteTileEntity.KEY_TAG, new NBTTagCompound());
		((BlockMaquetteTileEntity) te).writeToNBT(tag);

		stack.setStackDisplayName(((BlockMaquetteTileEntity) te).getName());

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
