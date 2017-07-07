package games.FourWins;

import engine.GameEngine;
import games.GameType_Multiplayer;
import lifecycles.Lifecycle;
import me.alf21.textdrawsystem.TextdrawSystem;
import me.alf21.textdrawsystem.container.Container;
import me.alf21.textdrawsystem.content.components.ComponentData;
import me.alf21.textdrawsystem.content.components.button.Button;
import me.alf21.textdrawsystem.dialogs.types.Panel;
import me.alf21.textdrawsystem.panelDialog.PanelDialog;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.object.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class TikTakToe extends GameType_Multiplayer {

	private static final int MAX_PLAYERS = 2;
	private int[][] field = new int[3][3];
	private Vector2D lastMove;

	public TikTakToe(Player host, GameEngine gameEngine) {
		super(host, gameEngine);
	}

	@Override
	protected void initGame(PanelDialog panelDialog) {
		if (getPlayers().size() != 1) {
			error_invalidPlayerAmount(getHost(), getPlayers(), MAX_PLAYERS);
			panelDialog.show();
		}
		else createGame();
	}

	@Override
	public void onGameFinished() {
		getGameEngine().getPlayers().forEach(player -> player.sendMessage("The game has been finished!"));
		if (getGameEngine().getWinner() == null) {
			getGameEngine().getPlayers().forEach((player -> player.sendMessage("Nobody have won the game...")));
		}
		else {
			getGameEngine().getPlayers().forEach((player -> player.sendMessage(getGameEngine().getWinner().getName() + " has won the game!")));
		}
	}

	@Override
	public void onGameBegins() {
		getGameEngine().getPlayers().forEach(player -> player.sendMessage("The game has been begun!"));
	}

	@Override
	public boolean isAbleToFinish() {
		if (getGameEngine().getRound() >= 8) return true;

		// 0 = none
		// 1 = host
		// 2 = guest

		int winner = checkWinCondition((int) lastMove.getY(), (int) lastMove.getX());
		if (winner != 0) {
			getGameEngine().setWinner((winner == 1) ? getHost() : (Player) getPlayers().toArray()[0]);
			return true;
		}
		return false;
	}

	private int checkWinCondition(int x, int y) {
		// algorithm by https://stackoverflow.com/questions/1056316/algorithm-for-determining-tic-tac-toe-game-over
		int n = field.length;
		int s = (getGameEngine().getCurrentPlayer() == getHost()) ? 1 : 2;
		//check end conditions

		//check col
		for(int i = 0; i < n; i++){
			if(field[x][i] != s)
				break;
			if(i == n-1){
				return s;
			}
		}

		//check row
		for(int i = 0; i < n; i++){
			if(field[i][y] != s)
				break;
			if(i == n-1){
				return s;
			}
		}

		//check diag
		if(x == y){
			//we're on a diagonal
			for(int i = 0; i < n; i++){
				if(field[i][i] != s)
					break;
				if(i == n-1){
					return s;
				}
			}
		}

		//check anti diag (thanks rampion)
		if(x + y == n - 1){
			for(int i = 0;i<n;i++){
				if(field[i][(n-1)-i] != s)
					break;
				if(i == n-1){
					return s;
				}
			}
		}

		//check draw
		if(getGameEngine().getRound() == (n^2 - 1)){
			return 0;
		}
		return 0;
	}

	private void createGame() {
		for (Player player : getGameEngine().getPlayers()) {
			Container container = createTextdraws(player);
		}
		getGameEngine().startGame();
		for (Player player : getGameEngine().getPlayers()) {
			getGameEngine().getPlayerLifecycleHolder().getObject(player, Lifecycle.class).getContainer().show();
		}
		List<Player> players = new ArrayList<>(getPlayers());
		players.add(getHost());
		getGameEngine().setPlayers(players);
	}

	private Container createTextdraws(Player player) {
		Panel panel = TextdrawSystem.getPanel(player);
		Container container = Container.create(player, "FourWins");
		Vector2D v2 = panel.getLayout().getSlot(panel.getContent(), 0, 0);
		float startX = v2.getX();
		float startY = v2.getY();
		int c = 0;
		for (int[] y : field) {
			for (int x : y) {
				Button button = container.createButton(startX + x * 20.0f, startY + c * 20.0f, 20.0f, "_", "field_"+c+x);
				button.toggleActivationEffect(false);
				button.setClickHandler(handler -> {
					if (button.getComponentData() == null && getGameEngine().getCurrentPlayer() == player) {
						toggleButtonForPlayers(button.getName(), getGameEngine().getPlayers(), player);
						if (!getGameEngine().checkGameIsFinished())
							getGameEngine().nextRound(getGameEngine().getCurrentPlayer() == player ? (Player) getPlayers().toArray()[0] : player);
					}
				});
			}
			c++;
		}
		return container;
	}

	private void toggleButtonForPlayers(String name, List<Player> players, Player actionPlayer) {
		for (Player player : players) {
			Lifecycle lifecycle = getGameEngine().getPlayerLifecycleHolder().getObject(player, Lifecycle.class);
			Button button = (Button) lifecycle.getContainer().getComponent("name");
			button.setComponentData((ComponentData<Player>) actionPlayer);
			button.getButtonTextdraw().setBoxColor(lifecycle.getColor());
			int y = Integer.parseInt(button.getName().split("_")[1]);
			int x = Integer.parseInt(button.getName().split("_")[2]);
			field[y][x] = (actionPlayer == getHost()) ? 1 : 2;
			lastMove = new Vector2D(x, y);
		}
	}

}
