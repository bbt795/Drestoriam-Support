package com.mordonia.mordoniasupport.listener;

import com.mordonia.mordoniasupport.MordoniaSupport;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.util.Lang;
import com.mordonia.mordoniasupport.util.TicketData;
import com.mordonia.mordoniasupport.util.TicketDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListener implements Listener {
    private TicketDataManager ticketDataManager;
    private HelpData helpData;
    private JavaPlugin plugin = MordoniaSupport.getProvidingPlugin(MordoniaSupport.class);

    public ConnectionListener(TicketDataManager ticketDataManager, HelpData helpData){
        this.ticketDataManager = ticketDataManager;
        this.helpData = helpData;
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

        if(helpData.playerMap.isEmpty() || helpData.dialogueMap.isEmpty()) return;

        if(helpData.playerMap.containsKey(event.getPlayer())){

            int id = helpData.playerMap.get(event.getPlayer());
            helpData.dialogueMap.remove(id);
            helpData.playerMap.remove(event.getPlayer());

            TicketData ticket = ticketDataManager.dataMap.get(id);
            ticket.setStatus("[Pending]");
            ticketDataManager.saveTicket(ticket);

            /*
            Player user = Bukkit.getPlayer(ticketDataManager.dataMap.get(id).getPlayer());
            Player staff = Bukkit.getPlayer(ticketDataManager.dataMap.get(id).getStaff());
            playerManager.getPlayerMap().get(user).setTicket(false);
            playerManager.getPlayerMap().get(staff).setTicket(false);
            */
        }
    }
}
