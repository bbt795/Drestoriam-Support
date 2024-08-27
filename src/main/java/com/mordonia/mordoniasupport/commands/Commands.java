package com.mordonia.mordoniasupport.commands;

import com.mordonia.mcore.MCore;
import com.mordonia.mcore.MCoreAPI;
import com.mordonia.mcore.data.palyerdata.MPlayerManager;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.util.Lang;
import com.mordonia.mordoniasupport.util.TicketData;
import com.mordonia.mordoniasupport.util.TicketDataManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.UUID;

import static net.md_5.bungee.api.ChatColor.*;

public class Commands implements CommandExecutor {

    private HelpData helpData;
    private TicketDataManager ticketDataManager;
    private MCoreAPI mCoreAPI;

    public Commands(TicketDataManager ticketDataManager, MCoreAPI mCoreAPI, HelpData helpData) {

        this.mCoreAPI = mCoreAPI;
        this.ticketDataManager = ticketDataManager;
        this.helpData = helpData;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("support")) {
            Player user = (Player) sender;
            if (args.length < 1) {
                if (!sender.hasPermission("ms.open")) {
                    sender.sendMessage(Lang.TITLE + " /support [issue]");
                    return false;
                }
                sender.sendMessage(Lang.TITLE + " /support [issue] \n /support check \n /support open [id] \n /support close [id]");
                return false;
            }
            String subcommand = args[0];
            switch (subcommand) {
                default:
                    String status = "[Pending]";
                    String issue = " ";
                    int id = 0;

                    while (ticketDataManager.dataMap.containsKey(id)) {
                        id++;
                    }
                    for (int i = 0; i < args.length; i++) {
                        String arg = args[i] + " ";
                        issue = issue + arg;
                    }

                    UUID uuid = user.getUniqueId();

                    String firstName = mCoreAPI.getmPlayerManager().getPlayerMap().get(uuid.toString()).getmName().getFirstname();
                    String lastName = mCoreAPI.getmPlayerManager().getPlayerMap().get(uuid.toString()).getmName().getLastname();
                    String name =  firstName + " " + lastName;

                    TicketData ticket = new TicketData(id, uuid, issue, status, null, name);

                    ticketDataManager.saveTicket(ticket);

                    sender.sendMessage(Lang.TITLE + ChatColor.BLUE + " You have created a new support ticket! A staff member will answer it shortly!");
                    for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                        if (staff.hasPermission("ms.open")) {
                            staff.sendMessage(Lang.TITLE + ChatColor.GOLD + ((Player) sender).getDisplayName() + ChatColor.BLUE + " has created a new support ticket! Use /support check to see it.");
                        }
                    }
                    break;

                case ("open"):
                    if (!sender.hasPermission("ms.open")) {
                        sender.sendMessage(Lang.TITLE + " " + ChatColor.DARK_RED + "You don't have enough permission to run this command!");
                        break;
                    }
                    if (args.length < 2) {
                        sender.sendMessage(Lang.TITLE + " " + ChatColor.RED + "Invalid usage!");
                        break;
                    }
                    String openIDString = args[1];
                    int openIDInt = Integer.valueOf(openIDString);

                    if (!ticketDataManager.dataMap.containsKey(openIDInt)) {
                        sender.sendMessage(Lang.TITLE + " " + ChatColor.RED + "Invalid ticket!");
                        break;
                    }
                    if (ticketDataManager.dataMap.get(openIDInt).getStatus().equalsIgnoreCase("[Open]")) {
                        sender.sendMessage(Lang.TITLE + " " + ChatColor.RED + "That ticket is currently open!");
                        break;
                    }
                    Player target = Bukkit.getPlayer(ticketDataManager.dataMap.get(openIDInt).getPlayer());
                    if (target == null) {
                        sender.sendMessage(Lang.TITLE + ChatColor.RED + "The player was not found!");
                        break;
                    }

                    TicketData openedTicket = ticketDataManager.dataMap.get(openIDInt);

                    openedTicket.setStaff(user.getUniqueId());
                    openedTicket.setStatus("[Open]");

                    ticketDataManager.saveTicket(openedTicket);

                    helpData.enableDialogue(openIDInt, target);
                    helpData.enableDialogue(openIDInt, user);

                    mCoreAPI.getmPlayerManager().getPlayerMap().get(user.getUniqueId().toString()).setInTicket(true);
                    mCoreAPI.getmPlayerManager().getPlayerMap().get(target.getUniqueId().toString()).setInTicket(true);

                    target.sendMessage(Lang.TITLE + " " + ChatColor.GOLD + user.getDisplayName() + ChatColor.BLUE + " has opened your ticket, you are now in a helper dialogue!");
                    user.sendMessage(Lang.TITLE + " " + ChatColor.BLUE + "You successfully opened the ticket!");

                    break;

                case ("check"):

                    if (!sender.hasPermission("ms.open")) {
                        sender.sendMessage(ChatColor.RED + " You don't have enough permission to use this command!");
                        break;
                    }

                    ticketDataManager.loadTickets();

                    if(ticketDataManager.dataMap.isEmpty()){
                        sender.sendMessage(Lang.TITLE + ChatColor.BLUE + " There are no tickets pending!");
                    }

                    int z = 0;
                    while (z < ticketDataManager.dataMap.size()) {

                        for (int idCh : ticketDataManager.dataMap.keySet()) {
                            String statusCh = ticketDataManager.dataMap.get(idCh).getStatus();
                            String issueCh = ticketDataManager.dataMap.get(idCh).getIssue();
                            String pName = ChatColor.stripColor(ticketDataManager.dataMap.get(idCh).getName());
                            TextComponent plName = new TextComponent(pName);
                            TextComponent ticketStatus = new TextComponent(statusCh + " ");
                            TextComponent ticketIssue = new TextComponent(issueCh);
                            TextComponent ticketID = new TextComponent(idCh + " ");

                            ticketID.setColor(DARK_AQUA);

                            if (statusCh.equalsIgnoreCase("[Open]")) {
                                ticketStatus.setColor(DARK_GREEN);
                                ticketStatus.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "A staff member is currently in this ticket!").create()));
                            } else {
                                ticketStatus.setColor(DARK_RED);
                                ticketStatus.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "No one has answered this ticket yet!").create()));

                            }
                            Player o = Bukkit.getPlayer(ticketDataManager.dataMap.get(idCh).getPlayer());
                            if (o == null) {
                                plName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "[Offline]").create()));
                            } else {
                                plName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "[Online]\n" + o.getDisplayName()).create()));
                            }

                            sender.spigot().sendMessage(ticketID, ticketStatus, plName, ticketIssue);
                            z++;
                        }
                    }
                    break;
                case ("close"):
                    if (!sender.hasPermission("ms.open")) {
                        sender.sendMessage(Lang.TITLE + ChatColor.RED + " You don't have permission to run this command!");
                        break;
                    }
                    if (args.length < 2) {
                        sender.sendMessage(Lang.TITLE + ChatColor.RED + " Invalid usage!");
                    }

                    String closeIDStr = args[1];
                    int closeID = Integer.valueOf(closeIDStr);
                    if (!ticketDataManager.dataMap.containsKey(closeID)) {
                        sender.sendMessage(Lang.TITLE + ChatColor.RED + " Ticket not found!");
                        break;
                    }
                    Player p = Bukkit.getPlayer(ticketDataManager.dataMap.get(closeID).getPlayer());
                    Player s = Bukkit.getPlayer(ticketDataManager.dataMap.get(closeID).getStaff());
                    if(s != null){
                        s.sendMessage(Lang.TITLE + ChatColor.DARK_GREEN + " You closed the ticket successfully!");
                        mCoreAPI.getmPlayerManager().getPlayerMap().get(s.getUniqueId().toString()).setInTicket(false);
                    }
                    if(p != null){
                        p.sendMessage(Lang.TITLE + ChatColor.DARK_GREEN +  " Your ticket has been closed!");
                        mCoreAPI.getmPlayerManager().getPlayerMap().get(p.getUniqueId().toString()).setInTicket(false);
                    }
                    if(helpData.dialogueMap.containsKey(closeID)){

                        helpData.dialogueMap.get(closeID).remove(p);
                        helpData.dialogueMap.get(closeID).remove(s);
                        helpData.dialogueMap.remove(closeID);

                    }

                    ticketDataManager.deleteTicket(closeID);

                    return false;
            }
        }
        return false;
    }
}
