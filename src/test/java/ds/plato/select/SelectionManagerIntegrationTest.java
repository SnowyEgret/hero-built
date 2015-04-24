package ds.plato.select;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import net.minecraft.util.BlockPos;

import org.junit.Test;

import ds.plato.test.PlatoIntegrationTest;

public class SelectionManagerIntegrationTest extends PlatoIntegrationTest {

	@Test
	public void select() {
		selectionManager.select(world, BlockPos.ORIGIN);
		assertThat(world.getBlock(BlockPos.ORIGIN), equalTo(blockSelected));
	}

	@Test
	public void deselect() {		
		selectionManager.select(world, BlockPos.ORIGIN);
		selectionManager.deselect(BlockPos.ORIGIN);
		assertThat(world.getBlock(BlockPos.ORIGIN), equalTo(dirt));
		//assertThat(world.getMetadata(BlockPos.ORIGIN), equalTo(0));
	}

}
