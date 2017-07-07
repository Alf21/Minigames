import engine.GameEngine;
import lifecycles.Lifecycle;
import manager.PlayerManager;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.resource.Gamemode;
import net.gtaun.util.event.EventManager;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class Minigames extends Gamemode {

	private PlayerManager playerManager;
	private PlayerLifecycleHolder playerLifecycleHolder;
	private GameEngine gameEngine;

	@Override
	protected void onEnable() throws Throwable {
		System.out.println("Minigames are loading...");
		gameEngine = new GameEngine();

		EventManager eventManager = getEventManager();
		System.out.println("eventManager: \t\t" + eventManager);

		playerLifecycleHolder = new PlayerLifecycleHolder(getEventManager());
		System.out.println("playerLifecycleHolder: \t" + playerLifecycleHolder);

		playerManager = new PlayerManager(eventManager, gameEngine);
		playerLifecycleHolder.registerClass(Lifecycle.class, Lifecycle::new);

		System.out.println("Minigames have been loaded.");
	}

	@Override
	protected void onDisable() throws Throwable {
		playerManager.uninitialize();
		playerManager = null;
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}
}
