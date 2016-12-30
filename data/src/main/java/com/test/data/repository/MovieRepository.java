package com.test.data.repository;

import com.test.data.domain.Movie;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
public interface MovieRepository extends GraphRepository<Movie> {
    //电影评分排名
    @Query("MATCH (:Person)-[r:RATED]->(m:Movie) " +
            "WITH m, COLLECT(r.stars) AS ratings " +
            "WITH m, ratings, REDUCE(s = 0, i IN ratings | s + i)*1.0 / SIZE(ratings) AS stars " +
            "RETURN ID(m) AS id, m.name AS name, stars, SIZE(ratings) AS num " +
            "ORDER BY stars DESC, num DESC SKIP {skip} LIMIT {limit}")
    Set<Map<String,Object>> findRatingMovie(@Param("skip") int skip, @Param("limit") int limit);

    //电影评分总数
    @Query("MATCH (:Person)-[r:RATED]->(m:Movie) " +
            "WITH m, COLLECT(r.stars) AS ratings " +
            "RETURN COUNT(m) AS count ")
    int findRatingMovieCount();
}
