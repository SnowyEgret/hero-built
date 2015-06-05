package ds.plato.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.Staff;
import ds.plato.world.IWorld;
import ds.plato.world.WorldWrapper;

public class Player implements IPlayer {

	private static Player instance = null;
	private EntityPlayer player;

	public enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST;
	}

	public Player(EntityPlayer player) {
		this.player = player;
	}

	protected Player() {
		player = Minecraft.getMinecraft().thePlayer;
	}

	// Singleton necessary for fields jumpHeight and prevYaw
	public static IPlayer instance() {
		// if (instance == null) {
		// instance = new Player();
		// }
		// return instance;
		return new Player();
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	// Returns the integrated server if in single player
	@Override
	public IWorld getWorld() {

		World w = null;
		Minecraft mc = Minecraft.getMinecraft();
		IntegratedServer integratedServer = mc.getIntegratedServer();
		if (integratedServer != null) {
			w = integratedServer.worldServerForDimension(player.dimension);
		} else {
			w = mc.theWorld;
		}
		return new WorldWrapper(w);
	}

	@Override
	public Hotbar getHotbar() {
		return new Hotbar(player.inventory);
	}

	@Override
	public Direction getDirection() {
		int yaw = (int) (player.rotationYawHead);
		yaw += (yaw >= 0) ? 45 : -45;
		yaw /= 90;
		int modulus = yaw % 4;
		Direction direction = null;
		switch (modulus) {
		case 0:
			direction = Direction.SOUTH;
			break;
		case 1:
			direction = Direction.WEST;
			break;
		case -1:
			direction = Direction.EAST;
			break;
		case 2:
			direction = Direction.NORTH;
			break;
		case -2:
			direction = Direction.NORTH;
			break;
		case 3:
			direction = Direction.EAST;
			break;
		case -3:
			direction = Direction.WEST;
			break;
		default:
			throw new RuntimeException("Unexpected modulus. Got " + modulus);
		}
		return direction;
	}

	@Override
	public ItemStack getHeldItemStack() {
		return player.getCurrentEquippedItem();
	}

	@Override
	public Item getHeldItem() {
		return getHeldItemStack().getItem();
	}

	@Override
	public ISpell getSpell() {
		ISpell spell = null;
		ItemStack stack = player.getHeldItem();
		if (stack != null) {
			Item item = stack.getItem();
			if (item instanceof Spell) {
				spell = (ISpell) item;
			} else if (item instanceof Staff) {
				spell = ((Staff) item).getSpell(stack);
			}
		}
		return spell;
	}

	@Override
	public Staff getStaff() {
		Staff staff = null;
		ItemStack is = player.getHeldItem();
		if (is != null) {
			Item item = is.getItem();
			if (item instanceof Staff) {
				staff = (Staff) item;
			}
		}
		return staff;
	}

	@Override
	public void orbitAround(Vec3 pos, int dx, int dy) {
		pos = pos.add(new Vec3(.5, .5, .5));
		Vec3 v = player.getPositionVector();
		v = v.subtract(pos);
		v = v.rotateYaw((float) (-dx * Math.PI / 180));
		// v = v.rotatePitch((float) (-dy * Math.PI / 180));
		double yaw = 180 / Math.PI * Math.atan2(v.zCoord, v.xCoord) + 90;
		// double pitch = 180 / Math.PI * Math.atan2(v.yCoord, Math.sqrt(v.xCoord*v.xCoord+v.zCoord*v.zCoord));
		v = v.add(pos);
		player.setLocationAndAngles(v.xCoord, v.yCoord, v.zCoord, (float) yaw, player.rotationPitch);
	}

	@Override
	public BlockPos getPosition() {
		return player.getPosition();
	}

	@Override
	public boolean isFlying() {
		return player.capabilities.isFlying;
	}

	@Override
	public void openGui(int id, IWorld world) {
		player.openGui(Plato.instance, id, world.getWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
	}

	@Override
	public void moveTo(BlockPos pos) {
		player.moveEntity(pos.getX(), pos.getY(), pos.getZ());
		// player.moveToBlockPosAndAngles(pos, player.rotationYaw, player.rotationPitch);
	}
}
