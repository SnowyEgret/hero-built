package ds.plato.util;

import static org.hamcrest.Matchers.equalTo;
import net.minecraft.util.BlockPos;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

import ds.plato.test.PlatoTest;

public class ExtendedBlockPosTest extends PlatoTest {

	private ExtendedBlockPos pos;

	@Before
	public void setUp() {
		super.setUp();
		pos = new ExtendedBlockPos(p0);
	}

	@Test
	public void constructor() {
		System.out.println(pos);
		//assertThat(pos.exPos, containsInAnyOrder(p0, p1));
	}

	@Test
	public void ground() {
		Iterable<BlockPos> positions = pos.ground();
		System.out.println(Iterables.toString(positions));
		assertThat(Iterables.size(positions), equalTo(9));
		Iterable expected = BlockPos.getAllInBox(new BlockPos(-1, 0, -1), new BlockPos(1, 0, 1));
		//Hamcrest drives me ...
//		for(BlockPos p : positions) {
//			assertThat(expected, IterableContains(p));
//		}
		//assertThat(positions, IsIterableContainingInOrder.contains(Iterables.toArray(expected, BlockPos.class)));
		//assertEquals(positions, BlockPos.getAllInBox(new BlockPos(-1, 0, -1), new BlockPos(1, 0, 1)));
		//assertThat(positions, containsInAnyOrder(BlockPos.getAllInBox(new BlockPos(-1, 0, -1), new BlockPos(1, 0, 1))));
	}

	private void assertThat(int size, Matcher<Integer> equalTo) {
		// TODO Auto-generated method stub
		
	}

}
