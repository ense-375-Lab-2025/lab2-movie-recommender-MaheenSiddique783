package com.example;

import org.junit.jupiter.api.*;
import java.util.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for MovieDatabase.
 * Portions of this code were generated with assistance from OpenAI's ChatGPT (https://chat.openai.com/), June 2025.
 */
public class MovieDatabaseTest {
    private MovieDatabase db;

    @BeforeEach
    void setUp() {
        // Delete database file before each test for a clean start
        File dbFile = new File("movies.db");
        if (dbFile.exists()) dbFile.delete();
        db = new MovieDatabase();
    }

    @Test
    void testAddAndRecommendMovie() {
        Movie m = new Movie(1, "Inception", "Sci-Fi", 2010);
        db.addMovie(m);
        List<Movie> sciFi = db.recommendByGenre("Sci-Fi");
        assertEquals(1, sciFi.size());
        assertEquals("Inception", sciFi.get(0).getTitle());
    }

    @Test
    void testDeleteMovie() {
        db.addMovie(new Movie(2, "Titanic", "Romance", 1997));
        db.deleteMovie(2);
        List<Movie> romance = db.recommendByGenre("Romance");
        assertTrue(romance.isEmpty());
    }

    @Test
    void testImportFromCSV() {
        db.importFromCSV("movie.csv"); // or "movies.csv" if you renamed it
        List<Movie> sciFi = db.recommendByGenre("Sci-Fi");
        assertTrue(sciFi.size() > 0); // You can check for a specific expected value based on your CSV data
    }
}
