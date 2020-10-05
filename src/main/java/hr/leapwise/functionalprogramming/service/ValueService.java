package hr.leapwise.functionalprogramming.service;

import com.cyan.commons.utility.validation.Defense;
import hr.leapwise.functionalprogramming.domain.DbValue;
import hr.leapwise.functionalprogramming.model.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
    private static final int BATCH_SIZE = 1000;

    private EntityManager entityManager;

    public ValueService(EntityManager entityManager) {
        this.entityManager = entityManager;
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
        final List<Value> results = Collections.nCopies(10000000, Value.builder()
                                                                       .name("www.hr")
                                                                       .created(ZonedDateTime.now())
                                                                       .createdBy("GH")
                                                                       .build());

            results.parallelStream().forEach(this::process);
    }

    /**
     * Simulate checking of existing Entities in DB
     * @param result
     */
    public void process(Value result)
    {
        if(!existingValues.contains(result))
        {
            values.add(result); // tu se događa exception, zašto? - List u koju spremamo nije bila thread safe.
        }
    }

    /**
     * Save values in DB by splitting List of values in smaller chunks (batches)
     */
    public void save()
    {
        System.out.println("Save start! --> " + ZonedDateTime.now());
        // koristeći lambde spremiti rezultate ne opterećujući procesor (koristiti chunkove podataka)
        if(values.size() > 0) {
            // create chunks of values
            IntStream.range(0, (values.size()+ BATCH_SIZE -1)/ BATCH_SIZE)
                     .mapToObj(i -> values.subList(i* BATCH_SIZE, Math.min(values.size(), (i+1)* BATCH_SIZE)))
                     .forEach(this::saveBatch);
        }
        System.out.println("Save end! --> " + ZonedDateTime.now());
    }

    /**
     * Batch save in DB
     * @param valuesBatch
     */
    public void saveBatch(final List<Value> valuesBatch) {

        Defense.notNull(valuesBatch, "database entities");
        Defense.notEmpty(valuesBatch, "database entities");


        valuesBatch.parallelStream()
                .collect(Collectors.toList())
                .forEach(value -> {

                //validate data
                Defense.notNull(value, "database entities");
                Defense.notNull(value.getName(), "name");
                Defense.notNull(value.getCreatedBy(), "createdBy");
                Defense.notNull(value.getCreated(), "created");

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

            System.out.println("Batch values inserted: "+BATCH_SIZE);
    }
}
