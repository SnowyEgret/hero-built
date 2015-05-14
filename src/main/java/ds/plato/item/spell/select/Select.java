package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.apache.commons.lang3.Range;

import ds.plato.api.IWorld;
import ds.plato.item.spell.select.Shell.Type;

public class Select {

	public static BlockPos[] above = positions(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1), false);
	public static BlockPos[] aboveNoCorners = positions(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1), true);
	public static BlockPos[] aboveInclusive = positions(Range.between(-1, 1), Range.between(0, 1), Range.between(-1, 1), false);
	public static BlockPos[] horizontal = positions(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1), false);
	public static BlockPos[] horizontalNoCorners = positions(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1), true);	
	public static BlockPos[] below = positions(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1), false);
	public static BlockPos[] belowNoCorners = positions(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1), true);
	public static BlockPos[] belowInclusive = positions(Range.between(-1, 1), Range.between(0, -1), Range.between(-1, 1), false);
	public static BlockPos[] EW = positions(Range.between(-1, 1), Range.between(-1, 1), Range.between(0, 0), false);
	public static BlockPos[] NS = positions(Range.between(0, 0), Range.between(-1, 1), Range.between(-1, 1), false);
	public static BlockPos[] all = concat(above, horizontal, below);
	public static BlockPos[] allNoCorners = concat(NS, EW, horizontal);
	public static BlockPos[] up = positions(Range.between(0, 0), Range.between(1, 1), Range.between(0, 0), false);
	public static BlockPos[] down = positions(Range.between(0, 0), Range.between(-1, -1), Range.between(0, 0), false);
	
	//If all of the horizontal blocks do not have air above then center
//	public static BlockPos[] edge(IWorld w, BlockPos center) {
//		Set<BlockPos> positions = new HashSet<>();
//		for (BlockPos p : horizontal) {
//			p = center.add(p);
//			if (w.getBlock(p.up()) == Blocks.air) {
//				// for (BlockPos pointAbove : new Shell(Type.TOP_CROSS, p, w)) {
//				for (BlockPos pointAbove : new Shell(Type.TOP_ALL, p, w)) {
//					Block b = w.getBlock(pointAbove);
//					if (b != Blocks.air) {
//						positions.add(p);
//						break;
//					}
//				}
//			}
//		}
//		BlockPos[] array = new BlockPos[positions.size()];
//		return positions.toArray(array);
//	}
	
	public static boolean isEdgeOnGround(IWorld w, BlockPos position) {
		for (BlockPos p : above) {
			p = p.add(position);
			if (w.getBlock(p) != Blocks.air) {
				return false;
			}
		}
		return false;
	}
	
	//Private------------------------------------------------
	
	private static BlockPos[] positions(Range<Integer> rx, Range<Integer> ry, Range<Integer> rz, boolean excludeCorners) {
		List<BlockPos> positions = new ArrayList<>();
		for(int x = rx.getMinimum(); x<= rx.getMaximum(); x++) {
			for(int y = ry.getMinimum(); y<= ry.getMaximum(); y++) {
				for(int z = rz.getMinimum(); z<= rz.getMaximum(); z++) {
					if (excludeCorners) {
						if((Math.abs(x)==1 && Math.abs(z)==1)|| (Math.abs(x)==1 && Math.abs(y)==1) || (Math.abs(y)==1 && Math.abs(z)==1)) continue;
					}
					positions.add(new BlockPos(x, y, z));
				}
			}
		}
		BlockPos[] array = new BlockPos[positions.size()];
		return positions.toArray(array);
	}

	//TODO use a set to avoid duplicates
	static BlockPos[] concat(BlockPos[] a, BlockPos[] b, BlockPos[] c) {
		Set positions = new HashSet();
		for (BlockPos p : a) {
			positions.add(p);
		}
		for (BlockPos p : b) {
			positions.add(p);
		}
		for (BlockPos p : c) {
			positions.add(p);
		}
		BlockPos[] array = new BlockPos[positions.size()];
		positions.toArray(array);
		return array;
//	   int aLen = a.length;
//	   int bLen = b.length;
//	   int cLen = c.length;
//	   BlockPos[] d= new BlockPos[aLen+bLen+cLen];
//	   System.arraycopy(a, 0, d, 0, aLen);
//	   System.arraycopy(b, 0, d, aLen, bLen);
//	   System.arraycopy(c, 0, d, aLen+bLen, cLen);
//	   return d;
	}

}
