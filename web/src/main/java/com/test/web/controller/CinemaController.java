package com.test.web.controller;

import com.test.data.domain.Cinema;
import com.test.data.model.CinemaQo;
import com.test.data.service.CinemaService;
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

@Controller
@RequestMapping("/cinema")
public class CinemaController {
    private static Logger logger = LoggerFactory.getLogger(CinemaController.class);

    @Autowired
    private CinemaService cinemaService;

    @RequestMapping("/index")
    public String index(ModelMap model) throws Exception{
        return "cinema/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        Cinema cinema = cinemaService.findById(id);
        model.addAttribute("cinema",cinema);
        return "cinema/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Cinema> getList(CinemaQo cinemaQo) {
        try {
            return  cinemaService.findPage(cinemaQo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model,Cinema cinema){
        model.addAttribute("cinema", cinema);
        return "cinema/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Cinema cinema) throws Exception{
        cinemaService.save(cinema);
        logger.info("新增->ID="+cinema.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Cinema cinema = cinemaService.findById(id);
        model.addAttribute("cinema",cinema);
        return "cinema/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Cinema cinema) throws Exception{
        cinemaService.save(cinema);
        logger.info("修改->ID="+cinema.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        cinemaService.delete(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
