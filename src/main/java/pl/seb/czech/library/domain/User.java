package pl.seb.czech.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
@Setter
public class User {
    
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
   
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;
    
    @Column(name = "birthDate")
    @NotNull
    private LocalDate birthDate;
    
    @Column(
            name = "created_on",
            updatable = false
    )
    private LocalDateTime createdOn = LocalDateTime.now();
    
    
    @Column(name = "fine_to_pay")
    private BigDecimal fine = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    
    public User(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                birthDate.equals(user.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate);
    }
    
    
    public void addToFine(BigDecimal howMuch) {
        fine = fine.add(howMuch);
    }
    
    public void payFine(BigDecimal howMuch) {
        fine = fine.subtract(howMuch);
    }
    
}
