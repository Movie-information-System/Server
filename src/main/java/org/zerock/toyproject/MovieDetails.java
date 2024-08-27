package org.zerock.toyproject;

import java.util.List;
import lombok.Getter;

@Getter
public class MovieDetails {

    private String title;
    private int movieSeq;
    private List<String> directors;
    private String repRlsDate;
    private String plot;
    private String poster;


    public static MovieDetails createMovieDetails(String title, int movieSeq,
                                           List<String> directors, String repRlsDate,
                                           String plot, String poster) {
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.title = title;
        movieDetails.movieSeq = movieSeq;
        movieDetails.directors = directors;
        movieDetails.repRlsDate = repRlsDate;
        movieDetails.plot = plot;
        movieDetails.poster = poster;
        return movieDetails;
    }
}
