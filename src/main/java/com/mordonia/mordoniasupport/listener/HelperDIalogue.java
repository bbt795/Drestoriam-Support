//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mordonia.mordoniasupport.listener;

import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import com.mordonia.mcore.ms.util.TicketData;
import com.mordonia.mcore.ms.util.TicketDataManager;
import com.mordonia.mordoniasupport.data.HelpData;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class HelperDIalogue implements Listener {
    private TicketDataManager ticketDataManager;
    private HelpData helpData;
    private PlayerChatDataManager playerChatDataManager;

    public HelperDIalogue(TicketDataManager ticketDataManager, HelpData helpData, PlayerChatDataManager playerChatDataManager) {
        this.ticketDataManager = ticketDataManager;
        this.helpData = helpData;
        this.playerChatDataManager = playerChatDataManager;
    }

    @EventHandler
    public void onHelperDialogue(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String firstname = this.playerChatDataManager.get(p).getFirstname();
        String lastname = this.playerChatDataManager.get(p).getLastname();
        String name = firstname + " " + lastname + ": ";
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
