package hr.leapwise.functionalprogramming;

import hr.leapwise.functionalprogramming.service.ValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@Transactional
public class ApplicationStartup
    implements ApplicationListener<ApplicationReadyEvent>
{

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        int startupDataVolume = 1000; // Reduced volume for startup during app init
        ValueService valueService = new ValueService(entityManager, startupDataVolume);

        valueService.start();

        valueService.save();
    }
}
