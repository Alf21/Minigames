package games;

import engine.GameEngine;
import me.alf21.textdrawsystem.TextdrawSystem;
import me.alf21.textdrawsystem.content.components.list.ListItem;
import me.alf21.textdrawsystem.panelDialog.PanelDialog;
import net.gtaun.shoebill.object.Player;
import textdraws.Welcome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public abstract class GameType_Multiplayer {

	private Player host;
	private List<Player> players;
	private GameEngine gameEngine;

	protected GameType_Multiplayer(Player host, GameEngine gameEngine) {
		this.host = host;
		players = new ArrayList<>();
		this.gameEngine = gameEngine;
	}


	public void init(Player host, GameEngine gameEngine) {
		PanelDialog playerSelection = TextdrawSystem.getPanel(getHost()).createPanelDialog();
		me.alf21.textdrawsystem.content.components.list.List selectablePlayers = playerSelection.addList("playerSelection");
		for (Player player : Player.get()) {
			ListItem listItem = selectablePlayers.addListItem(player.getName());
			if(getPlayers().contains(player))
				listItem.select();
			listItem.setClickHandler(handler -> {
				if (getPlayers().contains(player)) {
					getPlayers().remove(player);
					listItem.unselect();
				}
				else {
					getPlayers().add(player);
					listItem.select();
				}
			});
		}
		playerSelection.setClickCancelHandler(handler -> new Welcome(getHost(), getGameEngine(), gameEngine.getPlayerLifecycleHolder()));
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
			player.sendMessage("In this Game, just 2 Players are supported. Please select " + string + " player(s)." );
		}
	}


	public Player getHost() {
		return host;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}
}
