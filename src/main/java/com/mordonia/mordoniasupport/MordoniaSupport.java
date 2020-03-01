package com.mordonia.mordoniasupport;
import com.mordonia.mcore.MCore;
import com.mordonia.mcore.MySQLConnection;
import com.mordonia.mcore.mchat.util.channelData.ChatChannelDataManager;
import com.mordonia.mcore.mchat.util.kingdomData.KingdomDataManager;
import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import com.mordonia.mcore.ms.util.TicketDataManager;
import com.mordonia.mordoniasupport.commands.Commands;
import com.mordonia.mordoniasupport.data.DeleteTicket;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.data.TicketSave;
import com.mordonia.mordoniasupport.listener.ConnectionListener;
import com.mordonia.mordoniasupport.listener.HelperDIalogue;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MordoniaSupport extends JavaPlugin {
    public MCore mcoreAPI = (MCore) Bukkit.getServer().getPluginManager().getPlugin("MCore");

    @Override
    public void onEnable() {
        Plugin mchat = this.getServer().getPluginManager().getPlugin("MChat");
        if(mcoreAPI == null){
            return;
        }
        registerDataEvents();


    }

    @Override
    public void onDisable() {

    }
    public void registerDataEvents(){
        MySQLConnection connection = mcoreAPI.mySQLConnection;
        PlayerChatDataManager playerChatDataManager = mcoreAPI.playerChatDataManager;
        KingdomDataManager kingdomDataManager = mcoreAPI.kingdomDataManager;
        ChatChannelDataManager chatChannelDataManager = mcoreAPI.chatChannelDataManager;
        TicketDataManager ticketDataManager = mcoreAPI.ticketDataManager;
        HelpData helpData = new HelpData();
        DeleteTicket deleteTicket = new DeleteTicket(ticketDataManager, connection);
        TicketSave ticketSave = new TicketSave(connection);
        ticketDataManager.loadTickets();
        this.getCommand("support").setExecutor(new Commands(ticketDataManager, playerChatDataManager, connection, helpData, deleteTicket, ticketSave));
        this.getServer().getPluginManager().registerEvents(new HelperDIalogue(ticketDataManager, helpData, playerChatDataManager), this);
        this.getServer().getPluginManager().registerEvents(new ConnectionListener(ticketDataManager, helpData, connection, playerChatDataManager), this);

    }

}
