package com.test.web.controller;

import com.test.data.domain.Movie;
import com.test.data.model.MovieQo;
import com.test.data.service.MovieService;
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

@Controller
@RequestMapping("/movie")
public class MovieController {
    private static Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @RequestMapping("/index")
    public String index(ModelMap model) throws Exception{
        return "movie/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        Movie movie = movieService.findById(id);
        model.addAttribute("movie",movie);
        return "movie/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Movie> getList(MovieQo movieQo) {
        try {
            return  movieService.findPage(movieQo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model,Movie movie){
        model.addAttribute("movie", movie);
        return "movie/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Movie movie, HttpServletRequest request) throws Exception{
        movieService.save(movie);
        logger.info("新增->ID="+movie.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Movie movie = movieService.findById(id);
        model.addAttribute("movie",movie);
        return "movie/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Movie movie) throws Exception{
        movieService.save(movie);
        logger.info("修改->ID="+movie.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        movieService.delete(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
