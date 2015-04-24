package ds.plato.item.spell.other;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import ds.plato.Plato;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.gui.ITextSetable;
import ds.plato.item.spell.Spell;
import ds.plato.pick.Pick;
import ds.plato.undo.SetBlock;
import ds.plato.undo.Transaction;

public class SpellText extends Spell implements ITextSetable {

	private Font font;
	private IWorld world;
	private HotbarSlot[] slotEntries;
	private Pick[] picks;
	private Graphics graphics;

	public SpellText(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		int fontSize = 24;
		String fontName = "Arial";
		int fontStyle = Font.PLAIN;
		font = new Font(fontName, fontStyle, fontSize);
		// BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		graphics = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).getGraphics();
		// font = font.deriveFont(32);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', Items.feather, 'B', Items.dye };
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		this.world = world;
		this.slotEntries = slotEntries;
		Minecraft.getMinecraft().thePlayer.openGui(Plato.instance, 2, world.getWorld(), 0, 0, 0);
		picks = pickManager.getPicks();
		// Clear the picks because player may have cancelled
		pickManager.clearPicks();
	}

	@Override
	public void setText(String text) {

		// Using slick2k (slick.jar not slick-util.jar)
		// UnicodeFont uniFont = new UnicodeFont(font);
		// uniFont.addAsciiGlyphs();
		// uniFont.addGlyphs(0x3040, 0x30FF); // Setting the unicode Range
		// //uniFont.addGlyphs(JPN_CHAR_STR); // Setting JPN Font by String
		// // Setting the Color
		// uniFont.getEffects().add(new ColorEffect(java.awt.Color.black));
		// uniFont.getEffects().add(new ColorEffect(java.awt.Color.yellow));
		// uniFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		// try {
		// uniFont.loadGlyphs();
		// } catch (SlickException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// uniFont.drawString(100, 100, "test");

		Vector3d d = new Vector3d();
		d.sub(picks[0].point3d(), picks[1].point3d());
		double angle = new Vector3d(-1, 0, 0).angle(d);
		// AffineTransform transform = new AffineTransform();
		// transform.rotate(angleFromXAxis);
		// font = font.deriveFont(transform);
		System.out.println("[SpellText.setText] angle=" + angle);

		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, graphics);
		System.out.println("[SpellText.setText] rectangle=" + r);

		double hyp = Math.sqrt(Math.pow(r.getWidth(), 2) + Math.pow(r.getHeight(), 2));
		int width = (int) hyp * 2;
		int height = (int) width;

		// BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(font);
		Graphics2D g2 = (Graphics2D) g;
		// graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawRect(0, 0, width - 1, height - 1);
		g.translate(width / 2, height / 2);
		g2.rotate(angle);
		g2.drawString(text, 0, 0);

		Set<BlockPos> points = new HashSet<>();
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				int pixel = image.getRGB(w, h);
				// if (pixel == -16777216) {
				if (pixel == -1) {
					BlockPos p = new BlockPos(w - (width / 2), 0, h - (height / 2));
					//Point3i p = new Point3i(w - (width / 2), h - (height / 2), 0);
					p.add(picks[0].getPos());
					points.add(p);
				}
			}
		}

		System.out.println("[SpellText.invoke] points.size()=" + points.size());
		selectionManager.clearSelections();
		Transaction t = undoManager.newTransaction();
		for (BlockPos p : points) {
			t.add(new SetBlock(world, selectionManager, p, slotEntries[0].block).set());
		}
		t.commit();
		selectionManager.clearSelections();
		pickManager.clearPicks();
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

}
