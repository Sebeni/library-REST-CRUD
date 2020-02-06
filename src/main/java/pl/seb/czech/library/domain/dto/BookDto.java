package pl.seb.czech.library.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;

@AllArgsConstructor
@Getter
public class BookDto {
    private Long bookId;
    private TitleInfoDto titleInfoDto;
    private BookStatus bookStatus;
}
