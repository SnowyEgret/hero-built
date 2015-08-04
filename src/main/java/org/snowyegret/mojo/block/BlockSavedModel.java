package org.snowyegret.mojo.block;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.geom.IntegerDomain;
import org.snowyegret.geom.VoxelSet;
import org.snowyegret.mojo.item.spell.other.SpellSave;
import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class BlockSavedModel implements IBakedModel {

	private static final int COLOR = Color.WHITE.getRGB();
	private List generalQuads = Lists.newArrayList();

	public BlockSavedModel(String path) {

		if (path == null || path.isEmpty()) {
			System.out.println("Can not create quads. path=" + path);
			return;
		}

		// TODO duplicates code in BlockSaved
		// Read selections from file
		NBTTagCompound tag = null;
		try {
			tag = CompressedStreamTools.readCompressed(new FileInputStream(path));
		} catch (IOException e) {
			System.out.println("Can not create quads. e=" + e);
			return;
		}

		List<Selection> selections = Lists.newArrayList();
		int size = tag.getInteger(SpellSave.KEY_SIZE);
		for (int i = 0; i < size; i++) {
			selections.add(Selection.fromNBT(tag.getCompoundTag(String.valueOf(i))));
		}

		IntegerDomain domain = getDomain(selections);
		BlockPos cornerClosestToOrigin = new BlockPos(domain.rx.getMinimum(), domain.ry.getMinimum(),
				domain.rz.getMinimum());
		// TODO SpellSave should refuse to save a single block to avoid scale = 1 and model looking identical to block
		float scale = 1 / (float) (domain.maxDimension() + 1);
		// System.out.println("scale=" + scale);

		BlockModelShapes shapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		for (Selection sel : selections) {
			BlockPos pos = sel.getPos().subtract(cornerClosestToOrigin);
			// System.out.println("pos=" + pos);
			IBlockState state = sel.getState();
			TextureAtlasSprite sprite = shapes.getModelForState(state).getTexture();
			// Create a BakedQuad for each side
			for (EnumFacing side : EnumFacing.values()) {
				BakedQuad q = createBakedQuadForFace(pos, scale, 0, sprite, side);
				generalQuads.add(q);
			}
		}
	}

	// IBakedModel---------------------------------------------------------------------

	@Override
	public List getFaceQuads(EnumFacing side) {
		return Lists.newArrayList();
	}

	@Override
	public List getGeneralQuads() {
		return generalQuads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return null;
	}

	// Private-------------------------------------------------------------------------

	private IntegerDomain getDomain(List<Selection> selections) {
		List<Point3i> points = Lists.newArrayList();
		for (Selection s : selections) {
			BlockPos p = s.getPos();
			points.add(new Point3i(p.getX(), p.getY(), p.getZ()));
		}
		return new VoxelSet(points).getDomain();
	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/ChessboardSmartItemModel.java
	private BakedQuad createBakedQuadForFace(BlockPos pos, float w, int itemRenderLayer, TextureAtlasSprite sprite, EnumFacing face) {
		float lr, ud, fb;
		float x1, x2, x3, x4;
		float y1, y2, y3, y4;
		float z1, z2, z3, z4;

		switch (face) {
		case UP:
			lr = pos.getX() * w;
			ud = pos.getZ() * w;
			fb = pos.getY() * w;
			x1 = x2 = lr + w;
			x3 = x4 = lr;
			z1 = z4 = ud + w;
			z2 = z3 = ud;
			y1 = y2 = y3 = y4 = fb + w;
			break;

		case DOWN:
			lr = pos.getX() * w;
			ud = pos.getZ() * w;
			fb = pos.getY() * w;
			x1 = x2 = lr + w;
			x3 = x4 = lr;
			z1 = z4 = ud;
			z2 = z3 = ud + w;
			y1 = y2 = y3 = y4 = fb;
			break;

		case WEST:
			lr = pos.getZ() * w;
			ud = pos.getY() * w;
			fb = pos.getX() * w;
			z1 = z2 = lr + w;
			z3 = z4 = lr;
			y1 = y4 = ud;
			y2 = y3 = ud + w;
			x1 = x2 = x3 = x4 = fb;
			break;

		case EAST:
			lr = pos.getZ() * w;
			ud = pos.getY() * w;
			fb = pos.getX() * w;
			z1 = z2 = lr;
			z3 = z4 = lr + w;
			y1 = y4 = ud;
			y2 = y3 = ud + w;
			x1 = x2 = x3 = x4 = fb + w;
			break;

		case NORTH:
			lr = pos.getX() * w;
			ud = pos.getY() * w;
			fb = pos.getZ() * w;
			x1 = x2 = lr;
			x3 = x4 = lr + w;
			y1 = y4 = ud;
			y2 = y3 = ud + w;
			z1 = z2 = z3 = z4 = fb;
			break;

		case SOUTH:
			lr = pos.getX() * w;
			ud = pos.getY() * w;
			fb = pos.getZ() * w;
			x1 = x2 = lr + w;
			x3 = x4 = lr;
			y1 = y4 = ud;
			y2 = y3 = ud + w;
			z1 = z2 = z3 = z4 = fb + w;
			break;

		default:
			System.out.println("Unexpected face=" + face);
			return null;
		}

		return new BakedQuad(Ints.concat(
				// @formatter:off
				vertexToInts(x1, y1, z1, COLOR, sprite, 16f, 16f),
				vertexToInts(x2, y2, z2, COLOR, sprite, 16f, 0f),
				vertexToInts(x3, y3, z3, COLOR, sprite, 0f, 0f),
				vertexToInts(x4, y4, z4, COLOR, sprite, 0f, 16f)),
				// @formatter:on
				itemRenderLayer, face);
	}

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite sprite, float u, float v) {
		return new int[] {
			// @formatter:off
			Float.floatToRawIntBits(x), 
			Float.floatToRawIntBits(y), 
			Float.floatToRawIntBits(z), 
			color,
			Float.floatToRawIntBits(sprite.getInterpolatedU(u)),
			Float.floatToRawIntBits(sprite.getInterpolatedV(v)), 
			0
			// @formatter:on
		};
	}
}
