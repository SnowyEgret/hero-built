package ds.plato.test;

import static org.mockito.Mockito.when;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;

public class PlatoTest {

	@Mock protected BlockDirt dirt;
	@Mock protected BlockSand sand;
	@Mock protected BlockSand clay;
	@Mock protected BlockAir air;
	@Mock protected Item bucket;
	@Mock protected BlockSelected blockSelected;
	@Mock protected BlockPicked blockPicked;
	@Mock protected IWorld world;
	@Mock protected ISelect selectionManager;
	@Mock protected IPick pickManager;
	@Mock protected IUndo undoManager;
	@Mock protected EntityPlayer player;
	@Mock protected InventoryPlayer inventory;
	@Mock protected Configuration config;
	//@Mock protected BlockPos.O
	//@Mock protected Property property;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(dirt.getLocalizedName()).thenReturn("dirt");
		when(sand.getLocalizedName()).thenReturn("sand");
		when(clay.getLocalizedName()).thenReturn("clay");
		//TODO class Item does not have method getLocalizedName. What is called instead? Used in T_Spell.getRecipe
		when(bucket.toString()).thenReturn("bucket");
	}

	//Returns a StubWorld initialized with a mock air block
	protected IWorld newStubWorld() {
		return new StubWorld(air);
	}

//	protected Provider<IWorld> newMockWorldProvider() {
//		return new Provider() {
//			@Override
//			public Object get() {
//				return newStubWorld();
//			}
//		};
//	}
}
