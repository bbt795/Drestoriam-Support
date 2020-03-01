package com.mordonia.mordoniasupport.data;

import com.mordonia.mcore.MySQLConnection;
import com.mordonia.mcore.ms.util.TicketDataManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TicketSave {
    private MySQLConnection connection;


    public TicketSave(MySQLConnection connection){
        this.connection = connection;
    }


    public void saveFiles(Integer id, UUID player, String name, String issue, String status){
        try(PreparedStatement pr1 = connection.getConnection().prepareStatement("INSERT INTO ticket_data(id, player, name, issues, status) VALUES (?,?,?,?,?)")){
            pr1.setInt(1, id);
            pr1.setString(2, player.toString());
            pr1.setString(3, name);
            pr1.setString(4, issue);
            pr1.setString(5, status);
            pr1.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
