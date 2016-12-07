package com.test.data.repository;

import com.test.data.domain.Show;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends GraphRepository<Show> {
}