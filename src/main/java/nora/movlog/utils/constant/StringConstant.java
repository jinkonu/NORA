package nora.movlog.utils.constant;

import static nora.movlog.utils.constant.NumberConstant.*;

public class StringConstant {
    // COMMON
    public static final String EMPTY = "";


    // KOBIS API
    public static final String kobisKey = "4c9099b4c7f44c7a34192082ead54dbe";
    public static final String kobisUrl = "http://www.kobis.or.kr";
    public static final String kobisSearchByIdUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
    public static final String kobisSearchByNameUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieList.json";


    // TMDB API
    public static final String TMDB_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMDk1YTE3ZWEzYmNmZDBmZGUyYzliYWQ5MzhiZjE4MiIsInN1YiI6IjY1NDMzZDllZWQyYWMyMDBhZWIwNGNlYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KPotpNx48WIbRzopB-5n2AUfekStI5LqGuI2mjV8pk4";
    public static final String TMDB_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_SEARCH_BY_QUERY_PATH = "search/movie";
    public static final String TMDB_SEARCH_BY_ID_PATH = "movie/";
    public static final String TMDB_CREDITS_PATH = "credits";
    public static final String TMDB_GRADE_PATH = "release_dates";


    // API SEARCH PARAMETERS
    public static final String NO_ARGS = "";
    public static final String LANGUAGE_KOREAN = "ko-KR";
    public static final String APPEND_TO_RESPONSE = "append_to_response";
    public static final String QUERY = "query";
    public static final String LANGUAGE = "language";
    public static final String PAGE = "page";


    // JSON_NODE MAPPING PARAMETERS
    public static final String JSON_NODE_CAST = "cast";
    public static final String JSON_NODE_CERTIFICATION = "certification";
    public static final String JSON_NODE_CREDITS = "credits";
    public static final String JSON_NODE_CREW = "crew";
    public static final String JSON_NODE_DIRECTOR = "Director";
    public static final String JSON_NODE_GENRES = "genres";
    public static final String JSON_NODE_ID = "id";
    public static final String JSON_NODE_ISO = "iso_3166_1";
    public static final String JSON_NODE_ISO_KR = "KR";
    public static final String JSON_NODE_ISO_US = "US";
    public static final String JSON_NODE_JOB = "job";
    public static final String JSON_NODE_NAME = "name";
    public static final String JSON_NODE_NAME_ORIGINAL = "original_name";
    public static final String JSON_NODE_ORIGINAL_LANGUAGE = "original_language";
    public static final String JSON_NODE_ORIGINAL_LANGUAGE_KO = "ko";
    public static final String JSON_NODE_POPULARITY = "popularity";
    public static final String JSON_NODE_PRODUCTION_COUNTRIES = "production_countries";
    public static final String JSON_NODE_RELEASE_DATE = "release_date";
    public static final String JSON_NODE_RELEASE_DATES = "release_dates";
    public static final String JSON_NODE_RESULTS = "results";
    public static final String JSON_NODE_RUNTIME = "runtime";
    public static final String JSON_NODE_TITLE = "title";
    public static final String JSON_NODE_TITLE_ORIGINAL = "original_title";


    // WATCHGRADE
    public static final String KR_ALL = "All";
    public static final String KR_TWELVE = "12";
    public static final String KR_FIFTEEN = "15";
    public static final String KR_ADULT = "18";

    public static final String US_ALL = "G";
    public static final String US_TWELVE_1 = "PG";
    public static final String US_TWELVE_2 = "PG-13";
    public static final String US_FIFTEEN_1 = "R";
    public static final String US_FIFTEEN_2 = "NC-17";


    // MOVIE CONTROLLER
    public static final String DEFAULT_SEARCH_PAGE = "0";
    public static final String DEFAULT_SEARCH_SIZE = "5";


    // USER
    public static final String NO_LOGIN_ID_ERROR = "아이디가 비어있습니다.";
    public static final String TOO_SHORT_LOGIN_ID_ERROR = "아이디 길이가 " + MIN_LOGIN_ID_LENGTH + "자를 넘어야 합니다.";
    public static final String TOO_LONG_LOGIN_ID_ERROR = "아이디 길이가 " + MAX_LOGIN_ID_LENGTH + "자를 넘습니다.";
    public static final String DUPLICATE_LOGIN_ID_ERROR = "아이디가 중복됩니다.";

    public static final String NO_PASSWORD_ERROR = "비밀번호가 비어있습니다.";
    public static final String NOT_EQUAL_PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";

    public static final String NO_NICKNAME_ERROR = "닉네임이 비어있습니다.";
    public static final String TOO_SHORT_NICKNAME_ERROR = "닉네임 길이가 " + MIN_NICKNAME_LENGTH + "자를 넘어야 합니다.";
    public static final String TOO_LONG_NICKNAME_ERROR = "닉네임 길이가 " + MAX_NICKNAME_LENGTH + "자를 넘습니다.";

    public static final String NO_NOW_PASSWORD = "현재 비밀번호가 비어있습니다.";

    public static final String NO_SUCH_MEMBER = "해당 유저를 찾을 수 없습니다.";

    // CONTROLLER
    public static final String LAST_URL = "lastUrl";


    // TEST
    public static final String TEST_CASE_MEMBER_LOGIN_ID = "gingNangBoyz";
    public static final String TEST_CASE_MEMBER_PASSWORD = "tokyo";
    public static final String TEST_CASE_MEMBER_NICKNAME = "babybaby";
}
