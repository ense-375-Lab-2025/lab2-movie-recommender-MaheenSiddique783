package com.example;

/**
 * Represents a movie with id, title, genre(s), and year.
 * Portions of this code were generated with assistance from OpenAI's ChatGPT (https://chat.openai.com/), June 2025.
 */
public class Movie {
    private int id;
    private String title;
    private String genre; // Keep as comma-separated string
    private int year;

    public Movie(int id, String title, String genre, int year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
}
