package ds.plato.item.spell.other;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Vector3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import ds.plato.gui.GuiHandler;
import ds.plato.gui.ITextSetable;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellText extends Spell implements ITextSetable {

	private Font font;
	private IWorld world;
	private IBlockState firstBlockInHotbar;
	private Pick[] picks;
	private Graphics graphics;

	public SpellText() {
		super(2);
		int fontSize = 24;
		String fontName = "Arial";
		int fontStyle = Font.PLAIN;
		font = new Font(fontName, fontStyle, fontSize);
		graphics = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).getGraphics();
		// font = font.deriveFont(32);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', Items.feather, 'B', Items.dye };
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();
		firstBlockInHotbar = player.getHotbar().firstBlock();
		player.openGui(GuiHandler.GUI_SPELL_TEXT, world);
		picks = pickManager.getPicks();
		// Clear the picks because player may have cancelled
		pickManager.clearPicks(world);
	}

	@Override
	public void setText(String text, ISelect selectionManager, IPick pickManager, IUndo undoManager) {

		Vector3d d = new Vector3d();
		d.sub(picks[0].point3d(), picks[1].point3d());
		double angle = new Vector3d(-1, 0, 0).angle(d);
		// AffineTransform transform = new AffineTransform();
		// transform.rotate(angleFromXAxis);
		// font = font.deriveFont(transform);
		System.out.println("angle=" + angle);

		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, graphics);
		System.out.println("rectangle=" + r);

		double hyp = Math.sqrt(Math.pow(r.getWidth(), 2) + Math.pow(r.getHeight(), 2));
		int width = (int) hyp * 2;
		int height = width;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(font);
		Graphics2D g2 = (Graphics2D) g;
		// graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g2.drawRect(0, 0, width - 1, height - 1);
		g.translate(width / 2, height / 2);
		g2.rotate(angle);
		g2.drawString(text, 0, 0);

		Set<BlockPos> positions = new HashSet<>();
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				int pixel = image.getRGB(w, h);
				// if (pixel == -16777216) {
				if (pixel == -1) {
					BlockPos p = new BlockPos(w - (width / 2), 0, h - (height / 2));
					p = p.add(picks[0].getPos());
					positions.add(p);
				}
			}
		}

		System.out.println("size=" + positions.size());
		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
		Transaction t = undoManager.newTransaction();
		for (BlockPos p : positions) {
			t.add(new UndoableSetBlock(world, selectionManager, p, firstBlockInHotbar).set());
		}
		t.commit();
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}
}
