package pl.seb.czech.library.visualTesting;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ForH2Debug implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    DataPreparer dataPreparer;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        dataPreparer.prepareData();
    }


    
    
}
