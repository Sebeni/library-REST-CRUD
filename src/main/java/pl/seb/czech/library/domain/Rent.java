package pl.seb.czech.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return book.equals(rent.book) &&
                user.equals(rent.user) &&
                rentDate.equals(rent.rentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, user, rentDate);
    }

    public Rent(User user, Book book) {
        this.user = user;
        this.book = book;
    }
}
