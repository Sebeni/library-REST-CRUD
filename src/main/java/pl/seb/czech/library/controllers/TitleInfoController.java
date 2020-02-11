package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.dto.TitleInfoDto;
import pl.seb.czech.library.mapper.LibraryMapper;
import pl.seb.czech.library.service.TitleInfoService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/library/title")
public class TitleInfoController {
    private TitleInfoService titleInfoService;
    private LibraryMapper libraryMapper;
    
    public final static String getAvailableBooksURL = "getAvb";
    public final static String getAllBooksURL = "getAll";
    public final static String getAllBooksByAuthorURL = "byAuthor";
    public final static String getAllBooksByAuthorAndTitleURL = "byAuthorTitle";
    public final static String addTitleInfo = "addTitleInfo";
    public final static String deleteByIdURL = "delete";
    public final static String getByIdURL = "byId";

    @RequestMapping(method = RequestMethod.GET, value = getAvailableBooksURL)
    public long getNumOfAvailableBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAvailableBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = getAllBooksURL)
    public long getNumOfAllBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAllBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = getAllBooksByAuthorURL)
    public List<TitleInfoDto> findByAuthor(@RequestParam String authorName) {
        return libraryMapper.mapToTitleInfoDtoList(titleInfoService.findByAuthor(authorName));
    }

    @RequestMapping(method = RequestMethod.GET, value = getAllBooksByAuthorAndTitleURL)
    public TitleInfoDto findByAuthorAndTitle(@RequestParam String authorName, @RequestParam String title) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findByAuthorAndTitle(authorName, title));
    }

    @RequestMapping(method = RequestMethod.POST, value = addTitleInfo, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TitleInfoDto addTitleInfo(@RequestBody TitleInfoDto titleInfoDto) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.addTitleInfo(libraryMapper.mapToTitleInfo(titleInfoDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = deleteByIdURL)
    public void deleteById(@RequestParam Long titleInfoId) {
        titleInfoService.deleteById(titleInfoId);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = getByIdURL) 
        public TitleInfoDto findById(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findById(titleInfoId));
    }
}
