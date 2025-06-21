package com.example;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Manages storage and querying of movies using SQLite.
 * Portions of this code were generated with assistance from OpenAI's ChatGPT (https://chat.openai.com/), June 2025.
 */
public class MovieDatabase {
    private static final String DB_URL = "jdbc:sqlite:movies.db";

    public MovieDatabase() {
        // Create table if not exists
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies (" +
                               "id INTEGER PRIMARY KEY, " +
                               "title TEXT, " +
                               "genre TEXT, " +
                               "year INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMovie(Movie movie) {
        String sql = "INSERT INTO movies (id, title, genre, year) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movie.getId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getGenre());
            pstmt.setInt(4, movie.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(int id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovieByTitle(String title) {
        String sql = "DELETE FROM movies WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> recommendByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        String sql = "SELECT id, title, genre, year FROM movies WHERE genre LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + genre + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new Movie(rs.getInt("id"),
                                     rs.getString("title"),
                                     rs.getString("genre"),
                                     rs.getInt("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Import from a CSV file matching: Title,Genres,Year
    public void importFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            int id = 1;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // Skip header

                String[] parts = line.split(",");
                if (parts.length < 3) continue; // skip invalid lines

                String title = parts[0].trim();
                String yearStr = parts[parts.length - 1].trim();

                // Join all columns between title and year as genres
                StringBuilder genresBuilder = new StringBuilder();
                for (int i = 1; i < parts.length - 1; i++) {
                    if (i > 1) genresBuilder.append(", ");
                    genresBuilder.append(parts[i].trim());
                }
                String genres = genresBuilder.toString();

                int year = 0;
                try { year = Integer.parseInt(yearStr); } catch (NumberFormatException e) { /* skip or set default */ }

                addMovie(new Movie(id++, title, genres, year));
                // Debug print:
                // System.out.println("Inserted: " + title + " | " + genres + " | " + year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
