package pl.seb.czech.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;
    
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(
            name = "rent_date", 
            updatable = false)
    private LocalDate rentDate = LocalDate.now();


    @Column(name = "due_date")
    private LocalDate dueDate = rentDate.plusWeeks(2);
    
    @Column(name = "due_date_prolonged")
    private boolean dueDateProlonged;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return Objects.equals(id, rent.id) &&
                book.equals(rent.book) &&
                user.equals(rent.user) &&
                rentDate.equals(rent.rentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user, rentDate);
    }

    public Rent(User user, Book book) {
        this.user = user;
        this.book = book;
    }
    
    public void prolong(){
        dueDate = dueDate.plusWeeks(2);
        dueDateProlonged = true;
    }
}
