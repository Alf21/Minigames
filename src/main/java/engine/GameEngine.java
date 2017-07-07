package engine;

import games.GameType_Multiplayer;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.object.Player;
import textdraws.Welcome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class GameEngine {

	private int round;
	private Player currentPlayer;
	private GameType_Multiplayer game;
	private PlayerLifecycleHolder playerLifecycleHolder;
	private Player winner;
	private List<Player> players;

	public GameEngine() {
		round = 0;
	}

	public void initGame(Class<?> gameClass, Player host, PlayerLifecycleHolder playerLifecycleHolder) throws IllegalAccessException, InstantiationException {
		game = (GameType_Multiplayer) gameClass.newInstance();
		game.init(host, this);
		this.playerLifecycleHolder = playerLifecycleHolder;
	}

	public void startGame() {
		game.onGameBegins();
	}

	public void finishGame() {
		game.onGameFinished();
		for (Player player : getPlayers()) {
			new Welcome(player, new GameEngine(), playerLifecycleHolder);
		}
	}

	public void nextRound(Player player) {
		currentPlayer = player;
		round++;
	}

	public int getRound() {
		return round;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public GameType_Multiplayer getGame() {
		return game;
	}

	public PlayerLifecycleHolder getPlayerLifecycleHolder() {
		return playerLifecycleHolder;
	}

	public boolean checkGameIsFinished() {
		return game.isAbleToFinish();
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
