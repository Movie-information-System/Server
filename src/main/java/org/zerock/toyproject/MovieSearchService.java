package org.zerock.toyproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MovieSearchService {

    @Value("${spring.kmdb.api.key}")
    private String serviceKey;

    private final RedisTemplate<String, String> redisTemplate;

    public MovieSearchService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public List<Movie> moviesSearch(String keyword) {
        RestClient restClient = RestClient.create();

        String result = restClient.get()
                .uri("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?"
                        + "collection=kmdb_new2&"
                        + "listCount=20&"
                        + "query="+keyword+"&"
                        + "type=극영화&"
                        + "ServiceKey="+serviceKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        return getMovieList(result);
    }
    @Cacheable(cacheNames = "movieDetails", key = "'movie:movieSeq:'+#movieSequence+':title:'+#movieTitle", cacheManager = "movieCacheManager")
    public MovieDetails movieDetails(String movieTitle, String movieSequence) {
        RestClient restClient = RestClient.create();

        String result = restClient.get()
                .uri("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?"
                        + "collection=kmdb_new2&"
                        + "title="+movieTitle+"&"
                        + "movieSeq="+movieSequence+"&"
                        + "ServiceKey="+serviceKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        return getMovieDetails(result);
    }

    private static List<Movie> getMovieList(String result) {
        List<Movie> movies = new ArrayList<>();
        try{
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(result);
            JsonNode dataArray = rootNode.path("Data").get(0).path("Result");

            for(JsonNode data : dataArray){
                int movieSeq = data.path("movieSeq").asInt();

                String title = data.path("title").asText().replaceAll("!HE|!HS|\\s","");

                List<String> directorsNm = new ArrayList<>();
                JsonNode directorsArray = data.path("directors").path("director");
                for (JsonNode director : directorsArray) {
                    String directorNm = director.path("directorNm").asText();
                    directorsNm.add(directorNm);
                }

                String repRlsDate = data.path("repRlsDate").asText();

                movies.add(Movie.createMovies(movieSeq,title,directorsNm,repRlsDate));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    private static MovieDetails getMovieDetails(String result) {
        MovieDetails movieDetails;
        try{
            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(result);
            JsonNode dataArray = jsonNode.path("Data").get(0).path("Result").get(0);

            String title = dataArray.path("title").asText()
                    .replaceAll("!HE|!HS|\\s","");
            int movieSeq = dataArray.path("movieSeq").asInt();

            List<String> directors = new ArrayList<>();
            JsonNode directorsArray = dataArray.path("directors").path("director");
            for (JsonNode director : directorsArray) {
                String directorNm = director.path("directorNm").asText();
                directors.add(directorNm);
            }

            String repRlsDate = dataArray.path("repRlsDate").asText();

            String plot ="";
            JsonNode plotsArray = dataArray.path("plots").path("plot");
            for(JsonNode p : plotsArray){
                if(p.path("plotLang").asText().equals("한국어")){
                    plot = p.path("plotText").asText();
                    break;
                }
            }

            String poster = dataArray.path("posters").asText().split("\\|")[0];

            movieDetails = MovieDetails.createMovieDetails(title,movieSeq,directors,repRlsDate,plot,poster);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return movieDetails;
    }

    //Redis에 캐싱된 값들을 가져오는 과정
    public List<Object> redisCachingDataServe() {
        Set<String> keys = redisTemplate.keys("*");
        List<Object> values = new ArrayList<>();

        int count = 0;
        if (keys != null) {
            for (String key : keys) {
                if (count >= 5) break; // 5개의 value만 가져오기
                values.add(redisTemplate.opsForValue().get(key));
                count++;
            }
        }

        return values;
    }
}
