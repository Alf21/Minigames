package textdraws;

import engine.GameEngine;
import games.FourWins.TikTakToe;
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
	private GameEngine gameEngine;
	private PlayerLifecycleHolder playerLifecycleHolder;

	public Welcome(Player player, GameEngine gameEngine, PlayerLifecycleHolder playerLifecycleHolder) {
		this.player = player;
		this.gameEngine = gameEngine;
		this.playerLifecycleHolder = playerLifecycleHolder;
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
		list.addListItem("Tik Tak Toe").setClickHandler(handler -> {
			try {
				gameEngine.initGame(TikTakToe.class, player, playerLifecycleHolder);
			} catch (IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		});
	}

	// TODO colorPicker for the own Color

}
