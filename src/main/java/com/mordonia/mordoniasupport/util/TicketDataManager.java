package com.mordonia.mordoniasupport.util;

import com.mordonia.mordoniasupport.MordoniaSupport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class TicketDataManager {

    public HashMap<Integer, TicketData> dataMap = new HashMap();
    private Plugin plugin = MordoniaSupport.getPlugin(MordoniaSupport.class);
    public FileConfiguration config = plugin.getConfig();


    public void loadTickets() {

        for(String key: config.getConfigurationSection("tickets").getKeys(false)){

            TicketData ticket = new TicketData(
                    Integer.parseInt(key),
                    (UUID) config.get("tickets." + key + ".player"),
                    config.get("tickets." + key + ".issue").toString(),
                    config.get("tickets." + key + ".status").toString(),
                    (UUID) config.get("tickets." + key + ".staff"),
                    config.get("tickets." + key + ".name").toString()
                    );

            ticket.setPlayer((UUID) config.get("tickets." + key + ".player"));

            dataMap.put(Integer.parseInt(key), ticket);

        }

    }

    public void saveTicket(TicketData ticket){

        Integer id = ticket.getId();

        config.set("tickets." + id, null);

        config.set("tickets." + id + ".player", ticket.getPlayer().toString());
        config.set("tickets." + id + ".name", ticket.getName());
        config.set("tickets." + id + ".status", ticket.getStatus());
        config.set("tickets." + id + ".issue", ticket.getIssue());
        config.set("tickets." + id + ".staff", ticket.getStaff().toString());

        dataMap.put(ticket.getId(), ticket);

        plugin.saveConfig();

    }

    public void deleteTicket(Integer id){

        config.set("tickets." + id, null);

        dataMap.remove(id);

        plugin.saveConfig();

    }

}
