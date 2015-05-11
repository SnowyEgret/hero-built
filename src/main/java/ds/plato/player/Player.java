package ds.plato.player;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.World;
import ds.plato.api.IPlayer;
import ds.plato.api.ISpell;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.Staff;
import ds.plato.world.WorldWrapper;

public class Player implements IPlayer {

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

	public static IPlayer getPlayer() {
		return new Player(Minecraft.getMinecraft().thePlayer);
	}

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
	public HotbarSlot[] getHotbar() {
		List<HotbarSlot> slots = new ArrayList<>();
		InventoryPlayer inventory = player.inventory;
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null) {
				Item item = stack.getItem();
				//int metadata = stack.getItemDamage();

				Block b = null;
				if (item instanceof ItemBlock) {
					ItemBlock itemBlock = (ItemBlock) item;
					b = itemBlock.getBlock();
					// TODO how to get color name from sub block?
					// if (b instanceof BlockColored) {
					// if (stack.getHasSubtypes()) {
					// List<ItemStack> subBlocks = new ArrayList<>();
					// ((BlockColored) b).getSubBlocks(item, getCreativeTab(), subBlocks);
					// ItemStack is = subBlocks.get(metadata);
					// b = ((ItemBlock) is.getItem()).field_150939_a;
					// System.out.println("[Spell.getSlotEntriesFromPlayer] is=" + is);
					// }
					// MapColor c = ((BlockColored) b).getMapColor(metadata);
					// System.out.println("[Spell.getSlotEntriesFromPlayer] c=" + c.colorValue);
					// }
				} else if (item == Items.water_bucket) {
					b = Blocks.water;
				} else if (item == Items.lava_bucket) {
					b = Blocks.lava;
				}

				if (b != null) {
					HotbarSlot slot = new HotbarSlot(b, i + 1);
					slots.add(slot);
				}
			}
		}
		if (slots.isEmpty()) {
			slots.add(new HotbarSlot(Blocks.dirt));
		}
		//Prepare an array of appropriate size to be returned
		HotbarSlot[] array = new HotbarSlot[slots.size()];
		return slots.toArray(array);
	}

	@Override
	public Direction getDirection() {
		int yaw = (int) (player.rotationYawHead);
		yaw += (yaw >= 0) ? 45 : -45;
		yaw /= 90;
		// System.out.println("[Player.getDirection] yaw=" + yaw);
		int modulus = yaw % 4;
		// System.out.println("[Player.getDirection] modulus=" + modulus);
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
		// System.out.println("[Player.getDirection] direction=" + direction);
		return direction;
	}

	@Override
	public HotbarDistribution getHotbarDistribution() {
		return new HotbarDistribution(getHotbar());
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

}
