package com.drestoriam.drestoriamsupport;
import com.mordonia.mcore.MCore;

import com.mordonia.mcore.MCoreAPI;
import com.drestoriam.drestoriamsupport.commands.Commands;
import com.drestoriam.drestoriamsupport.data.HelpData;
import com.drestoriam.drestoriamsupport.listener.ConnectionListener;
import com.drestoriam.drestoriamsupport.listener.HelperDIalogue;
import com.drestoriam.drestoriamsupport.util.TicketDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DrestoriamSupport extends JavaPlugin {

    @Override
    public void onEnable() {

        MCoreAPI mCoreAPI = MCore.getPlugin(MCore.class).getmCoreAPI();

        HelpData helpData = new HelpData();
        TicketDataManager ticketDataManager = new TicketDataManager();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("support").setExecutor(new Commands(ticketDataManager, mCoreAPI, helpData));

        getServer().getPluginManager().registerEvents(new HelperDIalogue(ticketDataManager, helpData, mCoreAPI), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(ticketDataManager, helpData), this);

    }

    @Override
    public void onDisable() {

    }

}