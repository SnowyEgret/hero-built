package ds.plato.item.spell.select;

import static org.junit.Assert.*;

import java.util.Arrays;

import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;

import ds.plato.item.spell.select.Select;
import ds.plato.test.PlatoTest;

public class SelectTest extends PlatoTest {

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void concat() {
		BlockPos[] a = new BlockPos[]{p0, p0};
		BlockPos[] b = new BlockPos[]{p1, p1};
		BlockPos[] c = new BlockPos[]{p2, p2};
		BlockPos[] array = Select.concatArray(a, b, c);
		System.out.println(Arrays.toString(array));
	}

	@Test
	public void all() {
		BlockPos[] positions = Select.all();
		System.out.println(Arrays.toString(positions));
	}

}
