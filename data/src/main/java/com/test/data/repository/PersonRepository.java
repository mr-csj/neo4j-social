package com.test.data.repository;

import com.test.data.domain.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository extends GraphRepository<Person> {
    @Query("MATCH (n:Person) WHERE ID(n) <> {id} RETURN n;")
    Iterable<Person> findByIdNotIn(@Param("id") Long id);

    //找出所有朋友包括路径
    @Query("MATCH shortestPath((n:Person)-[r:FRIEND_OF*]->(m:Person)) WHERE ID(n)={id} RETURN m, length(r) as path ORDER BY m.name")
    Set<Person> findFriendsById(@Param("id") Long id);

    //看过指定电影的所有朋友
    @Query("MATCH (n:Person)-[r:FRIEND_OF*]-(m:Person) WHERE id(n)={id} WITH  m MATCH (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " +
            "RETURN  distinct m ORDER BY m.name")
    Set<Person> findFriendsVisiterMovie(@Param("id") Long id, @Param("name") String name);

    //未看过指定电影的朋友
    @Query("MATCH (n:Person)-[:FRIEND_OF*1..3]->(m:Person) WHERE id(n)={id} AND NOT (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " +
            "RETURN  m ORDER BY m.name")
    Set<Person> findFriendsNotVisiterMovie(@Param("id") Long id, @Param("name") String name);

    @Query("MATCH (n:Person)-[:FRIEND_OF*1..3]->(m:Person) WHERE id(n)={id} AND NOT (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " +
            "RETURN  m ORDER BY m.name skip {skip} limit {limit}")
    Set<Person> findFriendsNotVisiterMoviePage(@Param("id") Long id, @Param("name") String name, @Param("skip")int skip, @Param("limit")int limit);

    //看过指定电影的用户
    @Query("MATCH (o:Person) WHERE (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o")
    Set<Person> findUsersByVisiterMovieName(@Param("name") String name);

    //没有看过指定电影的用户
    @Query("MATCH (o:Person) WHERE NOT (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o")
    Set<Person> findUsersByNotVisiterMovieName(@Param("name") String name);

    @Query("MATCH (o:Person) WHERE NOT (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o SKIP {skip} LIMIT {limit}")
    Set<Person> findUsersByNotVisiterMovieNamePage(@Param("name")String name, @Param("skip")int skip, @Param("limit")int limit);

    Person findByName(String name);

}


