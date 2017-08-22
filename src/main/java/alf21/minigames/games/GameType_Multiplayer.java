package alf21.minigames.games;

import alf21.minigames.engine.GameEngine;
import me.alf21.textdrawsystem.TextdrawSystem;
import me.alf21.textdrawsystem.content.components.list.ListItem;
import me.alf21.textdrawsystem.panelDialog.PanelDialog;
import net.gtaun.shoebill.object.Player;
import alf21.minigames.textdraws.Welcome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public abstract class GameType_Multiplayer {

	private GameEngine gameEngine;

	protected GameType_Multiplayer() {}


	public void init(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		getGameEngine().getPlayers().add(getGameEngine().getHost());
		PanelDialog playerSelection = TextdrawSystem.getPanel(getGameEngine().getHost()).createPanelDialog();
		me.alf21.textdrawsystem.content.components.list.List selectablePlayers = playerSelection.addList("playerSelection");
		for (Player player : Player.get()) {
			if (player == gameEngine.getHost()) continue; // Him-/Herself
			ListItem listItem = selectablePlayers.addListItem(player.getName());
			if(getGameEngine().getPlayers().contains(player))
				listItem.select();
			listItem.setClickHandler(handler -> {
				if (getGameEngine().getPlayers().contains(player)) {
					getGameEngine().getPlayers().remove(player);
					listItem.unselect();
				}
				else {
					getGameEngine().getPlayers().add(player);
					listItem.select();
				}
			});
		}
		playerSelection.setClickCancelHandler(handler -> new Welcome(getGameEngine().getHost()));
		playerSelection.setClickOkHandler((handler, data) -> initGame(playerSelection));
		playerSelection.show();
	}

	protected abstract void initGame(PanelDialog panelDialog);

	public abstract void onGameFinished();

	public abstract void onGameBegins();

	public abstract boolean isAbleToFinish();

	public static void error_invalidPlayerAmount(Player player, List<Player> players, int neededAmount) {
		if (players.size() + 1 != neededAmount) {
			String string = (players.size() + 1 > neededAmount) ? (players.size() - neededAmount) + " less " : ((players.size() - neededAmount + 1) * -1) + " more ";
			player.sendMessage("In this Game, just 2 Players are supported. Please select " + string + "player(s)." );
		}
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}
}
