package org.zerock.toyproject;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class MovieSearchController {

    private MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @GetMapping("main")
    public List<Movie> mainCachedMovies(){
        return null;
    }

    @GetMapping("search")
    public List<Movie> search(@RequestParam String keyword) {
        return movieSearchService.moviesSearch(keyword);
    }

    @GetMapping("details")
    public MovieDetails details(@RequestParam String title,
                                @RequestParam String movieSeq) {
        return movieSearchService.movieDetails(title,movieSeq);
    }
}
