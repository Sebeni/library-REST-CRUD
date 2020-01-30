package pl.seb.czech.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Rent {
    
    @NotNull
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn
    private User user;
    
    @OneToOne
    private Book book;

    @Column(name = "rent_date", updatable = false)
    private LocalDateTime rentDate = LocalDateTime.now();

    @Column(name = "due_date")
    private LocalDateTime dueDate = rentDate.plusWeeks(2);
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return rentDate.equals(rent.rentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentDate);
    }
}
