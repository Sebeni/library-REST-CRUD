package pl.seb.czech.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
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
    
    
    @Column(name = "created_on", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdOn;
    
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            targetEntity = Rent.class
    )
    private Set<Rent> rents = new HashSet<>();

    
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
