package ds.plato.spell;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.minecraftforge.fml.common.registry.GameRegistry;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;

import ds.plato.Plato;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.SpellLoader;
import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.item.spell.select.SpellGrowAll;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.test.PlatoTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GameRegistry.class })
@PowerMockIgnore({ "javax.management.*" })
public class SpellLoaderTest extends PlatoTest {

	SpellLoader loader;

	@Before
	public void setUp() {
		super.setUp();
		PowerMockito.mockStatic(GameRegistry.class);
		loader = new SpellLoader(config, undoManager, selectionManager, pickManager, Plato.ID);
	}

	//@Test
	// public void loadSpell() throws Exception {
	// Spell spell = loader.loadSpell(SpellDelete.class);
	// assertThat(spell, instanceOf(SpellDelete.class));
	// assertThat(spell.getInfo().getName().toLowerCase(), equalTo("delete"));
	// }

	//@Test
//	public void loadSpells() throws Exception {
//		List spellClasses = Lists.newArrayList(SpellDelete.class, SpellCopy.class, SpellGrowAll.class);
//		List<Spell> spells = loader.loadSpells(spellClasses);
//		//assertThat(spells, hasItems(spellClasses));
//		for (Spell s : spells) {
//			assertThat(s, instanceOf(Spell.class));
//		}
//		assertThat(spells.size(), is(spellClasses.size()));
//	}
	
	//Fails her but not in game. Spell.class.isAssignableFrom(c) returns false
	//@Test
	public void loadSpellsFromPackage() throws Exception {
		List<Spell> spells = loader.loadSpellsFromPackage("ds.plato.spell");
		for (Spell s : spells) {
			assertThat(s, instanceOf(Spell.class));
		}
	}
}
