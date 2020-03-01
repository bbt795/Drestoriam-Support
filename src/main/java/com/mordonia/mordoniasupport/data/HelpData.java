package com.mordonia.mordoniasupport.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpData {
    public HashMap<Integer, ArrayList<Player>> dialogueMap = new HashMap<>();
    public HashMap<Player, Integer> playerMap = new HashMap<>();



    public void enableDialogue(Integer dialogueID, Player player){
        ArrayList<Player> players = dialogueMap.get(dialogueID);

        if(players == null){
            players = new ArrayList<Player>();
        }
        players.add(player);
        dialogueMap.put(dialogueID, players);
        playerMap.put(player, dialogueID);

    }

    public ArrayList<Player> getID(Player player){
        Integer id = playerMap.get(player);
        return dialogueMap.get(id);

    }


}
