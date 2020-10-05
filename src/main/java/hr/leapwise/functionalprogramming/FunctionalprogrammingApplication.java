package hr.leapwise.functionalprogramming;

import hr.leapwise.functionalprogramming.service.ValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;

@SpringBootApplication
public class FunctionalprogrammingApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FunctionalprogrammingApplication.class, args);
    }
}
