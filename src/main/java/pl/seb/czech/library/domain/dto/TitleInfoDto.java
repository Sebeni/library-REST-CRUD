package pl.seb.czech.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class TitleInfoDto {
    private Long TitleInfoId;
    private String title;
    private String author;
    private Integer publicationYear;
    private BigDecimal price;
}
