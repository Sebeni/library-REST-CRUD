package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.TitleInfo;
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

    @RequestMapping(method = RequestMethod.GET, value = "getAvb")
    public long getNumOfAvailableBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAvailableBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAll")
    public long getNumOfAllBooks(@RequestParam String title) {
        return titleInfoService.getNumOfAllBooks(title);
    }

    @RequestMapping(method = RequestMethod.GET, value = "byAuthor")
    public List<TitleInfoDto> findByAuthor(@RequestParam String authorName) {
        return libraryMapper.mapToTitleInfoDtoList(titleInfoService.findByAuthor(authorName));
    }

    @RequestMapping(method = RequestMethod.GET, value = "byAuthorTitle")
    public TitleInfoDto findByAuthorAndTitle(@RequestParam String authorName, @RequestParam String title) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findByAuthorAndTitle(authorName, title));
    }

    @RequestMapping(method = RequestMethod.POST, value = "addTitleInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TitleInfoDto addTitleInfo(@RequestBody TitleInfoDto titleInfoDto) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.addTitleInfo(libraryMapper.mapToTitleInfo(titleInfoDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "delete")
    public void deleteById(@RequestParam Long titleInfoId) {
        titleInfoService.deleteById(titleInfoId);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "byId") 
        public TitleInfoDto findById(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToTitleInfoDto(titleInfoService.findById(titleInfoId));
    }
}
