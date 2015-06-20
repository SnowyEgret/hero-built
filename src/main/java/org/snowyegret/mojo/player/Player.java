package org.snowyegret.mojo.player;

import java.util.List;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.undo.IUndo;
import org.snowyegret.mojo.world.IWorld;
import org.snowyegret.mojo.world.WorldWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.google.common.collect.Lists;

public class Player implements IPlayer {

	private EntityPlayer player;

	public enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST;
	}

	protected Player(EntityPlayer player) {
		this.player = player;
	}

	protected Player() {
		player = Minecraft.getMinecraft().thePlayer;
	}

	public static IPlayer instance(EntityPlayer player) {
		return new Player(player);
	}

	public static IPlayer instance() {
		return new Player();
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public IWorld getWorld() {
		return new WorldWrapper(player.worldObj);
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
				spell = ((Staff) item).getSpell(stack, getPickManager());
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
	public void openGui(int id) {
		player.openGui(MoJo.instance, id, getWorld().getWorld(), (int) player.posX, (int) player.posY,
				(int) player.posZ);
	}

	@Override
	public void moveTo(BlockPos pos) {
		player.moveEntity(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public Modifiers getModifiers() {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		return ((PlayerProperties) p).getModifiers();
	}

	@Override
	public IUndo getUndoManager() {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		return ((PlayerProperties) p).getUndoManager();
	}

	@Override
	public ISelect getSelectionManager() {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		return ((PlayerProperties) p).getSelectionManager();
	}

	@Override
	public IPick getPickManager() {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		return ((PlayerProperties) p).getPickManager();
	}

	@Override
	public void setLastSpell(ISpell spell) {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		((PlayerProperties) p).setLastSpell(spell);
	}

	@Override
	public Clipboard getClipboard() {
		IExtendedEntityProperties p = player.getExtendedProperties(PlayerProperties.NAME);
		return ((PlayerProperties) p).getClipboard();
	}

	@Override
	public void playSoundAtPlayer(String sound) {
		getWorld().getWorld().playSoundAtEntity(player, sound, 1f, 1f);
	}

	@Override
	public List<BlockPos> getBounds() {
		List<BlockPos> bounds = Lists.newArrayList();
		bounds.add(player.getPosition());
		bounds.add(player.getPosition().up());
		return bounds;
	}

}
