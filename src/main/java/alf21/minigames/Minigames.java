package alf21.minigames;

import alf21.minigames.lifecycles.Lifecycle;
import alf21.minigames.manager.PlayerManager;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.resource.Gamemode;
import net.gtaun.util.event.EventManager;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class Minigames extends Gamemode {

	private PlayerManager playerManager;
	private PlayerLifecycleHolder playerLifecycleHolder;
	private static Minigames instance;

	@Override
	protected void onEnable() throws Throwable {
		instance = new Minigames();

		System.out.println("Minigames are loading...");

		EventManager eventManager = getEventManager();

		instance.playerLifecycleHolder = new PlayerLifecycleHolder(getEventManager().createChildNode());
		instance.playerLifecycleHolder.registerClass(Lifecycle.class, Lifecycle::new);

		instance.playerManager = new PlayerManager(eventManager);

		System.out.println("Minigames have been loaded.");
	}

	@Override
	protected void onDisable() throws Throwable {
		instance.playerManager.uninitialize();
		instance.playerManager = null;
	}

	public static Minigames getInstance() {
		return instance;
	}

	public PlayerLifecycleHolder getPlayerLifecycleHolder() {
		return playerLifecycleHolder;
	}
}
