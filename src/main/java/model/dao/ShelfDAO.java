package model.dao;

import model.Database;
import model.pojo.Book;
import model.pojo.Shelf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void deleteShelfById(int id) {
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

    public Shelf findShelfById(int id) {
        String sql = "select * from shelf where id = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Shelf shelf = new Shelf(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("cabinet_id"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Shelf findShelfByName(String name) {
        String sql = "select * from shelf where name = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Shelf(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("cabinet_id"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Shelf> findAllShelvesByCabinetId(int cabinetId) {
        String sql = "select * from shelf where cabinet_id = ?";
        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cabinetId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<Shelf> shelves = new ArrayList<>();
                while (rs.next()) {
                    shelves.add(new Shelf(rs.getInt("id"), rs.getString("name"),
                            rs.getInt("cabinet_id")));
                }
                return shelves;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String updateShelfByName(String name, String newName) {
        String sql = "update shelf set name = ? where name = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, name);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Failed to update shelf");
            }
            return newName;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteShelfByName(String name) {
        String sql = "delete from shelf where name = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, name);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Failed to delete cabinet");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
