package ds.plato.test;

import static org.mockito.Mockito.when;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Configuration;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class PlatoTest {

	//@Mock protected BlockDirt dirt;
//	@Mock protected BlockSand sand;
//	@Mock protected BlockSand clay;
//	@Mock protected BlockAir air;
	@Mock protected IBlockState dirt;
	@Mock protected IBlockState sand;
	@Mock protected IBlockState clay;
	@Mock protected IBlockState air;
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
	//@Mock protected Property property;
	protected BlockPos p0, p1, p2, p3;

	@Before
	public void setUp() {
		p0 = new BlockPos(0,0,0);
		p1 = new BlockPos(1,1,1);
		p2 = new BlockPos(2,2,2);
		p3 = new BlockPos(3,3,3);
		MockitoAnnotations.initMocks(this);
//		when(dirt.getBlock().getLocalizedName()).thenReturn("dirt");
//		when(sand.getBlock().getLocalizedName()).thenReturn("sand");
//		when(clay.getBlock().getLocalizedName()).thenReturn("clay");
		//TODO class Item does not have method getLocalizedName. What is called instead? Used in T_Spell.getRecipe
//		when(bucket.toString()).thenReturn("bucket");
	}

	//Returns a StubWorld initialized with a mock air block
	protected IWorld newStubWorld() {
		return new StubWorld(air.getBlock());
		
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
