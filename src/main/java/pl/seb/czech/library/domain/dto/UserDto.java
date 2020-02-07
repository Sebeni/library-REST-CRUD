package pl.seb.czech.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long UserId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private double fine;
}
