package com.test.web.controller;

import com.test.data.domain.Person;
import com.test.data.model.MovieQo;
import com.test.data.repository.MovieRepository;
import com.test.data.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/worth")
public class RecommendController {
    private static Logger logger = LoggerFactory.getLogger(RecommendController.class);

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/movie")
    public String index() throws Exception{
        return "worth/movie";
    }

    @RequestMapping(value = "/movie_list")
    @ResponseBody
    public Page<Map<String, Object>> getList(MovieQo movieQo) {
        try {
            Pageable pageable = new PageRequest(movieQo.getPage(), movieQo.getSize(), null);
            Set<Map<String,Object>> movies = movieRepository.findRatingMovie(movieQo.getPage() * movieQo.getSize(), movieQo.getSize());
            int count = movieRepository.findRatingMovieCount();
            return new PageImpl(new ArrayList(movies), pageable, (long)count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/user")
    public String user(ModelMap model) throws Exception{
        Set<Map<String,Object>> movies = movieRepository.findRatingMovie(0, 100);
        model.addAttribute("movies", movies);
        return "worth/user";
    }

    @RequestMapping(value = "/user_list")
    @ResponseBody
    public Page<Person> getUserList(MovieQo movieQo) {
        try {
            Pageable pageable = new PageRequest(movieQo.getPage(), movieQo.getSize(), null);
            Set<Person> list = personRepository.findUsersByNotVisiterMovieNamePage(movieQo.getName(),
                    movieQo.getPage() * movieQo.getSize(), movieQo.getSize());
            int count = personRepository.findUsersByNotVisiterMovieName(movieQo.getName()).size();
            return new PageImpl(new ArrayList(list), pageable, (long)count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/friend")
    public String friend(ModelMap model) throws Exception{
        Set<Map<String,Object>> movies = movieRepository.findRatingMovie(0, 100);
        Iterable<Person> persons = personRepository.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("persons", persons);
        return "worth/friend";
    }

    @RequestMapping(value = "/friend_list")
    @ResponseBody
    public Page<Person> getFriendList(MovieQo movieQo) {
        try {
            Pageable pageable = new PageRequest(movieQo.getPage(), movieQo.getSize(), null);
            int count = personRepository.findFriendsNotVisiterMovie(movieQo.getId(), movieQo.getName()).size();
            Set<Person> list = personRepository.findFriendsNotVisiterMoviePage(movieQo.getId(), movieQo.getName(),
                    movieQo.getPage() * movieQo.getSize(), movieQo.getSize());
            return new PageImpl(new ArrayList(list), pageable, (long)count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
