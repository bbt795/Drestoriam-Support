package com.mordonia.mordoniasupport;
import com.mordonia.mcore.MCore;

import com.mordonia.mcore.MCoreAPI;
import com.mordonia.mordoniasupport.commands.Commands;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.listener.ConnectionListener;
import com.mordonia.mordoniasupport.listener.HelperDIalogue;
import com.mordonia.mordoniasupport.util.TicketDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MordoniaSupport extends JavaPlugin {

    @Override
    public void onEnable() {

        MCoreAPI mCoreAPI = MCore.getPlugin(MCore.class).getmCoreAPI();

        HelpData helpData = new HelpData();
        TicketDataManager ticketDataManager = new TicketDataManager();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("support").setExecutor(new Commands(ticketDataManager, mCoreAPI.getmPlayerManager(), helpData));

        getServer().getPluginManager().registerEvents(new HelperDIalogue(ticketDataManager, helpData, mCoreAPI.getmPlayerManager()), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(ticketDataManager, helpData), this);

    }

    @Override
    public void onDisable() {

    }

}