package com.mordonia.mordoniasupport.listener;

import com.mordonia.mcore.data.palyerdata.MPlayerManager;
import com.mordonia.mordoniasupport.data.HelpData;
import java.util.ArrayList;

import com.mordonia.mordoniasupport.util.TicketDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class HelperDIalogue implements Listener {
    private TicketDataManager ticketDataManager;
    private HelpData helpData;
    private MPlayerManager playerManager;

    public HelperDIalogue(TicketDataManager ticketDataManager, HelpData helpData, MPlayerManager playerManager) {
        this.ticketDataManager = ticketDataManager;
        this.helpData = helpData;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onHelperDialogue(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String firstname = this.playerManager.getPlayerMap().get(p).getmName().getFirstname();
        String lastname = this.playerManager.getPlayerMap().get(p).getmName().getLastname();
        String name = firstname + " " + lastname;
        event.setCancelled(true);

        for (int id : ticketDataManager.dataMap.keySet()) {
            if (ticketDataManager.dataMap.get(id).getStatus().equals("[Open]")) {
                event.getRecipients().clear();
                ArrayList<Player> players = this.helpData.getID(p);
                if (players != null) {
                    for (Player player : players) {
                        player.sendMessage(ChatColor.DARK_AQUA + name + ": " + event.getMessage());
                    }
                    break;
                 }
                }
            }
        }
    }
