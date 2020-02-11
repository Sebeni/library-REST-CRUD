package pl.seb.czech.library.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.Rent;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.domain.dto.BookDto;
import pl.seb.czech.library.domain.dto.RentDto;
import pl.seb.czech.library.domain.dto.TitleInfoDto;
import pl.seb.czech.library.domain.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class LibraryMapper {

    public BookDto mapToBookDto(Book book) {
        return new BookDto(book.getId(), mapToTitleInfoDto(book.getTitleInfo()), book.getBookStatus());
    }

    public Book mapToBook(BookDto bookDto) {
        return new Book(mapToBook(bookDto).getTitleInfo(), bookDto.getBookStatus());
    }

    public RentDto mapToRentDto(Rent rent) {
        return new RentDto(rent.getId(), mapToBookDto(rent.getBook()), mapToUserDto(rent.getUser()), rent.getRentDate(), rent.getDueDate(), rent.isDueDateProlonged());
    }

    public Rent mapToRent(RentDto rentDto) {
        return new Rent(mapToUser(rentDto.getUserDto()), mapToBook(rentDto.getBookDto()));
    }

    public TitleInfo mapToTitleInfo(TitleInfoDto titleInfoDto) {
        return new TitleInfo(titleInfoDto.getTitle(), titleInfoDto.getAuthor(), titleInfoDto.getPublicationYear(), titleInfoDto.getPrice());
    }

    public TitleInfoDto mapToTitleInfoDto(TitleInfo titleInfo) {
        return new TitleInfoDto(titleInfo.getId(), titleInfo.getTitle(), titleInfo.getAuthor(), titleInfo.getPublicationYear(), titleInfo.getPrice());
    }
    
    public List<TitleInfoDto> mapToTitleInfoDtoList(List<TitleInfo> titleInfoList) {
        return titleInfoList.stream()
                .map(this::mapToTitleInfoDto)
                .collect(Collectors.toList());
    }

    public User mapToUser(UserDto userDto){
        return new User(userDto.getFirstName(), userDto.getLastName(), userDto.getBirthDate());
    }

    public UserDto mapToUserDto(User user){
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getFine(), user.getCreatedOn());
    }
    
    public List<UserDto> mapToUserDtoList(List<User> userList) {
        return userList.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }
}
