package com.test.data.service;

import com.test.data.domain.Movie;
import com.test.data.domain.Show;
import com.test.data.model.MovieQo;
import com.test.data.model.ShowQo;
import com.test.data.repository.CinemaRepository;
import com.test.data.repository.MovieRepository;
import com.test.data.repository.ShowRepository;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PagesService<Movie> moviePagesService;

    public Movie findById(Long id) {
        return movieRepository.findOne(id);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public void delete(Long id) {
        movieRepository.delete(id);
    }


    public Page<Movie> findPage(MovieQo movieQo){
        Pageable pageable = new PageRequest(movieQo.getPage(), movieQo.getSize(), new Sort(Sort.Direction.ASC, "id"));

        Filters filters = new Filters();
        if (!StringUtils.isEmpty(movieQo.getName())) {
            Filter filter = new Filter("name", "*"+movieQo.getName()+"*");
            filter.setComparisonOperator(ComparisonOperator.LIKE);
            filters.add(filter);
        }
        if (!StringUtils.isEmpty(movieQo.getCreate())) {
            Filter filter = new Filter("create", movieQo.getCreate());
            filter.setComparisonOperator(ComparisonOperator.GREATER_THAN);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }

        return moviePagesService.findAll(Movie.class, pageable, filters);
    }
}
