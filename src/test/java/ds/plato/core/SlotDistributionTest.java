package ds.plato.core;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ds.plato.test.PlatoTest;

public class SlotDistributionTest extends PlatoTest {
	
	@Test
	public void test() {
		HotbarSlot[] l = new HotbarSlot[3];
		l[0] = new HotbarSlot(dirt, 0, 2);
		l[1] = new HotbarSlot(sand, 0, 5);
		l[2] = new HotbarSlot(clay, 0, 7);
		HotbarDistribution d = new HotbarDistribution(l);
		int sum = 0;
		for (Integer p : d.getPecentages()) {
			sum += p;
		}
		System.out.println("[T_SlotDistribution.test] d=" + d);
		assertThat(sum, equalTo(100));
	}
}
