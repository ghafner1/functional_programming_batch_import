package hr.leapwise.functionalprogramming.service;

import hr.leapwise.functionalprogramming.FunctionalprogrammingApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals; // For JUnit 5 assertions

@ExtendWith(SpringExtension.class)
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
        long expectedCount = 1000 + testDataVolume; // 1000 from ApplicationStartup
        assertEquals(expectedCount, count);
    }
}