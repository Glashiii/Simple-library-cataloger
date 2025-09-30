package model;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializator {
    public static void init() {
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS room(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cabinet(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    room_id INTEGER NOT NULL,
                    FOREIGN KEY (room_id) REFERENCES room(id)
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shelf(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    cabinet_id INTEGER NOT NULL,
                    FOREIGN KEY (cabinet_id) REFERENCES cupboard(id)
                )""");

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS book(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        author TEXT,
                        shelf_id INTEGER NOT NULL,
                        FOREIGN KEY (shelf_id) REFERENCES shelf(id),
                        UNIQUE (title, shelf_id)
                    )""");

            System.out.println("Database initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

