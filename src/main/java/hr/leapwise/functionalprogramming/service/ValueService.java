package hr.leapwise.functionalprogramming.service;

import hr.leapwise.functionalprogramming.domain.DbValue;
import hr.leapwise.functionalprogramming.model.Value;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service Implementation for batch import using functional programming.
 *
 * @author Goran Hafner
 * @version 1.0
 */
@Service
public class ValueService
{
    private static final Logger logger = LoggerFactory.getLogger(ValueService.class);
    private static final int BATCH_SIZE = 1000;
    private int numberOfValuesToGenerate = 10000000; // Default value

    private EntityManager entityManager;

    @org.springframework.beans.factory.annotation.Autowired
    public ValueService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // This constructor is for manual instantiation, e.g. in tests or specific setups
    public ValueService(EntityManager entityManager, int numberOfValuesToGenerate) {
        this.entityManager = entityManager;
        this.numberOfValuesToGenerate = numberOfValuesToGenerate;
    }

    private final List<Value> existingValues = new ArrayList<>();
    //create thread safe synchronized list
    private final List<Value> values = Collections.synchronizedList(new ArrayList<>());

    /**
     * Create 10 000 000 Object Values
     */
    public void start()
    {
        // create 10 000 000 input values
        final List<Value> results = IntStream.range(0, this.numberOfValuesToGenerate).mapToObj(i -> Value.builder().name("www.hr").description("Description_" + i).created(ZonedDateTime.now()).createdBy("GH").build()).collect(Collectors.toList());

            results.parallelStream().forEach(this::process);
    }

    /**
     * Simulate checking of existing Entities in DB
     * @param result
     */
    public void process(Value result)
    {
        values.add(result);
    }

    /**
     * Save values in DB by splitting List of values in smaller chunks (batches)
     */
    public void save()
    {
        logger.info("Save start! --> {}", ZonedDateTime.now());
        // koristeći lambde spremiti rezultate ne opterećujući procesor (koristiti chunkove podataka)
        if(values.size() > 0) {
            // create chunks of values
            IntStream.range(0, (values.size()+ BATCH_SIZE -1)/ BATCH_SIZE)
                     .mapToObj(i -> values.subList(i* BATCH_SIZE, Math.min(values.size(), (i+1)* BATCH_SIZE)))
                     .forEach(this::saveBatch);
        }
        logger.info("Save end! --> {}", ZonedDateTime.now());
    }

    /**
     * Batch save in DB
     * @param valuesBatch
     */
    public void saveBatch(final List<Value> valuesBatch) {

        Assert.notNull(valuesBatch, "database entities");
        Assert.notEmpty(valuesBatch, "database entities");


        valuesBatch.stream()
                .forEach(value -> {

                //validate data
                Assert.notNull(value, "database entities");
                Assert.notNull(value.getName(), "name");
                Assert.notNull(value.getCreatedBy(), "createdBy");
                Assert.notNull(value.getCreated(), "created");

                // create DbValue Entity
                DbValue dbValue = new DbValue();
                dbValue.setName(value.getName());
                dbValue.setDescription(value.getDescription());
                dbValue.setCreatedBy(value.getCreatedBy());
                dbValue.setCreated(value.getCreated());
                dbValue.setExpirationDate(value.getExpirationDate());

                entityManager.persist(dbValue);
            });
            entityManager.flush();
            // HINT !!! after each batch insert, we have to release hibernate first level cache (to avoid in OutOfMemoryException)
            entityManager.clear();

            logger.info("Batch values processed: {}", valuesBatch.size());
    }
}
