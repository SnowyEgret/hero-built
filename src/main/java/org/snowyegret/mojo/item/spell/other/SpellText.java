package org.snowyegret.mojo.item.spell.other;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Vector3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.ITextInput;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.message.client.OpenGuiMessage;
import org.snowyegret.mojo.message.client.SpellMessage;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public class SpellText extends Spell implements ITextInput {

	private Graphics graphics;
	//TODO Move this to player
	private Font font;

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
	public void invoke(Player player) {
		// We are on the server thread and GuiSpellText doesn't have a container
		player.sendMessage(new OpenGuiMessage(GuiHandler.GUI_SPELL_TEXT));
		// Clear the picks because player may have cancelled
		player.clearPicks();
	}

	@Override
	public void setText(String text, Player player) {

		Pick[] picks = player.getPicks();
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
		// g2.drawRect(0, 0, width - 1, height - 1);
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
		player.clearSelections();
		player.clearPicks();

		IBlockState b = player.getHotbar().firstBlock();
		List<IUndoable> undoables = Lists.newArrayList();
		for (BlockPos p : positions) {
			undoables.add(new UndoableSetBlock(p, player.getWorld().getState(p), b));
		}
		player.getTransactionManager().doTransaction(undoables);
	}

	@Override
	public void cancel(Player player) {
		player.clearPicks();
		player.clearSelections();
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}
}
