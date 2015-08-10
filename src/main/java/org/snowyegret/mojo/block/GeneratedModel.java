package org.snowyegret.mojo.block;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import org.snowyegret.geom.IDrawable;
import org.snowyegret.geom.IntegerDomain;
import org.snowyegret.geom.VoxelSet;
import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

// Generates quads for cubes based on selections saved to file or an IDrawable
// Cubes are scaled to fit inside a block
// Used by BlockSavedModel and ItemModels for draw spells (package spell.draw) in EventHandlerClient#onModelBakeEvent
public class GeneratedModel implements IBakedModel {

	private static final int COLOR = Color.WHITE.getRGB();

	// This class is never registered, so there can be more than one instance and fields
	// For #toString
	private int numBlocks;
	private List generalQuads = Lists.newArrayList();
	private List faceQuads = Lists.newArrayList();

	// IBakedModel---------------------------------------------------------------------

	// For case where BlockMaquetteSmartModel cannot get selections
	// Returns a model with no quads
	public GeneratedModel() {
	}

	public GeneratedModel(Iterable<Selection> selections) {
		createQuads(selections);
	}

	public GeneratedModel(IDrawable drawable, IBlockState state) {
		System.out.println("drawable=" + drawable);

		TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getModelForState(state).getTexture();
		System.out.println("sprite=" + sprite);
		VoxelSet voxels = drawable.voxelize();
		numBlocks = voxels.size();
		IntegerDomain domain = voxels.getDomain();
		BlockPos cornerClosestToOrigin = new BlockPos(domain.rx.getMinimum(), domain.ry.getMinimum(),
				domain.rz.getMinimum());
		float scale = 1 / (float) (domain.maxDimension() + 1);
		for (Point3i p : voxels) {
			BlockPos pos = new BlockPos(p.x, p.y, p.z).subtract(cornerClosestToOrigin);
			// System.out.println("pos=" + pos);
			for (EnumFacing side : EnumFacing.values()) {
				BakedQuad q = createBakedQuadForFace(pos, scale, 0, sprite, side);
				generalQuads.add(q);
			}
		}
	}

	@Deprecated
	public GeneratedModel(NBTTagCompound tag) {
		// System.out.println("tag=" + tag);
		createQuads(tag);
	}

	// IBakedModel---------------------------------------------------------------------

