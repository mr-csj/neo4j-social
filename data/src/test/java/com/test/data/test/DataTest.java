package com.test.data.test;

import com.test.data.config.Neo4jConfig;
import com.test.data.domain.Cinema;
import com.test.data.domain.Movie;
import com.test.data.domain.Person;
import com.test.data.domain.Show;
import com.test.data.repository.PersonRepository;
import com.test.data.repository.ShowRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Neo4jConfig.class})
public class DataTest {
    private static Logger log = LoggerFactory.getLogger(DataTest.class);

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ShowRepository showRepository;

    @Before
    public void initData(){
       Cinema cinema = new Cinema();
       cinema.setName("凤凰影院");
       cinema.setCity("深圳");

       Movie movie = new Movie();
       movie.setName("终结者");

       Show show = new Show();
       show.setName("终结者首映");
       show.setMovie(movie);
       show.setCinema(cinema);
       show.setCreate(new Date());

       showRepository.save(show);
       Assert.notNull(show.getId());

       for(int i=1; i<=30; i++){
           Person person = new Person();
           person.setName("观众" + i);
           person.setSex(1);
           person.setCreate(new Date());

           if(i > 1) {
               Person friend = personRepository.findByName("观众" + (i - 1));
               person.beFriend(friend);
           }

           person.addVistiter(show);
           person.rate(show.getMovie(),i % 10,"很不错");

           personRepository.save(person);
       }

    }

    //@Test
    public void addData(){
        Cinema cinema = new Cinema();
        cinema.setName("汇众影院");
        cinema.setCity("深圳");

        Movie movie = new Movie();
        movie.setName("狼图腾");

        Show show = new Show();
        show.setName("狼图腾第一场");
        show.setMovie(movie);
        show.setCinema(cinema);
        show.setCreate(new Date());

        showRepository.save(show);
        //Show show = showRepository.findOne(2553L);
        Assert.notNull(show.getId());

        for(int i=1501; i<=1700; i++){
            Person person = new Person();
            person.setName("观众" + i);
            person.setSex(1);
            person.setCreate(new Date());

            if(i > 1) {
                Person friend = personRepository.findByName("观众" + (i - 1));
                person.beFriend(friend);
            }

            person.addVistiter(show);
            //person.rate(show.getMovie(),i % 10,"好看");

            personRepository.save(person);
        }
    }

}
