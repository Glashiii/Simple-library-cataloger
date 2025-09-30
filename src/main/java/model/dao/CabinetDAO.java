package model.dao;

import model.Database;
import model.pojo.Cabinet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Cabinet findById(int id) {
        String sql = "SELECT * FROM cabinet WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Cabinet(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("room_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCabinetById(int id) {
        String sql = "delete from cabinet where id = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql))  {
            preparedStatement.setInt(1, id);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Failed to delete cabinet");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
