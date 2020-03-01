package com.mordonia.mordoniasupport.listener;

import com.mordonia.mcore.MySQLConnection;
import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import com.mordonia.mcore.ms.util.TicketDataManager;
import com.mordonia.mordoniasupport.MordoniaSupport;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionListener implements Listener {
    private TicketDataManager ticketDataManager;
    private HelpData helpData;
    private MySQLConnection connection;
    private PlayerChatDataManager playerChatDataManager;
    private JavaPlugin plugin = MordoniaSupport.getProvidingPlugin(MordoniaSupport.class);

    public ConnectionListener(TicketDataManager ticketDataManager, HelpData helpData, MySQLConnection connection, PlayerChatDataManager playerChatDataManager){
        this.ticketDataManager = ticketDataManager;
        this.helpData = helpData;
        this.connection = connection;
        this.playerChatDataManager = playerChatDataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        if(player.hasPermission("support.open")){
            for(int id : ticketDataManager.dataMap.keySet()){
                if(ticketDataManager.dataMap.get(id).getStatus().equalsIgnoreCase("[Pending]")){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(Lang.TITLE + ChatColor.GOLD + " There are unanswered tickets! Please do /support check to see them.");
                        }
                    }.runTaskTimer(plugin, 0, 24000);
                }
                break;
            }

        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player u = event.getPlayer();
        if(helpData.playerMap.isEmpty() || helpData.dialogueMap.isEmpty()) return;
        if(helpData.playerMap.containsKey(event.getPlayer())){
            int id = helpData.playerMap.get(event.getPlayer());
            helpData.dialogueMap.remove(id);
            helpData.playerMap.remove(event.getPlayer());
            ticketDataManager.dataMap.get(id).setStatus("[Pending]");
            Player user = Bukkit.getPlayer(ticketDataManager.dataMap.get(id).getPlayer());
            Player staff = Bukkit.getPlayer(ticketDataManager.dataMap.get(id).getStaff());
            playerChatDataManager.get(user).setTicket(false);
            playerChatDataManager.get(staff).setTicket(false);
        }
    }
}
