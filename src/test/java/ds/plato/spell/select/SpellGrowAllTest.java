package ds.plato.spell.select;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lwjgl.input.Keyboard;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ds.plato.api.ISelect;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Spell;
import ds.plato.pick.Pick;
import ds.plato.select.SelectionManager;
import ds.plato.test.PlatoTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Keyboard.class })
@PowerMockIgnore({ "javax.management.*" })
public class SpellGrowAllTest extends PlatoTest {

//	private SlotEntry[] slotEntries;
//	private Pick[] picks;
//	ISelect sm;
//	IWorld stubWorld;
//	Spell growSpell;
//
//	@Before
//	public void setUp() {
//		super.setUp();
//		PowerMockito.mockStatic(Keyboard.class);
//		slotEntries = new SlotEntry[] {new SlotEntry(dirt, 0, 0)};
//		picks = new Pick[] {new Pick(0, 0, 0, dirt, 0, 0), new Pick(9, 0, 0, dirt, 0, 0)};
//		stubWorld = newStubWorld();
//		sm = new SelectionManager(blockSelected);//.setWorld(stubWorld);
//		growSpell = new SpellGrowAll(undoManager, sm, pickManager);
//	}
//
//	@Test
//	public void invoke_grow() {
//		sm.select(stubWorld, 0, 0, 0);
//		when(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)).thenReturn(false);
//		//growSpell.invoke(picks, slotEntries);
//		//TODO set up pickManager
//		growSpell.invoke(stubWorld, slotEntries);
//		assertThat(sm.size(), equalTo(27));
//	}
//
//	@Test
//	public void invoke_shrink() {
//		sm.select(stubWorld, 0, 0, 0);
//		when(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)).thenReturn(false);
//		//growSpell.invoke(picks, slotEntries);
//		growSpell.invoke(stubWorld, slotEntries);
//		when(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)).thenReturn(true);
//		//growSpell.invoke(picks, slotEntries);
//		growSpell.invoke(stubWorld, slotEntries);
//		assertThat(sm.size(), equalTo(1));
//	}
//
}
