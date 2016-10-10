package com.test.data.service;

import com.test.data.domain.Show;
import com.test.data.model.ShowQo;
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
public class ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private PagesService<Show> showPagesService;

    public Show findById(Long id) {
        return showRepository.findOne(id);
    }

    public Show save(Show show) {
        return showRepository.save(show);
    }

    public void delete(Long id) {
        showRepository.delete(id);
    }


    public Page<Show> findPage(ShowQo showQo){
        Pageable pageable = new PageRequest(showQo.getPage(), showQo.getSize(), new Sort(Sort.Direction.ASC, "id"));

        Filters filters = new Filters();
        if (!StringUtils.isEmpty(showQo.getName())) {
            Filter filter = new Filter("name", "*"+showQo.getName()+"*");
            filter.setComparisonOperator(ComparisonOperator.LIKE);
            filters.add(filter);
        }
        if (!StringUtils.isEmpty(showQo.getCreate())) {
            Filter filter = new Filter("create", showQo.getCreate());
            filter.setComparisonOperator(ComparisonOperator.GREATER_THAN);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }

        return showPagesService.findAll(Show.class, pageable, filters);
    }
}
