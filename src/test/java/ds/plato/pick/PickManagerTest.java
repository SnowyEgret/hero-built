package ds.plato.pick;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.junit.Before;
import org.junit.Test;

import ds.plato.test.PlatoTest;

public class PickManagerTest extends PlatoTest {

	IPick m;

	@Before
	public void setUp() {
		super.setUp();
		m = new PickManager(blockPicked, selectionManager);
		m.reset(3);
	}

//	@Test
//	public void addPick() {
//		Pick p1 = m.addPick(origin, dirt, null);
//		assertThat(m.getPicks()[0], is(p1));
//	}

	// Interface IPick ---------------------------------------------------------

	@Test
	public void pick() {
		Pick p = m.pick(world, p1, EnumFacing.DOWN);
		assertThat(m.getPicks()[0], is(p));
	}

	@Test
	public void getPicks() {
		Pick p = m.pick(world, p1, EnumFacing.DOWN);
		assertThat(m.getPicks()[0], equalTo(p));
	}
	
	@Test
	public void getPickAt() {
		Pick p = m.pick(world, p1, EnumFacing.DOWN);
		assertThat(m.getPick(p1), is(p));
	}

	@Test
	public void clearPicks() {
		Pick p = m.pick(world, p1, EnumFacing.DOWN);
		m.clearPicks(world);
		assertThat(m.getPicks().length, is(0));
	}

	@Test
	public void reset() {
		m.pick(world, p1, EnumFacing.DOWN);
		m.reset(3);
		for (int i = 0; i < 6; i++) {
			m.pick(world, new BlockPos(i, 0, 0), EnumFacing.DOWN);
		}
		assertThat(m.getPicks().length, is(3));
	}

	@Test
	public void isPicking() {
		m.reset(2);
		m.pick(world, p1, EnumFacing.DOWN);
		assertThat(m.isPicking(), is(true));
		m.pick(world, p2, EnumFacing.DOWN);
		assertThat(m.isPicking(), is(false));
	}

	@Test
	public void isFinishedPicking() {
		m.reset(2);
		m.pick(world, p1, EnumFacing.DOWN);
		assertThat(m.isFinishedPicking(), is(false));
		m.pick(world, p2, EnumFacing.DOWN);
		assertThat(m.isFinishedPicking(), is(true));
	}

	@Test
	public void lastPick() {
		m.reset(2);
		m.pick(world, p1, EnumFacing.DOWN);
		Pick pick = m.pick(world, p2, EnumFacing.DOWN);
		assertThat(m.lastPick(), is(pick));
	}
}
