package com.mordonia.mordoniasupport.data;

import com.mordonia.mcore.MySQLConnection;
import com.mordonia.mcore.ms.util.TicketDataManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteTicket {
    private TicketDataManager ticketDataManager;
    private MySQLConnection connection;

    public DeleteTicket(TicketDataManager ticketDataManager, MySQLConnection connection) {
        this.ticketDataManager = ticketDataManager;
        this.connection = connection;
    }


    public void deleteTickets(Integer id){
        try(PreparedStatement pr = connection.getConnection().prepareStatement("DELETE FROM `ticket_data` WHERE id=?")){
            pr.setInt(1, id);
            pr.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
}
