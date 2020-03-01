package com.mordonia.mordoniasupport.commands;

import com.mordonia.mcore.MySQLConnection;
import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import com.mordonia.mcore.ms.util.TicketData;
import com.mordonia.mcore.ms.util.TicketDataManager;
import com.mordonia.mordoniasupport.data.DeleteTicket;
import com.mordonia.mordoniasupport.data.HelpData;
import com.mordonia.mordoniasupport.data.TicketSave;
import com.mordonia.mordoniasupport.util.Lang;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.jvm.hotspot.utilities.LivenessAnalysis;


import java.util.UUID;

import static net.md_5.bungee.api.ChatColor.*;

public class Commands implements CommandExecutor {
    private TicketDataManager ticketDataManager;
    private PlayerChatDataManager playerChatDataManager;
    private MySQLConnection connection;
    private OfflinePlayer p;
    private String name;
    private HelpData helpData;
    private DeleteTicket deleteTicket;
    private TicketSave ticketSave;

    public Commands(TicketDataManager ticketDataManager, PlayerChatDataManager playerChatDataManager, MySQLConnection connection, HelpData helpData, DeleteTicket deleteTicket, TicketSave ticketSave) {
        this.playerChatDataManager = playerChatDataManager;
        this.ticketDataManager = ticketDataManager;
        this.connection = connection;
        this.helpData = helpData;
        this.deleteTicket = deleteTicket;
        this.ticketSave = ticketSave;
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
                sender.sendMessage(Lang.TITLE + " /support [issue] \n /support check \n /suppoer open [id] \n /support close [id]");
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
                    String name = playerChatDataManager.get(user).getFirstname() + " " + playerChatDataManager.get(user).getLastname();
                    ticketDataManager.dataMap.put(id, new TicketData(id, uuid, issue, status, null, name));
                    ticketSave.saveFiles(id, uuid, name, issue, status);

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
                    ticketDataManager.dataMap.get(openIDInt).setStaff(user.getUniqueId());
                    ticketDataManager.dataMap.get(openIDInt).setStatus("[Open]");
                    helpData.enableDialogue(openIDInt, target);
                    helpData.enableDialogue(openIDInt, user);
                    playerChatDataManager.get(user).setTicket(true);
                    playerChatDataManager.get(target).setTicket(true);

                    target.sendMessage(Lang.TITLE + " " + ChatColor.GOLD + user.getDisplayName() + ChatColor.BLUE + " has opened your ticket, you are now in a helper dialogue!");
                    user.sendMessage(Lang.TITLE + " " + ChatColor.BLUE + "You successfully opened the ticket!");
                    break;
                case ("check"):
                    if (!sender.hasPermission("ms.open")) {
                        sender.sendMessage(ChatColor.DARK_RED + "You don't have enough permission to use this command!");
                        break;
                    }
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
                            TextComponent tickeyStatus = new TextComponent(statusCh + " ");
                            TextComponent ticketIssue = new TextComponent(issueCh);
                            TextComponent ticketID = new TextComponent(String.valueOf(idCh) + " ");

                            ticketID.setColor(DARK_AQUA);

                            if (statusCh.equalsIgnoreCase("[Open]")) {
                                tickeyStatus.setColor(DARK_GREEN);
                                tickeyStatus.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "A staff member is currently in this ticket!").create()));
                            } else {
                                tickeyStatus.setColor(DARK_RED);
                                tickeyStatus.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "No one has answered this ticket yet!").create()));

                            }
                            Player o = Bukkit.getPlayer(ticketDataManager.dataMap.get(idCh).getPlayer());
                            if (o == null) {
                                plName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "[Offline]").create()));
                            } else {
                                plName.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "[Online]\n" + o.getDisplayName()).create()));
                            }

                            sender.spigot().sendMessage(ticketID, tickeyStatus, plName, ticketIssue);
                            z++;
                        }
                    }
                    break;
                case ("close"):
                    if (!sender.hasPermission("ms.open")) {
                        sender.sendMessage(Lang.TITLE + ChatColor.DARK_RED + "You don't have permission to run this command!");
                        break;
                    }
                    if (args.length < 2) {
                        sender.sendMessage(Lang.TITLE + ChatColor.DARK_RED + "Invalid usage!");
                    }

                    String closeIDStr = args[1];
                    int closeID = Integer.valueOf(closeIDStr);
                    if (!ticketDataManager.dataMap.containsKey(closeID)) {
                        sender.sendMessage(Lang.TITLE + ChatColor.DARK_RED + " Ticket not found!");
                        break;
                    }
                    Player p = Bukkit.getPlayer(ticketDataManager.dataMap.get(closeID).getPlayer());
                    Player s = Bukkit.getPlayer(ticketDataManager.dataMap.get(closeID).getStaff());
                    if(s != null){
                        s.sendMessage(Lang.TITLE + ChatColor.DARK_GREEN + " You closed the ticket successfully!");
                        playerChatDataManager.get(s).setTicket(false);
                    }
                    if(p != null){
                        p.sendMessage(Lang.TITLE + ChatColor.DARK_GREEN +  " Your ticket has been closed!");
                        playerChatDataManager.get(p).setTicket(false);
                    }
                    if(helpData.dialogueMap.containsKey(closeID)){
                        helpData.dialogueMap.get(closeID).remove(p);
                        helpData.dialogueMap.get(closeID).remove(s);
                        helpData.dialogueMap.remove(closeID);

                    }
                    ticketDataManager.dataMap.remove(closeID);
                    deleteTicket.deleteTickets(closeID);
                    sender.sendMessage(Lang.TITLE + ChatColor.DARK_GREEN + " You closed the ticket successfully!");


                    return false;
            }
        }
        return false;
    }
}
