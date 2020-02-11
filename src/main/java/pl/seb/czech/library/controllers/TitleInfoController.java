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
    
    public final static String GET_AVAILABLE_BOOKS_URL = "getAvb";
    public final static String GET_ALL_BOOKS_URL = "getAll";
    public final static String GET_ALL_BOOKS_BY_AUTHOR_URL = "byAuthor";
    public final static String GET_ALL_BOOKS_BY_AUTHOR_AND_TITLE_URL = "byAuthorTitle";
    public final static String ADD_TITLE_INFO_URL = "addTitleInfo";
    public final static String DELETE_BY_ID_URL = "delete";
    public final static String GET_BY_ID_URL = "byId";

    @RequestMapping(method = RequestMethod.GET, value = GET_AVAILABLE_BOOKS_URL)
    public long getNumOfAvailableBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAvailableBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = GET_ALL_BOOKS_URL)
    public long getNumOfAllBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAllBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = GET_ALL_BOOKS_BY_AUTHOR_URL)
    public List<TitleInfoDto> findByAuthor(@RequestParam String authorName) {
        return libraryMapper.mapToTitleInfoDtoList(titleInfoService.findByAuthor(authorName));
    }

    @RequestMapping(method = RequestMethod.GET, value = GET_ALL_BOOKS_BY_AUTHOR_AND_TITLE_URL)
    public TitleInfoDto findByAuthorAndTitle(@RequestParam String authorName, @RequestParam String title) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findByAuthorAndTitle(authorName, title));
    }

    @RequestMapping(method = RequestMethod.POST, value = ADD_TITLE_INFO_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TitleInfoDto addTitleInfo(@RequestBody TitleInfoDto titleInfoDto) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.addTitleInfo(libraryMapper.mapToTitleInfo(titleInfoDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = DELETE_BY_ID_URL)
    public void deleteById(@RequestParam Long titleInfoId) {
        titleInfoService.deleteById(titleInfoId);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = GET_BY_ID_URL) 
        public TitleInfoDto findById(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findById(titleInfoId));
    }
}
