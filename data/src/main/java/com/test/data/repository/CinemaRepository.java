package com.test.data.repository;

import com.test.data.domain.Cinema;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends GraphRepository<Cinema> {
}


