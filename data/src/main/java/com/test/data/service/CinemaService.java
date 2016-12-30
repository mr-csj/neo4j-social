package com.test.data.service;

import com.test.data.domain.Cinema;
import com.test.data.model.CinemaQo;
import com.test.data.repository.CinemaRepository;
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
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private PagesService<Cinema> cinemaPagesService;

    public Cinema findById(Long id) {
        return cinemaRepository.findOne(id);
    }

    public Cinema save(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    public void delete(Long id) {
        cinemaRepository.delete(id);
    }


    public Page<Cinema> findPage(CinemaQo cimemaQo){
        Pageable pageable = new PageRequest(cimemaQo.getPage(), cimemaQo.getSize(), new Sort(Sort.Direction.ASC, "id"));

        Filters filters = new Filters();
        if (!StringUtils.isEmpty(cimemaQo.getName())) {
            Filter filter = new Filter("name", "*"+cimemaQo.getName()+"*");
            filter.setComparisonOperator(ComparisonOperator.LIKE);
            filters.add(filter);
        }

        return cinemaPagesService.findAll(Cinema.class, pageable, filters);
    }
}
