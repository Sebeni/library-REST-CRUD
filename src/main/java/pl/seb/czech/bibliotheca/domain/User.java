package pl.seb.czech.bibliotheca.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

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
    private String name;

    @Column(name = "last_name")
    @NotNull
    private String lastName;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", insertable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;
    
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.REMOVE
    )
    private Rent rent;

    
    public User(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
}
