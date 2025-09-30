package model.dao;

import model.Database;
import model.pojo.Cabinet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CabinetDAO {
    public void addCabinet(Cabinet cabinet) {
        String sql = "insert into cabinet(name, room_id) values(?,?)";
        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cabinet.getName());
            preparedStatement.setInt(2, cabinet.getRoomId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
