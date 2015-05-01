package ds.plato.item.spell.select;

import net.minecraft.util.BlockPos;

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

	public static BlockPos[] all() {
		return concatArray(topALL(), middleALL(), bottomALL());
	}
	
	public static BlockPos[] allCross() {
		return concatArray(topCross(), middleALL(), bottomCross());
	}
	
	//Private------------------------------------------------
	
	static BlockPos[] concatArray(BlockPos[] a, BlockPos[] b, BlockPos[] c) {
	   int aLen = a.length;
	   int bLen = b.length;
	   int cLen = c.length;
	   BlockPos[] d= new BlockPos[aLen+bLen+cLen];
	   System.arraycopy(a, 0, d, 0, aLen);
	   System.arraycopy(b, 0, d, aLen, bLen);
	   System.arraycopy(c, 0, d, aLen+bLen, cLen);
	   return d;
	}
	
	private static BlockPos[] topALL() {
		return new BlockPos[] {
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
	}
	
	private static BlockPos[] middleALL() {
		return new BlockPos[] {
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
	}
	
	private static BlockPos[] bottomALL() {
		return new BlockPos[] {
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
	}
	
	private static BlockPos[] topCross() {
		return new BlockPos[] {
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
	}
	
	private static BlockPos[] middleCross() {
		return new BlockPos[] {
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
	}
	
	private static BlockPos[] bottomCross() {
		return new BlockPos[] {
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
	}
	
}
