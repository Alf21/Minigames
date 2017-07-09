package alf21.minigames.engine;

import alf21.minigames.games.GameType_Multiplayer;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.object.Player;
import alf21.minigames.textdraws.Welcome;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class GameEngine {

	private Player host;
	private int round;
	private Player currentPlayer;
	private GameType_Multiplayer game;
	private Player winner;
	private List<Player> players;

	public GameEngine() {
		players = new ArrayList<>();
		round = 0;
	}

	public void initGame(Class<?> gameClass, Player host) throws IllegalAccessException, InstantiationException {
		this.host = host;
		game = (GameType_Multiplayer) gameClass.newInstance();
		game.init(this);
	}

	public void startGame() {
		int r = new Random().nextInt(players.size());
		currentPlayer = (Player) players.toArray()[r-1];
		game.onGameBegins();
	}

	public void finishGame() {
		game.onGameFinished();
		for (Player player : getPlayers()) {
			new Welcome(player);
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

	public boolean checkGameIsFinished() {
		if (!game.isAbleToFinish()) return false;
		finishGame();
		return true;
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

	public Player getHost() {
		return host;
	}
}
