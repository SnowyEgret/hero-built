package ds.plato.staff;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ds.plato.api.ISpell;
import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.item.staff.StaffSelect;
import ds.plato.pick.Pick;
import ds.plato.test.PlatoTest;

public class StaffSelectTest extends PlatoTest {

	@Mock PlayerInteractEvent mockEvent;
	@Mock SpellDelete mockDelete;
	@Mock SpellCopy mockMove;
	StaffSelect staff;
	ItemStack stack;

	@Before
	public void setUp() {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		Pick[] picks = new Pick[] { new Pick(new BlockPos(1, 1, 1), dirt, null), new Pick(new BlockPos(2, 2, 2), dirt, null) };
		when(pickManager.getPicks()).thenReturn(picks);
		when(pickManager.isFinishedPicking()).thenReturn(true);
		List spells = new ArrayList();
		spells.add(mockDelete);
		spells.add(mockMove);
		staff = new StaffSelect(pickManager, spells);
	}

	@Test
	public void nextSpell_setsCurrentSpell() {
		staff.nextSpell(stack);
		assertEquals(mockMove, staff.getSpell(stack));
		staff.nextSpell(stack);
		assertEquals(mockDelete, staff.getSpell(stack));
	}

	@Test
	public void nextSpell_startsAtBeginningWhenReachesEnd() {
		assertEquals(mockMove, staff.nextSpell(stack));
		assertEquals(mockDelete, staff.nextSpell(stack));
		assertEquals(mockMove, staff.nextSpell(stack));
	}

	@Test
	public void nextSpell_pickManagerResetToNumPicksOfSpell() {
		assertEquals(mockMove, staff.nextSpell(stack));
		verify(pickManager).reset(mockMove.getNumPicks());
		assertEquals(mockDelete, staff.nextSpell(stack));
		verify(pickManager, times(2)).reset(mockDelete.getNumPicks());
	}

	@Test
	public void previousSpell_startsAtEndWhenReachesBeginning() {
		assertEquals(mockMove, staff.previousSpell(stack));
		assertEquals(mockDelete, staff.previousSpell(stack));
		assertEquals(mockMove, staff.previousSpell(stack));
	}

	@Test
	public void nextSpell_pickMangerIsReset() {
		ISpell s = staff.nextSpell(stack);
		verify(pickManager).reset(s.getNumPicks());
		s = staff.nextSpell(stack);
		verify(pickManager, times(2)).reset(s.getNumPicks());
	}

	@Test
	public void previousSpell() {
		staff.previousSpell(stack);
		assertEquals(mockMove, staff.getSpell(stack));
		staff.previousSpell(stack);
		assertEquals(mockDelete, staff.getSpell(stack));
		staff.nextSpell(stack);
		assertEquals(mockMove, staff.getSpell(stack));
		staff.nextSpell(stack);
		assertEquals(mockDelete, staff.getSpell(stack));

	}
}
