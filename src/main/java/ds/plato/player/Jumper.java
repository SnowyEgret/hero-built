package ds.plato.player;

import net.minecraft.util.BlockPos;

public class Jumper {

	int height;
	private IPlayer player;

	public Jumper(IPlayer player) {
		this.player = player;
	}

	public void jump() {
		if (height != 0) {
			player.moveTo(new BlockPos(0, height + 1, 0));
		}
	}

	public void setHeight(BlockPos pos) {
		BlockPos p = player.getPosition();
		int dx = pos.getX() - p.getX();
		int dy = pos.getY() - p.getY();
		int dz = pos.getZ() - p.getZ();
		// System.out.println(dx);
		// System.out.println(dy);
		if (dx == 0 && dz == 0 && dy > 0) {
			if (dy > height) {
				height = dy;
			}
		}
	}

}
