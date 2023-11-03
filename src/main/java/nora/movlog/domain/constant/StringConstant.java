package nora.movlog.domain.constant;

public class StringConstant {
    // KOBIS API
    public static String kobisKey = "4c9099b4c7f44c7a34192082ead54dbe";
    public static String kobisUrl = "http://www.kobis.or.kr";
    public static String kobisSearchByIdUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
    public static String kobisSearchByNameUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieList.json";

    // TMDB API
    public static String tmdbKey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMDk1YTE3ZWEzYmNmZDBmZGUyYzliYWQ5MzhiZjE4MiIsInN1YiI6IjY1NDMzZDllZWQyYWMyMDBhZWIwNGNlYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KPotpNx48WIbRzopB-5n2AUfekStI5LqGuI2mjV8pk4";
    public static String tmdbUrl = "https://api.themoviedb.org/3/";
    public static String tmdbSearchByQueryUrlPath = "search/movie";
    public static String tmdbSearchByIdUrlPath = "movie/";
    public static String tmdbTitlePath = "alternative_titles";
    public static String tmdbCreditsPath = "credits";
    public static String tmdbGradePath = "release_dates";

    // API SEARCH PARAMETERS
    public static String LANGUAGE_KOREAN = "ko-KR";
}
