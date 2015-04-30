package ds.plato.undo;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;

import ds.plato.api.ISelect;
import ds.plato.api.IWorld;
import ds.plato.select.Selection;
import ds.plato.select.SelectionManager;
import ds.plato.test.PlatoTest;

public class UndoableSetBlockTest extends PlatoTest {
	
	IWorld w;
	ISelect sm;
	
	@Before
	public void setUp() {
		super.setUp();
		w = newStubWorld();
		sm = new SelectionManager(blockSelected);
	}

	@Test
	public void set() {		
		new UndoableSetBlock(w, sm, p1, sand).set();
		Block b = w.getBlock(p1);
		assertEquals(blockSelected, b);
	}
	
	@Test
	public void set_selectedAfterSet() {		
		new UndoableSetBlock(w, sm, p1, sand).set();
		Selection s = sm.getSelection(p1);
		assertEquals(sand, s.getBlock());
	}

	@Test
	public void set_airNotSelected() {		
		new UndoableSetBlock(w, sm, p1, air).set();
		assertThat("Air is not selected", sm.getSelection(p1), is(nullValue()));
	}

	@Test
	public void undo() {
		UndoableSetBlock undoable = new UndoableSetBlock(w, sm, p1, sand).set();
		undoable.undo();
		assertEquals(air, w.getBlock(p1));
	}

	@Test
	public void redo() {
		UndoableSetBlock undoable = new UndoableSetBlock(w, sm, p1, sand).set();
		undoable.undo();
		undoable.redo();
		assertEquals(sand,  w.getBlock(p1));
	}

}
