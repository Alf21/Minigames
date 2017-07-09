package alf21.minigames.textdraws;

import alf21.minigames.Minigames;
import alf21.minigames.engine.GameEngine;
import alf21.minigames.games.TikTakToe.TikTakToe;
import alf21.minigames.lifecycles.Lifecycle;
import me.alf21.textdrawsystem.TextdrawSystem;
import me.alf21.textdrawsystem.content.components.list.List;
import me.alf21.textdrawsystem.dialogs.layouts.DialogLayout;
import me.alf21.textdrawsystem.dialogs.types.Panel;
import me.alf21.textdrawsystem.panelDialog.PanelDialog;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.object.Player;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class Welcome {

	private Player player;
	private Panel panel;

	public Welcome(Player player) {
		this.player = player;
		create();
	}

	private void create() {
		panel = TextdrawSystem.createPanel(player);
		TextdrawSystem.setPanel(player, panel);
		panel.setDialogLayout(DialogLayout.FORM);
		panel.getLeftButton().setText("Back");
		panel.getRightButton().setText("Next");

		PanelDialog intro = TextdrawSystem.createPanelDialog(player);
		intro.setCaption("Minigames Gamemode");
		intro.setMessage("Welcome to the Minigames Gamemode.\nIf you wanna test this gamemode, continue by clicking >> NEXT <<");
		intro.toggleCancelButton(false);
		intro.setCloseHandler(dialog -> player.kick());
		intro.setClickOkHandler((handler, componentDataCollection) -> createGameSelection());
		intro.show();
	}

	private void createGameSelection() {
		PanelDialog gameSelection = TextdrawSystem.createPanelDialog(player);
		gameSelection.setCaption("Game Selection");
		List list = gameSelection.addList("game_selection");
		// TikTakToe
		list.addListItem("Tik Tak Toe").setClickHandler(handler -> {
			try {
				GameEngine gameEngine = new GameEngine();
				Minigames.getInstance().getPlayerLifecycleHolder().getObject(player, Lifecycle.class).setGameEngine(gameEngine);
				gameEngine.initGame(TikTakToe.class, player);
			} catch (IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		});
		gameSelection.toggleOkButton(false);
		gameSelection.setClickCancelHandler(handler -> new Welcome(player));
		gameSelection.setCloseHandler(handler -> new Welcome(player));
		gameSelection.show();
	}

	// TODO colorPicker for the own Color

}
