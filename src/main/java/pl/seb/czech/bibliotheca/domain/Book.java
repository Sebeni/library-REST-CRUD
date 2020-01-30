package pl.seb.czech.bibliotheca.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Books")
public class Book {
    
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "title_info_id")
    private TitleInfo titleInfo;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
    
    @OneToOne(mappedBy = "book")
    private Rent rent;
    
    
}
