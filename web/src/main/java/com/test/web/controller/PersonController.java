package com.test.web.controller;

import com.test.data.domain.Movie;
import com.test.data.domain.Rating;
import com.test.data.domain.Show;
import com.test.data.domain.Person;
import com.test.data.model.PersonQo;
import com.test.data.repository.ShowRepository;
import com.test.data.repository.PersonRepository;
import com.test.data.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/person")
public class PersonController {
    private static Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ShowRepository showRepository;

    @RequestMapping("/index")
    public String index() throws Exception{
        return "person/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        Person person = personService.findById(id);
        model.addAttribute("person", person);
        return "person/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Person> getList(PersonQo personQo) {
        try {
            Page<Person> persons = personService.findPage(personQo);
            return  persons;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model,Person person){
        Iterable<Person> friends = personRepository.findAll();
        Iterable<Show> visiters = showRepository.findAll();
        model.addAttribute("friends",friends);
        model.addAttribute("visiters",visiters);
        model.addAttribute("person", person);
        return "person/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Person person, HttpServletRequest request) throws Exception{
        person.setCreate(new Date());
        personService.save(person);
        logger.info("新增->ID="+ person.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Person person = personService.findById(id);
        Iterable<Person> friends = personRepository.findByIdNot(id);
        Iterable<Show> visiters = showRepository.findAll();

        //用户朋友
        Set<Long> fids = new HashSet<>();
        for(Person friend : person.getFriends()){
            fids.add(friend.getId());
        }

        //用户看过节目
        Set<Long> vids = new HashSet<>();
        for(Show show : person.getVisiters()){
            vids.add(show.getId());
        }

        model.addAttribute("friends",friends);
        model.addAttribute("visiters",visiters);
        model.addAttribute("person", person);
        model.addAttribute("fids",fids);
        model.addAttribute("vids",vids);
        return "person/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Person person) throws Exception{
        personService.save(person);
        logger.info("修改->ID="+ person.getId());
        return "1";
    }

    @RequestMapping(value="/rating/{id}")
    public String rating(ModelMap model,@PathVariable Long id){
        //深度为2可返回看过的节目的电影对象：person.getVisiters().getMovie()
        Person person = personRepository.findOne(id, 2);

        //已评分的电影
        Set<Long> rating_sids = new HashSet<>();
        for(Rating rating : person.getRatings()){
            rating_sids.add(rating.getMovie().getId());
        }

        //看过的未评分的电影
        Set<Movie> movies = new HashSet<>();
        for(Show show : person.getVisiters()){
            if(!rating_sids.contains(show.getMovie().getId()))
               movies.add(show.getMovie());
        }

        model.addAttribute("movies",movies);
        model.addAttribute("person", person);

        return "person/rating";
    }

    @RequestMapping(method = RequestMethod.POST, value="/rating/save")
    @ResponseBody
    public String rating_save(Rating rating) throws Exception{
        Person person = rating.getPerson();
        person.rate(rating.getMovie(),rating.getStars(),rating.getComment());
        personService.save(person);
        logger.info("评分->ID="+ person.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        personService.delete(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
