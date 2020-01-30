package pl.seb.czech.bibliotheca.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Rent {
    
    @NotNull
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    private User user;
    
    @OneToOne
    private Book book;
    
    private LocalDate rentDate = LocalDate.now();
    private LocalDate dueDate = rentDate.plusWeeks(2);
    
}
