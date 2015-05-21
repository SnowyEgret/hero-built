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

import ds.plato.world.IWorld;

public class StubWorldTest extends PlatoTest {

	IWorld w;

	@Before
	public void setUp() {
		super.setUp();
		w = newStubWorld();
	}

	@Test
	public void setBlock_getBlock() {
		Block blockSet = sand;
		w.setBlock(p0, blockSet);
		Block blockGot = w.getBlock(p0);
		assertEquals(blockSet, blockGot);
	}

	@Test
	public void getBlock_atPointNotSetReturnsAir() {
		assertEquals(air, w.getBlock(p0));
	}

	@Test
	public void setBlock_twice() {
		w.setBlock(p0, sand);
		w.setBlock(p0, dirt);
		assertEquals(dirt, w.getBlock(p0));
	}

//	@Test
//	public void getBlockMetadata_atPointNotSetReturns0() {
//		int metadata = world.getMetadata(1, 2, 3);
//		assertThat(metadata, equalTo(0));
//	}

}
