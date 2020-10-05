package hr.leapwise.functionalprogramming.service;

import hr.leapwise.functionalprogramming.FunctionalprogrammingApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith (SpringRunner.class)
@SpringBootTest(classes = FunctionalprogrammingApplication.class)
public  class ValueServiceTest
{

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testStart()
    {

        ValueService valueService = new ValueService(entityManager);

        valueService.start();

        valueService.save();
    }
}