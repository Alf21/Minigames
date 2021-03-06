package alf21.minigames.manager;

import alf21.minigames.Minigames;
import alf21.minigames.engine.GameEngine;
import alf21.minigames.lifecycles.Lifecycle;
import net.gtaun.shoebill.common.player.PlayerLifecycleHolder;
import net.gtaun.shoebill.constant.WeaponModel;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.event.player.PlayerConnectEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerSpawnEvent;
import net.gtaun.shoebill.event.player.PlayerUpdateEvent;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;
import alf21.minigames.textdraws.Welcome;

import java.util.Random;

/**
 * Created by Alf21 on 07.07.2017 in the project 'minigames'.
 */
public class PlayerManager {

	private EventManagerNode eventManagerNode;

	public PlayerManager(EventManager rootEventManager)
	{
		eventManagerNode = rootEventManager.createChildNode();
		PlayerLifecycleHolder playerLifecycleHolder = Minigames.getInstance().getPlayerLifecycleHolder();

		eventManagerNode.registerHandler(PlayerUpdateEvent.class, (e) ->
		{
			Player player = e.getPlayer();

			// getUpdateCount() Example
			if (player.getUpdateCount() % 100 == 0)
			{
				player.setScore(player.getMoney());
			}
		});

		eventManagerNode.registerHandler(PlayerConnectEvent.class, (e) ->
		{
			Player player = e.getPlayer();
			player.sendGameText(5000, 5, "~w~SA-MP: ~r~Minigames");
			Player.sendMessageToAll(Color.GREEN,player.getName()+" connected!");
			Lifecycle playerLifecycle = playerLifecycleHolder.getObject(player, Lifecycle.class);

			Random random = new Random();

			playerLifecycle.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

			new Welcome(player);
		});

		eventManagerNode.registerHandler(PlayerDisconnectEvent.class, (e) ->
		{
			Player player = e.getPlayer();
			Player.sendMessageToAll(Color.RED,player.getName()+" Disconnected!");
			Player.sendDeathMessageToAll(player, null, WeaponModel.DISCONNECT);
		});
	}

	public void uninitialize()
	{
		eventManagerNode.destroy();
	}
}
