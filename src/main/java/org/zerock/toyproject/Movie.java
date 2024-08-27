package org.zerock.toyproject;

import java.util.List;
import lombok.Getter;

@Getter
public class Movie {

    private int movieSeq;
    private String title;
    private List<String> directorsNm;
    private String repRlsDate;


    public static Movie createMovies(int movieSeq, String title,
                                     List<String> directorsNm, String repRlsDate) {
        Movie movie = new Movie();
        movie.movieSeq = movieSeq;
        movie.title = title;
        movie.directorsNm = directorsNm;
        movie.repRlsDate = repRlsDate;
        return movie;
    }

}
