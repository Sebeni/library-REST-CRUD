package pl.seb.czech.library.visualTesting;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.seb.czech.library.service.BookService;
import pl.seb.czech.library.service.RentService;

@Component
public class ForH2Debug implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    DataPreparer dataPreparer;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    RentService rentService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        dataPreparer.prepareData();

    }


    
    
}
