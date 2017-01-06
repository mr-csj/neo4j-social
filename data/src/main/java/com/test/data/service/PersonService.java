package com.test.data.service;

import com.test.data.domain.Person;
import com.test.data.model.PersonQo;
import com.test.data.repository.PersonRepository;
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
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PagesService<Person> personPagesService;

    public Person findById(Long id) {
        return personRepository.findOne(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.delete(id);
    }


    public Page<Person> findPage(PersonQo personQo){
        Pageable pageable = new PageRequest(personQo.getPage(), personQo.getSize(), new Sort(Sort.Direction.ASC, "id"));

        Filters filters = new Filters();
        if (!StringUtils.isEmpty(personQo.getName())) {
            Filter filter = new Filter("name", "*"+personQo.getName()+"*");
            filter.setComparisonOperator(ComparisonOperator.LIKE);
            filters.add(filter);
        }
        if (!StringUtils.isEmpty(personQo.getCreate())) {
            Filter filter = new Filter("create", personQo.getCreate().getTime());
            filter.setComparisonOperator(ComparisonOperator.GREATER_THAN);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }

        return personPagesService.findAll(Person.class, pageable, filters);
    }
}
