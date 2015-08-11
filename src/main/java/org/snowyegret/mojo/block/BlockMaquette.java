package org.snowyegret.mojo.block;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.ClientProxy;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public class BlockMaquette extends Block implements ITileEntityProvider {

	public static final String EXTENTION = ".maquette";
	// For passing BlockMaqetteTileEntity fields to smart model
	public static final IUnlistedProperty PROP_SELECTIONS = new PropertySelections();
	public static final IUnlistedProperty PROP_NAME = new PropertyName();

	// For rotating block
	public static final PropertyDirection PROP_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockMaquette() {
		super(Material.clay);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROP_FACING, EnumFacing.NORTH));
	}

	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(PROP_FACING, EnumFacing.getFront(5 - (meta & 3)));
		return iblockstate;
	}

	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i |= 5 - ((EnumFacing) state.getValue(PROP_FACING)).getIndex();
		return i;
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
		return new ExtendedBlockState(this, new IProperty[] { PROP_FACING }, new IUnlistedProperty[] { PROP_SELECTIONS,
				PROP_NAME });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) world.getTileEntity(pos);
		Iterable<Selection> selections = null;
		String name = null;
		if (te != null) {
			selections = te.getSelections();
			name = te.getName();
		} else {
			System.out.println("Could not get name and selections from tile entity. te=" + te);
		}
		return ((IExtendedBlockState) state).withProperty(PROP_SELECTIONS, selections).withProperty(PROP_NAME, name);
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState stateIn, EntityLivingBase playerIn, ItemStack stack) {
		// Only on server
		if (world.isRemote) {
			return;
		}

		Player player = new Player((EntityPlayer) playerIn);
		Modifiers modifiers = player.getModifiers();
		if (modifiers.isPressed(Modifier.CTRL)) {
			// BlockMaquetteTileEntity:readFromNBT has been called because stack has tag with key 'BlockEntityTag'
			// Set in #getDrops
			// See comment in #getDrops
			BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) world.getTileEntity(pos);
			List<IUndoable> undoables = Lists.newArrayList();
			for (Selection s : te.getSelections()) {
				BlockPos p = s.getPos().subtract(te.getOrigin());
				p = p.add(pos);
				undoables.add(new UndoableSetBlock(p, world.getBlockState(p), s.getState()));
			}
			player.getTransactionManager().doTransaction(undoables);
		}
	}

	// If this returns null, super.getDrops in getDrops with get an empty list.
	// http://www.minecraftforge.net/forum/index.php/topic,32550.msg170136.html#msg170136
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	// @Override
	// public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
	// }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		// Only on server
		if (worldIn.isRemote) {
			return false;
		}

		Player player = new Player((EntityPlayer) playerIn);
		Modifiers modifiers = player.getModifiers();

		// Rotate
		if (modifiers.isPressed(Modifier.SHIFT)) {
			return rotateBlock(worldIn, pos, EnumFacing.UP);
		}

		// Export
		if (modifiers.isPressed(Modifier.CTRL)) {
			// TODO modifier to export
			// Write tag to file
			try {
				// TODO if file exists
				// player.sendMessage(new OpenGuiMessage(GuiHandler.FILE_OVERWRITE_DIALOG));
				BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) worldIn.getTileEntity(pos);
				Path path = Files.createFile(Paths.get(ClientProxy.PATH_EXPORT.toString(), te.getName() + EXTENTION));
				System.out.println("path=" + path);
				NBTTagCompound tag = new NBTTagCompound();
				te.writeToNBT(tag);
				CompressedStreamTools.writeCompressed(tag, new FileOutputStream(path.toFile()));
				return true;
			} catch (IOException e) {
				System.out.println(e);
				player.clearSelections();
				player.clearPicks();
				return false;
			}
		}

		return false;
	}

	// These methods (getDrops, removedByPlayer, harvestBlock) delay deletion of tile entity until block is picked up.
	// http://www.minecraftforge.net/forum/index.php/topic,32477.msg169713.html
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> itemStacks = super.getDrops(world, pos, state, fortune);
		TileEntity te = world.getTileEntity(pos);
		if (te == null || !(te instanceof BlockMaquetteTileEntity)) {
			System.out.println("Could not write tile entity to tag. te=" + te);
			return itemStacks;
		}

		// http://www.minecraftforge.net/forum/index.php/topic,32550.msg170141.html#msg170141
		// Choonster:
		// Side note: if you create a compound tag with the key "BlockEntityTag" in an ItemStack's compound tag and your
		// Block has a TileEntity, ItemBlock will call TileEntity#readFromNBT with it after placing the Block.
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		NBTTagCompound blockEntityTag = new NBTTagCompound();
		tag.setTag("BlockEntityTag", blockEntityTag);
		// "it" in above comment refers to the BlockEntity tag. Must write tile entity to blockEntityTag
		((BlockMaquetteTileEntity) te).writeToNBT(blockEntityTag);

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
