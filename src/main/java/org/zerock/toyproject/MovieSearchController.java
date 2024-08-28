package org.zerock.toyproject;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class MovieSearchController {

    //처음 주입으로 초기화 이후 불변성을 보장하기 위해 final 을 쓴다.
    private final MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @GetMapping("caching")
    public List<Object> mainCachingData(){
        return movieSearchService.redisCachingDataServe();
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
