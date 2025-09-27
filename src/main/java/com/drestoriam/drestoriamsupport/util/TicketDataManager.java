package com.drestoriam.drestoriamsupport.util;

import com.drestoriam.drestoriamsupport.DrestoriamSupport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class TicketDataManager {

    public HashMap<Integer, TicketData> dataMap = new HashMap<>();
    private Plugin plugin = DrestoriamSupport.getPlugin(DrestoriamSupport.class);
    public FileConfiguration config = plugin.getConfig();


    public void loadTickets() {

        try {

            //Loop through all the current tickets saved in the config.yml
            for (String key : config.getConfigurationSection("tickets").getKeys(false)) {

                //create a ticket object and parse data from config.yml
                TicketData ticket = new TicketData(
                        Integer.parseInt(key),
                        UUID.fromString(String.valueOf(config.get("tickets." + key + ".player"))),
                        config.get("tickets." + key + ".issue").toString(),
                        config.get("tickets." + key + ".status").toString(),
                        null,
                        config.get("tickets." + key + ".name").toString()
                );

                //if the config contains a 'staff' section, add that information to the ticket object
                if(config.get("tickets." + key + ".staff") != null){

                    ticket.setStaff(UUID.fromString(String.valueOf(config.get("tickets." + key + ".staff"))));

                }

                //if the map doesn't contain the current ticket, add it
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

            //Parse the information from the provided ticket object
            Integer id = ticket.getId();

            config.set("tickets." + id, null);

            config.set("tickets." + id + ".player", ticket.getPlayer().toString());
            config.set("tickets." + id + ".name", ticket.getName());
            config.set("tickets." + id + ".status", ticket.getStatus());
            config.set("tickets." + id + ".issue", ticket.getIssue());

            //if the ticket does have staff information, add that too
            if(ticket.getStaff() != null) {
                config.set("tickets." + id + ".staff", ticket.getStaff().toString());
            }

            //Add the ticket to the map of tickets
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
