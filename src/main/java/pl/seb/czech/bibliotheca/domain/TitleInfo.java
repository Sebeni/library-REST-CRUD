package pl.seb.czech.bibliotheca.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "titles_info")
public class TitleInfo {
    
    @NotNull
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String title;
    
    @NotNull
    private String author;
    
    @NotNull
    @Column(name = "publication_year")
    private Integer publicationYear;
    
    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "titleInfo",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Book> bookList;
    
}
