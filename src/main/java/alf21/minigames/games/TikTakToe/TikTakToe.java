package alf21.minigames.games.TikTakToe;

import alf21.minigames.Minigames;
import alf21.minigames.games.GameType_Multiplayer;
import alf21.minigames.lifecycles.Lifecycle;
import me.alf21.textdrawsystem.TextdrawSystem;
import me.alf21.textdrawsystem.container.Container;
import me.alf21.textdrawsystem.content.components.ComponentData;
import me.alf21.textdrawsystem.content.components.clickableTextdraw.ClickableTextdraw;
import me.alf21.textdrawsystem.dialogs.types.Panel;
import me.alf21.textdrawsystem.panelDialog.PanelDialog;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.object.Player;

import java.util.List;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class TikTakToe extends GameType_Multiplayer {

	private static final int MAX_PLAYERS = 2;
	private int[][] field = new int[3][3];
	private Vector2D lastMove;

	public TikTakToe() {
		super();
	}

	@Override
	protected void initGame(PanelDialog panelDialog) {
		panelDialog.hide();
		panelDialog.destroy();
		if (getGameEngine().getPlayers().size() != MAX_PLAYERS - 1) { //TODO rem -1
			error_invalidPlayerAmount(getGameEngine().getHost(), getGameEngine().getPlayers(), MAX_PLAYERS);
			panelDialog.show();
		}
		else createGame();
	}

	@Override
	public void onGameFinished() {
		getGameEngine().getPlayers().forEach(player -> player.sendMessage("This game has ended!"));
		if (getGameEngine().getWinner() == null) {
			getGameEngine().getPlayers().forEach((player -> player.sendMessage("Nobody has won this game...")));
		}
		else {
			getGameEngine().getPlayers().forEach((player -> player.sendMessage(getGameEngine().getWinner().getName() + " has won the game!")));
		}
		// TODO ask "new round?" -> last turn will be visible until player doesnt press "NO"
		getGameEngine().getPlayers().forEach(player -> {
			Container container = Minigames.getInstance().getPlayerLifecycleHolder().getObject(player, Lifecycle.class).getContainer();
			container.hide();
			container.destroy();
		});
	}

	@Override
	public void onGameBegins() {
		getGameEngine().getPlayers().forEach(player -> player.sendMessage("This game has begun!"));
	}

	@Override
	public boolean isAbleToFinish() {
		if (getGameEngine().getRound() >= 8) return true;

		// 0 = none
		// 1 = host
		// 2 = guest
		// 3 = draw

		int winner = checkWinCondition((int) lastMove.getY(), (int) lastMove.getX());
		if (winner != 0) {
			getGameEngine().setWinner((winner == 1) ? getGameEngine().getHost() : (Player) getGameEngine().getPlayers().toArray()[0]);
			return true;
		}
		return false;
	}

	private int checkWinCondition(int x, int y) {
		// algorithm by https://stackoverflow.com/questions/1056316/algorithm-for-determining-tic-tac-toe-game-over
		int n = field.length;
		int s = (getGameEngine().getCurrentPlayer() == getGameEngine().getHost()) ? 1 : 2;
		//check end conditions

		//check draw
		if(getGameEngine().getRound() == ((int) Math.pow(n, 2) - 1)) return 3;

		//check col
		for(int i = 0; i < n; i++) {
			if(field[x][i] != s)
				break;
			if(i == n-1) return s;
		}

		//check row
		for(int i = 0; i < n; i++) {
			if(field[i][y] != s)
				break;
			if(i == n-1) return s;
		}

		//check diag
		if(x == y){
			//we're on a diagonal
			for(int i = 0; i < n; i++) {
				if(field[i][i] != s)
					break;
				if(i == n-1) return s;
			}
		}

		//check anti diag (thanks rampion)
		if(x + y == n - 1){
			for(int i = 0; i < n; i++) {
				if(field[i][(n-1)-i] != s)
					break;
				if(i == n-1) return s;
			}
		}
		return 0;
	}

	private void createGame() {
		for (Player player : getGameEngine().getPlayers()) {
			Container container = createTextdraws(player);
			Minigames.getInstance().getPlayerLifecycleHolder().getObject(player, Lifecycle.class).setContainer(container);
		}
		getGameEngine().startGame();
		for (Player player : getGameEngine().getPlayers()) {
			Minigames.getInstance().getPlayerLifecycleHolder().getObject(player, Lifecycle.class).getContainer().show();
		}
	}

	private Container createTextdraws(Player player) {
		Panel panel = TextdrawSystem.getPanel(player);
		Container container = Container.create(player, "FourWins");
		Vector2D v2 = panel.getLayout().getSlot(panel.getContent(), 0, 0);
		float startX = v2.getX();
		float startY = v2.getY();
		int iy = 0;
		for (int[] y : field) {
			int ix = 0;
			for (int ignored : y) {
				ClickableTextdraw clickableTextdraw = container.createClickableTextdraw(startX + ix * 55.0f, startY + iy * 60.0f, 50.0f, 50.0f, Color.WHITE, "field_"+iy+"_"+ix);
				clickableTextdraw.setClickHandler(handler -> {
					if (!handler.isClicked() && getGameEngine().getCurrentPlayer() == player) {
						toggleButtonForPlayers(handler.getName(), getGameEngine().getPlayers(), player);
						if (!getGameEngine().checkGameIsFinished())
							getGameEngine().nextRound(getGameEngine().getCurrentPlayer() == player ? (Player) getGameEngine().getPlayers().toArray()[0] : player);
					}
				});
				ix++;
			}
			iy++;
		}
		return container;
	}

	private void toggleButtonForPlayers(String name, List<Player> players, Player actionPlayer) {
		for (Player player : players) {
			Lifecycle lifecycle = Minigames.getInstance().getPlayerLifecycleHolder().getObject(player, Lifecycle.class);
			ClickableTextdraw clickableTextdraw = (ClickableTextdraw) lifecycle.getContainer().getComponent(name);
			clickableTextdraw.setComponentData(new ComponentData<>(actionPlayer));
			clickableTextdraw.hide();
			clickableTextdraw.getButtonTextdraw().setBoxColor(lifecycle.getColor());
			clickableTextdraw.show();
			int y = Integer.parseInt(clickableTextdraw.getName().split("_")[1]);
			int x = Integer.parseInt(clickableTextdraw.getName().split("_")[2]);
			field[y][x] = (actionPlayer == getGameEngine().getHost()) ? 1 : 2;
			lastMove = new Vector2D(x, y);
		}
	}

}
