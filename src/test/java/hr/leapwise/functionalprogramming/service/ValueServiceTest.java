package hr.leapwise.functionalprogramming.service;

import hr.leapwise.functionalprogramming.FunctionalprogrammingApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RunWith (SpringRunner.class)
@SpringBootTest(classes = FunctionalprogrammingApplication.class)
public  class ValueServiceTest
{

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testStart()
    {

        int testDataVolume = 2500; // Using a smaller volume for testing
        ValueService valueService = new ValueService(entityManager, testDataVolume);

        valueService.start();
        valueService.save();

        // Add assertion
        long count = (long) entityManager.createQuery("SELECT COUNT(v) FROM DbValue v").getSingleResult();
        Assert.assertEquals(testDataVolume, count);
    }
}