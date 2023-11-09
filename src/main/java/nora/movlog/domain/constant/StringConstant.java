package nora.movlog.domain.constant;

public class StringConstant {
    // KOBIS API
    public static String kobisKey = "4c9099b4c7f44c7a34192082ead54dbe";
    public static String kobisUrl = "http://www.kobis.or.kr";
    public static String kobisSearchByIdUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
    public static String kobisSearchByNameUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieList.json";

    // TMDB API
    public static String TMDB_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMDk1YTE3ZWEzYmNmZDBmZGUyYzliYWQ5MzhiZjE4MiIsInN1YiI6IjY1NDMzZDllZWQyYWMyMDBhZWIwNGNlYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KPotpNx48WIbRzopB-5n2AUfekStI5LqGuI2mjV8pk4";
    public static String TMDB_URL = "https://api.themoviedb.org/3/";
    public static String TMDB_SEARCH_BY_QUERY_PATH = "search/movie";
    public static String TMDB_SEARCH_BY_ID_PATH = "movie/";
    public static String TMDB_CREDITS_PATH = "credits";
    public static String TMDB_GRADE_PATH = "release_dates";

    // API SEARCH PARAMETERS
    public static String NO_ARGS = "";
    public static String LANGUAGE_KOREAN = "ko-KR";
}
