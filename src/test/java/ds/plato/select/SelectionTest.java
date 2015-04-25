package ds.plato.select;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import javax.vecmath.Point3d;

import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;

import ds.plato.test.PlatoTest;

public class SelectionTest extends PlatoTest {

	@Test
	public void point3d() {
		Selection s = new Selection(p1, dirt);
		assertThat(s.point3d(), equalTo(new Point3d(1,1,1)));
	}

	@Test
	public void getPos() {
		Selection s = new Selection(p1, dirt);
		assertThat(s.getPos(), equalTo(p1));
	}

	@Test
	public void getBlock() {
		Selection s = new Selection(p1, sand);
		//FIXME Mokito is set up in PlatoTest to return string "sand" when getUnlocalizedName is called
		//or.. understand this why assertThat won't accept Matcher
		//assertThat(s.getBlock().getUnlocalizedName(), equalTo("sand"));
		//assertThat(sand.getUnlocalizedName(), equalTo("sand"));
	}

}