	@Deprecated
	public GeneratedModel(String path) {
		// System.out.println("path=" + path);
		NBTTagCompound tag = readTagFromFile(path);
		if (tag != null) {
			createQuads(tag);
		}
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return faceQuads;
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
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getTexture(Blocks.dirt.getDefaultState());
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneratedModel [numBlocks=");
		builder.append(numBlocks);
		builder.append(", faceQuads=");
		builder.append(faceQuads.size());
		builder.append(", generalQuads=");
		builder.append(generalQuads.size());
		builder.append("]");
		return builder.toString();
	}

	// Private-------------------------------------------------------------------------

	private NBTTagCompound readTagFromFile(String pathString) {
		try {
			Path path = Paths.get(pathString);
		} catch (Exception e) {
			System.out.println("Can not read tag from file. Invalid path. path=" + pathString);
			return null;
		}

		NBTTagCompound tag = null;
		try {
			tag = CompressedStreamTools.readCompressed(new FileInputStream(pathString));
		} catch (IOException e) {
			System.out.println("Can not read tag from file. e=" + e);
		}
		return tag;

	}

	private void createQuads(Iterable<Selection> selections) {

		IntegerDomain domain = getDomain(selections);
		BlockPos cornerClosestToOrigin = new BlockPos(domain.rx.getMinimum(), domain.ry.getMinimum(),
				domain.rz.getMinimum());
		// TODO SpellSave should refuse to save a single block to avoid scale = 1 and model looking identical to block
		float scale = 1 / (float) (domain.maxDimension() + 1);
		// System.out.println("scale=" + scale);

		BlockModelShapes shapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		numBlocks = 0;
		for (Selection sel : selections) {
			numBlocks++;
			BlockPos pos = sel.getPos().subtract(cornerClosestToOrigin);
			// System.out.println("pos=" + pos);
			IBlockState state = sel.getState();
			// TextureAtlasSprite sprite = shapes.getModelForState(state).getTexture();
			IBakedModel model = shapes.getModelForState(sel.getState());
			List<BakedQuad> gQuads = model.getGeneralQuads();
			for (BakedQuad q : gQuads) {
				q = scaleAndTranslateQuad(q, pos, scale);
				// System.out.println("q=" + q.getFace());
				generalQuads.add(q);
				// faceQuads.add(q);
			}
			// Create a BakedQuad for each side
			for (EnumFacing face : EnumFacing.values()) {
				// BakedQuad q = createBakedQuadForFace(pos, scale, 0, sprite, side);
				// generalQuads.add(q);
				List<BakedQuad> fQuads = model.getFaceQuads(face);
				for (BakedQuad q : fQuads) {
					q = scaleAndTranslateQuad(q, pos, scale);
					// System.out.println("q=" + q.getFace());
					// generalQuads.add(q);
					faceQuads.add(q);
				}
			}
		}
	}

	private BakedQuad scaleAndTranslateQuad(BakedQuad q, Vec3i t, float s) {

		int[] v = q.getVertexData().clone();
		int x = t.getX();
		int y = t.getY();
		int z = t.getZ();

		switch (q.getFace()) {
		case UP:
			v[0] = transform(v[0], x, s);
			v[7] = transform(v[7], x, s);
			v[14] = transform(v[14], x, s);
			v[21] = transform(v[21], x, s);

			v[1] = transform(v[1], -z, s);
			v[8] = transform(v[8], -z, s);
			v[15] = transform(v[15], -z, s);
			v[22] = transform(v[22], -z, s);

			v[2] = transform(v[2], y, s);
			v[9] = transform(v[9], y, s);
			v[16] = transform(v[16], y, s);
			v[23] = transform(v[23], y, s);
			break;

		case DOWN:
			v[0] = transform(v[0], x, s);
			v[7] = transform(v[7], x, s);
			v[14] = transform(v[14], x, s);
			v[21] = transform(v[21], x, s);

			v[1] = transform(v[1], z, s);
			v[8] = transform(v[8], z, s);
			v[15] = transform(v[15], z, s);
			v[22] = transform(v[22], z, s);

			v[2] = transform(v[2], -y, s);
			v[9] = transform(v[9], -y, s);
			v[16] = transform(v[16], -y, s);
			v[23] = transform(v[23], -y, s);
			break;

		case EAST:
			v[0] = transform(v[0], z, s);
			v[7] = transform(v[7], z, s);
			v[14] = transform(v[14], z, s);
			v[21] = transform(v[21], z, s);

			v[1] = transform(v[1], y, s);
			v[8] = transform(v[8], y, s);
			v[15] = transform(v[15], y, s);
			v[22] = transform(v[22], y, s);

			v[2] = transform(v[2], x, s);
			v[9] = transform(v[9], x, s);
			v[16] = transform(v[16], x, s);
			v[23] = transform(v[23], x, s);
			break;

		case WEST:
			v[0] = transform(v[0], z, s);
			v[7] = transform(v[7], z, s);
			v[14] = transform(v[14], z, s);
			v[21] = transform(v[21], z, s);

			v[1] = transform(v[1], y, s);
			v[8] = transform(v[8], y, s);
			v[15] = transform(v[15], y, s);
			v[22] = transform(v[22], y, s);

			v[2] = transform(v[2], -x, s);
			v[9] = transform(v[9], -x, s);
			v[16] = transform(v[16], -x, s);
			v[23] = transform(v[23], -x, s);
			break;

		case NORTH:
			v[0] = transform(v[0], -x, s);
			v[7] = transform(v[7], -x, s);
			v[14] = transform(v[14], -x, s);
			v[21] = transform(v[21], -x, s);

			v[1] = transform(v[1], y, s);
			v[8] = transform(v[8], y, s);
			v[15] = transform(v[15], y, s);
			v[22] = transform(v[22], y, s);

			v[2] = transform(v[2], -z, s);
			v[9] = transform(v[9], -z, s);
			v[16] = transform(v[16], -z, s);
			v[23] = transform(v[23], -z, s);
			break;

		case SOUTH:
			// Case where quad coordinates are aligned with world coordinates
			v[0] = transform(v[0], x, s);
			v[7] = transform(v[7], x, s);
			v[14] = transform(v[14], x, s);
			v[21] = transform(v[21], x, s);

			v[1] = transform(v[1], y, s);
			v[8] = transform(v[8], y, s);
			v[15] = transform(v[15], y, s);
			v[22] = transform(v[22], y, s);

			v[2] = transform(v[2], z, s);
			v[9] = transform(v[9], z, s);
			v[16] = transform(v[16], z, s);
			v[23] = transform(v[23], z, s);
			break;
		default:
			break;
		}

		return new BakedQuad(v, q.getTintIndex(), q.getFace());
	}

	private int transform(int i, int t, float s) {
		float f = Float.intBitsToFloat(i);
		f = (f + t) * s;
		return Float.floatToRawIntBits(f);
	}

	private void createQuads(NBTTagCompound tag) {
		if (tag == null) {
			System.out.println("Could not create quads. tag=" + tag);
			return;
		}
		List<Selection> selections = Lists.newArrayList();
		int size = tag.getInteger(BlockMaquetteTileEntity.KEY_SIZE);
		for (int i = 0; i < size; i++) {
			selections.add(Selection.fromNBT(tag.getCompoundTag(String.valueOf(i))));
		}
		createQuads(selections);
	}

	private IntegerDomain getDomain(Iterable<Selection> selections) {
		List<Point3i> points = Lists.newArrayList();
		for (Selection s : selections) {
			BlockPos p = s.getPos();
			points.add(new Point3i(p.getX(), p.getY(), p.getZ()));
		}
		return new VoxelSet(points).getDomain();
	}

	// Based on code at
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
