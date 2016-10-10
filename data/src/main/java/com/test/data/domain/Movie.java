package com.test.data.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Movie{
    @GraphId
    private Long id;
    private String name;
    @Relationship(type = "RATED", direction = Relationship.INCOMING)
    private Set<Rating> ratings = new HashSet<>();

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }

    public Set<Rating> getRatings() {
        return ratings;
    }
}
