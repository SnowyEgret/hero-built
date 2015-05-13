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

//	private static BlockPos[][][] p = new BlockPos[3][3][3];
//	static {
//		for(int x = -1; x<= 1; x++) {
//			for(int y = -1; y<= 1; y++) {
//				for(int z = -1; z<= 1; z++) {
//					p[x+1][y+1][z+1] = new BlockPos(x, y, z);
//				}
//			}
//		}
//	}

	public static BlockPos[] above = positions(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1), false);
	public static BlockPos[] aboveNoCorners = positions(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1), true);
	public static BlockPos[] aboveInclusive = positions(Range.between(-1, 1), Range.between(0, 1), Range.between(-1, 1), false);
//		new BlockPos[] {
//				p[0][2][0],
//				p[0][2][1],
//				p[0][2][2],
//				p[1][2][0],
//				p[1][2][1],
//				p[1][2][2],
//				p[2][2][0],
//				p[2][2][1],
//				p[2][2][2],
//		};
	
	public static BlockPos[] horizontal = positions(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1), false);
	public static BlockPos[] horizontalNoCorners = positions(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1), true);
	
	public static BlockPos[] below = positions(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1), false);
	public static BlockPos[] belowNoCorners = positions(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1), true);
	public static BlockPos[] belowInclusive = positions(Range.between(-1, 1), Range.between(0, -1), Range.between(-1, 1), false);
//		new BlockPos[] {
//				p[0][0][0],
//				p[0][0][1],
//				p[0][0][2],
//				p[1][0][0],
//				p[1][0][1],
//				p[1][0][2],
//				p[2][0][0],
//				p[2][0][1],
//				p[2][0][2],
//		};
	
	public static BlockPos[] EW = positions(Range.between(-1, 1), Range.between(-1, 1), Range.between(0, 0), false);
	public static BlockPos[] NS = positions(Range.between(0, 0), Range.between(-1, 1), Range.between(-1, 1), false);
		
//	private static BlockPos[] topCross = 
//		new BlockPos[] {
//				//p[0][2][0],
//				p[0][2][1],
//				//p[0][2][2],
//				p[1][2][0],
//				p[1][2][1],
//				p[1][2][2],
//				//p[2][2][0],
//				p[2][2][1],
//				//p[2][2][2],
//		};
	
//	private static BlockPos[] horizontalNoCorners = 
//		new BlockPos[] {
//				//p[0][1][0],
//				p[0][1][1],
//				//p[0][1][2],
//				p[1][1][0],
//				p[1][1][1],
//				p[1][1][2],
//				//p[2][1][0],
//				p[2][1][1],
//				//p[2][1][2],
//		};
	
//	private static BlockPos[] bottomCross = 
//		new BlockPos[] {
//				//p[0][0][0],
//				p[0][0][1],
//				p[0][0][2],
//				//p[1][0][0],
//				p[1][0][1],
//				p[1][0][2],
//				//p[2][0][0],
//				p[2][0][1],
//				//p[2][0][2],
//		};	

	public static BlockPos[] all = concat(above, horizontal, below);
	public static BlockPos[] allNoCorners = concat(NS, EW, horizontal);
	public static BlockPos[] up = positions(Range.between(0, 0), Range.between(1, 1), Range.between(0, 0), false);
	public static BlockPos[] down = positions(Range.between(0, 0), Range.between(-1, -1), Range.between(0, 0), false);
	
//	public static BlockPos[] edge(IWorld w, BlockPos p0) {
//		for (BlockPos p : all) {
//			if (p.getY() == p0.getY()) {
//				if (w.getBlock(p.up()) == Blocks.air) {
//					// for (BlockPos pointAbove : new Shell(Type.TOP_CROSS, p, w)) {
//					for (BlockPos pointAbove : new Shell(Type.TOP_ALL, p, w)) {
//						Block b = w.getBlock(pointAbove);
//						if (b != Blocks.air) {
//							//positions.add(p);
//							break;
//						}
//					}
//				}
//			}
//		}
//	}
	

	
	//Private------------------------------------------------
	
	private static BlockPos[] positions(Range<Integer> rx, Range<Integer> ry, Range<Integer> rz, boolean excludeCorners) {
		List<BlockPos> p = new ArrayList<>();
		for(int x = rx.getMinimum(); x<= rx.getMaximum(); x++) {
			for(int y = ry.getMinimum(); y<= ry.getMaximum(); y++) {
				for(int z = rz.getMinimum(); z<= rz.getMaximum(); z++) {
					if (excludeCorners) {
						if((Math.abs(x)==1 && Math.abs(z)==1)|| (Math.abs(x)==1 && Math.abs(y)==1) || (Math.abs(y)==1 && Math.abs(z)==1)) continue;
					}
					p.add(new BlockPos(x, y, z));
				}
			}
		}
		BlockPos[] array = new BlockPos[p.size()];
		return p.toArray(array);
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
