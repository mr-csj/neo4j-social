package com.test.web.controller;

import com.test.data.domain.Cinema;
import com.test.data.domain.Movie;
import com.test.data.domain.Show;
import com.test.data.model.ShowQo;
import com.test.data.repository.CinemaRepository;
import com.test.data.repository.MovieRepository;
import com.test.data.service.ShowService;
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
import java.util.Date;


@Controller
@RequestMapping("/show")
public class ShowController {
    private static Logger logger = LoggerFactory.getLogger(ShowController.class);

    @Autowired
    private ShowService showService;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping("/index")
    public String index(ModelMap model) throws Exception{
        return "show/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        Show show = showService.findById(id);
        model.addAttribute("show",show);
        return "show/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Show> getList(ShowQo showQo) {
        try {
            return  showService.findPage(showQo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model,Show show){
        Iterable<Cinema> cinemas = cinemaRepository.findAll();
        Iterable<Movie> movies = movieRepository.findAll();
        model.addAttribute("cinemas",cinemas);
        model.addAttribute("movies",movies);
        model.addAttribute("show", show);
        return "show/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Show show, HttpServletRequest request) throws Exception{
        show.setCreate(new Date());
        showService.save(show);
        logger.info("新增->ID="+show.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Show show = showService.findById(id);
        Iterable<Cinema> cinemas = cinemaRepository.findAll();
        Iterable<Movie> movies = movieRepository.findAll();
        model.addAttribute("cinemas",cinemas);
        model.addAttribute("movies",movies);
        model.addAttribute("show",show);

        return "show/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Show show) throws Exception{
        showService.save(show);
        logger.info("修改->ID="+show.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        showService.delete(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
