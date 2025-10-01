package model.dao;

import model.Database;
import model.pojo.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO {

    public Room findById(int id) {
        String sql = "select * from room where id = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Room(rs.getInt("id"),rs.getString("name"));
            }
        }   catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
