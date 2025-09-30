package model.dao;

import model.Database;
import model.pojo.Shelf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShelfDAO {

    public void addShelf(Shelf shelf) {
        String sql = "insert into shelf(name, cabinet_id) values(?,?)";
        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, shelf.getName());
            preparedStatement.setInt(2, shelf.getCabinetId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteShelfById(Long id) {
        String sql = "delete from shelf where id = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql))  {
            preparedStatement.setLong(1, id);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Failed to delete shelf");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
