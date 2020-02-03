package pl.seb.czech.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "title_info_id")
    private TitleInfo titleInfo;
    
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus bookStatus;
    
    @OneToOne(mappedBy = "book")
    private Rent rent;


    public Book(@NotNull TitleInfo titleInfo, @NotNull BookStatus bookStatus) {
        this.titleInfo = titleInfo;
        this.bookStatus = bookStatus;
    }
    
    @PreRemove
    private void removeTitleInfo() {
        this.titleInfo = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                titleInfo.equals(book.titleInfo) &&
                bookStatus == book.bookStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titleInfo, bookStatus);
    }
}
