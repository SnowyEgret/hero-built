package ds.plato.item.spell.select;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import net.minecraft.util.BlockPos;

import org.junit.Test;

import ds.plato.test.PlatoTest;

public class SelectTest extends PlatoTest {

	@Test
	public void concat() {
		BlockPos[] a = new BlockPos[]{p0, p0};
		BlockPos[] b = new BlockPos[]{p1, p1};
		BlockPos[] c = new BlockPos[]{p2, p2};
		BlockPos[] array = Select.concat(a, b, c);
		assertThat(array.length, equalTo(3));
	}

	@Test
	public void all() {
		BlockPos[] positions = Select.all;
		assertThat(positions.length, equalTo(27));
	}

	@Test
	public void allNoCorners() {
		BlockPos[] positions = Select.allNoCorners;
		assertThat(positions.length, equalTo(19));
	}

	@Test
	public void EW() {
		BlockPos[] positions = Select.EW;
		assertThat(positions.length, equalTo(9));
	}

	@Test
	public void NS() {
		BlockPos[] positions = Select.NS;
		assertThat(positions.length, equalTo(9));
	}

	@Test
	public void horizontal() {
		BlockPos[] positions = Select.horizontal;
		assertThat(positions.length, equalTo(9));
	}

	@Test
	public void horizontalNoCorners() {
		BlockPos[] positions = Select.horizontalNoCorners;
		assertThat(positions.length, equalTo(5));
	}

	@Test
	public void above() {
		BlockPos[] positions = Select.above;
		assertThat(positions.length, equalTo(9));
	}

	@Test
	public void aboveInclusive() {
		BlockPos[] positions = Select.aboveInclusive;
		assertThat(positions.length, equalTo(18));
	}

	@Test
	public void below() {
		BlockPos[] positions = Select.below;
		assertThat(positions.length, equalTo(9));
	}

	@Test
	public void belowInclusive() {
		BlockPos[] positions = Select.belowInclusive;
		assertThat(positions.length, equalTo(18));
	}

	@Test
	public void up() {
		BlockPos[] positions = Select.up;
		assertThat(positions.length, equalTo(1));
	}

	@Test
	public void down() {
		BlockPos[] positions = Select.down;
		assertThat(positions.length, equalTo(1));
	}

}
