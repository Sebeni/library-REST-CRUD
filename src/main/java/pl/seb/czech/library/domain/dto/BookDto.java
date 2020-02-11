package pl.seb.czech.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.seb.czech.library.domain.BookStatus;

@AllArgsConstructor
@Getter
public class BookDto {
    private Long bookId;
    private TitleInfoDto titleInfoDto;
    private BookStatus bookStatus;
}
