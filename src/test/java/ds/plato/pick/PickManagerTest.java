package ds.plato.pick;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;

import ds.plato.test.PlatoTest;

public class PickManagerTest extends PlatoTest {

	PickManager m;
	BlockPos origin;

	@Before
	public void setUp() {
		super.setUp();
		m = new PickManager();
		m.reset(3);
		origin = new BlockPos(0,0,0);
	}

	@Test
	public void pick() {
		Pick p1 = m.addPick(origin, dirt, null);
		assertThat(m.getPick(0), is(p1));
	}

	@Test
	public void reset() {
		m.addPick(origin, dirt, null);
		m.reset(3);
		for (int i = 0; i < 6; i++) {
			m.addPick(new BlockPos(i, 0, 0), dirt, null);
		}
		assertThat(m.size(), is(3));
	}

	@Test
	public void getPickAt() {
		BlockPos pos = new BlockPos(1, 0, 0);
		Pick p = m.addPick(pos, dirt, null);
		assertThat(m.getPickAt(pos), is(p));
	}

	@Test
	public void getPick() {
		Pick p = m.addPick(new BlockPos(1, 0, 0), dirt, null);
		assertThat(m.getPick(0), equalTo(p));
	}

	@Test
	public void isFinishedPicking() {
		m.reset(2);
		m.addPick(new BlockPos(1, 0, 0), dirt, null);
		assertThat(m.isFinishedPicking(), is(false));
		m.addPick(new BlockPos(2, 0, 0), dirt, null);
		assertThat(m.isFinishedPicking(), is(true));
	}

	@Test
	public void getPicksArray() {
		Pick p0 = m.addPick(new BlockPos(0, 0, 0), dirt, null);
		Pick p1 = m.addPick(new BlockPos(1, 0, 0), dirt, null);
		Pick[] picks = m.getPicks();
		assertEquals(p0, picks[0]);
		assertEquals(p1, picks[1]);
	}

}
