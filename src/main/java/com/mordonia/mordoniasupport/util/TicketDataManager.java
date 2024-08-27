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

        try {

            for (String key : config.getConfigurationSection("tickets").getKeys(false)) {

                TicketData ticket = new TicketData(
                        Integer.parseInt(key),
                        UUID.fromString(String.valueOf(config.get("tickets." + key + ".player"))),
                        config.get("tickets." + key + ".issue").toString(),
                        config.get("tickets." + key + ".status").toString(),
                        null,
                        config.get("tickets." + key + ".name").toString()
                );

                if(config.get("tickets." + key + ".staff") != null){

                    ticket.setStaff(UUID.fromString(String.valueOf(config.get("tickets." + key + ".staff"))));

                }

                if(!dataMap.containsKey(Integer.parseInt(key))){

                    dataMap.put(Integer.parseInt(key), ticket);

                }

            }

        } catch(NullPointerException e){

            return;

        }

    }

    public void saveTicket(TicketData ticket){

        try {

            Integer id = ticket.getId();

            config.set("tickets." + id, null);

            config.set("tickets." + id + ".player", ticket.getPlayer().toString());
            config.set("tickets." + id + ".name", ticket.getName());
            config.set("tickets." + id + ".status", ticket.getStatus());
            config.set("tickets." + id + ".issue", ticket.getIssue());

            if(ticket.getStaff() != null) {
                config.set("tickets." + id + ".staff", ticket.getStaff().toString());
            }

            dataMap.put(ticket.getId(), ticket);

            plugin.saveConfig();

        } catch (NullPointerException e){

            return;

        }

    }

    public void deleteTicket(Integer id){

        config.set("tickets." + id, null);

        dataMap.remove(id);

        plugin.saveConfig();

    }

}
