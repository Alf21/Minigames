package alf21.minigames.lifecycles;

import alf21.minigames.engine.GameEngine;
import me.alf21.textdrawsystem.container.Container;
import net.gtaun.shoebill.common.player.PlayerLifecycleObject;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class Lifecycle extends PlayerLifecycleObject {

	private Color color;
	private Container container;
	private GameEngine gameEngine;

	public Lifecycle(EventManager eventManager, Player player) {
		super(eventManager, player);
	}

	@Override
	protected void onInit() {

	}

	@Override
	protected void onDestroy() {

	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
}
