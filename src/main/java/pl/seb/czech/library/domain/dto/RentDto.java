package pl.seb.czech.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class RentDto {
    private Long rentId;
    private BookDto bookDto;
    private UserDto userDto;
    private LocalDate rentDate;
    private LocalDate dueDate;
    private boolean dueDateProlonged;
}
