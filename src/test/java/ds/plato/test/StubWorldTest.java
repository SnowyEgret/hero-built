package ds.plato.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ds.plato.api.IWorld;

public class StubWorldTest extends PlatoTest {

	IWorld world;

	@Before
	public void setUp() {
		super.setUp();
		world = newStubWorld();
	}

	@Test
	public void setBlock_getBlock() {
		Block blockSet = sand;
		world.setBlock(BlockPos.ORIGIN, blockSet);
		Block blockGot = world.getBlock(BlockPos.ORIGIN);
		//int metadata = world.getMetadata(BlockPos.ORIGIN);
		assertEquals(blockSet, blockGot);
	}

	@Test
	public void getBlock_atPointNotSetReturnsAir() {
		assertEquals(air, world.getBlock(BlockPos.ORIGIN));
	}

//	@Test
//	public void getBlockMetadata_atPointNotSetReturns0() {
//		int metadata = world.getMetadata(1, 2, 3);
//		assertThat(metadata, equalTo(0));
//	}

}
