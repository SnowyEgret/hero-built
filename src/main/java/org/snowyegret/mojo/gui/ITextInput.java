package org.snowyegret.mojo.gui;

import org.snowyegret.mojo.player.Player;

public interface ITextInput {

	void setText(String text, Player player);
	
	void cancel(Player player);

}
