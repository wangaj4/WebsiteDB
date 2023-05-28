package edu.uci.ics.fabflixmobile.data.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String name;
    private final String year;

    private final String director;


    public Movie(String name, String year, String director) {
        this.name = name;
        this.year = year;
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public String getDirector() {
        return director;
    }

    public String getYear() {
        return year;
    }

}