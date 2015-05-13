package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.BlockPos;

import org.apache.commons.lang3.Range;

public class Select {

	private static BlockPos[][][] p = new BlockPos[3][3][3];
	static {
		for(int x = -1; x<= 1; x++) {
			for(int y = -1; y<= 1; y++) {
				for(int z = -1; z<= 1; z++) {
					p[x+1][y+1][z+1] = new BlockPos(x, y, z);
				}
			}
		}
	}

	private static BlockPos[] top = 
		new BlockPos[] {
				p[0][2][0],
				p[0][2][1],
				p[0][2][2],
				p[1][2][0],
				p[1][2][1],
				p[1][2][2],
				p[2][2][0],
				p[2][2][1],
				p[2][2][2],
		};
	
	private static BlockPos[] middle =
		new BlockPos[] {
				p[0][1][0],
				p[0][1][1],
				p[0][1][2],
				p[1][1][0],
				p[1][1][1],
				p[1][1][2],
				p[2][1][0],
				p[2][1][1],
				p[2][1][2],
		};
	
	private static BlockPos[] bottom =
		new BlockPos[] {
				p[0][0][0],
				p[0][0][1],
				p[0][0][2],
				p[1][0][0],
				p[1][0][1],
				p[1][0][2],
				p[2][0][0],
				p[2][0][1],
				p[2][0][2],
		};
	
	public static BlockPos[] EW = positions(Range.between(-1, 1), Range.between(-1, 1), Range.between(0, 0));
	public static BlockPos[] NS = positions(Range.between(0, 0), Range.between(-1, 1), Range.between(-1, 1));
		
	private static BlockPos[] topCross = 
		new BlockPos[] {
				//p[0][2][0],
				p[0][2][1],
				//p[0][2][2],
				p[1][2][0],
				p[1][2][1],
				p[1][2][2],
				//p[2][2][0],
				p[2][2][1],
				//p[2][2][2],
		};
	
	private static BlockPos[] middleCross = 
		new BlockPos[] {
				//p[0][1][0],
				p[0][1][1],
				//p[0][1][2],
				p[1][1][0],
				p[1][1][1],
				p[1][1][2],
				//p[2][1][0],
				p[2][1][1],
				//p[2][1][2],
		};
	
	private static BlockPos[] bottomCross = 
		new BlockPos[] {
				//p[0][0][0],
				p[0][0][1],
				p[0][0][2],
				//p[1][0][0],
				p[1][0][1],
				p[1][0][2],
				//p[2][0][0],
				p[2][0][1],
				//p[2][0][2],
		};	

	public static BlockPos[] all() {
		return concat(top, middle, bottom);
	}
	
	public static BlockPos[] allCross() {
		return concat(topCross, middle, bottomCross);
	}
	
	//Private------------------------------------------------
	
	private static BlockPos[] positions(Range<Integer> rx, Range<Integer> ry, Range<Integer> rz) {
		List<BlockPos> p = new ArrayList<>();
		for(int x = rx.getMinimum(); x<= rx.getMaximum(); x++) {
			for(int y = ry.getMinimum(); y<= ry.getMaximum(); y++) {
				for(int z = rz.getMinimum(); z<= rz.getMaximum(); z++) {
					p.add(new BlockPos(x, y, z));
				}
			}
		}
		BlockPos[] array = new BlockPos[p.size()];
		return p.toArray(array);
	}

	static BlockPos[] concat(BlockPos[] a, BlockPos[] b, BlockPos[] c) {
	   int aLen = a.length;
	   int bLen = b.length;
	   int cLen = c.length;
	   BlockPos[] d= new BlockPos[aLen+bLen+cLen];
	   System.arraycopy(a, 0, d, 0, aLen);
	   System.arraycopy(b, 0, d, aLen, bLen);
	   System.arraycopy(c, 0, d, aLen+bLen, cLen);
	   return d;
	}
	
}
