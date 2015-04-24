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

public class SetBlockTest extends PlatoTest {
	
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
		new SetBlock(w, sm, BlockPos.ORIGIN, sand).set();
		Block b = w.getBlock(BlockPos.ORIGIN);
		assertEquals(blockSelected, b);
	}
	
	@Test
	public void set_selectedAfterSet() {		
		new SetBlock(w, sm, BlockPos.ORIGIN, sand).set();
		Selection s = sm.getSelection(BlockPos.ORIGIN);
		assertEquals(sand, s.getBlock());
	}

	@Test
	public void set_airIsNotSelected() {		
		new SetBlock(w, sm, BlockPos.ORIGIN, air).set();
		assertThat("Air is not selected", sm.getSelection(BlockPos.ORIGIN), is(nullValue()));
	}

	@Test
	public void undo() {
		SetBlock undoable = new SetBlock(w, sm, BlockPos.ORIGIN, sand).set();
		undoable.undo();
		assertEquals(dirt, w.getBlock(BlockPos.ORIGIN));
	}

	@Test
	public void redo() {
		SetBlock undoable = new SetBlock(w, sm, BlockPos.ORIGIN, sand).set();
		undoable.undo();
		undoable.redo();
		assertEquals(sand,  w.getBlock(BlockPos.ORIGIN));
	}

}
