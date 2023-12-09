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
    public static final String DEFAULT_NOTIFICATION_SIZE = "10";


    // USER
    public static final String NO_LOGIN_ID_ERROR = "아이디가 비어있습니다.";
    public static final String DUPLICATE_LOGIN_ID_ERROR = "아이디가 중복됩니다.";

    public static final String NO_PASSWORD_ERROR = "비밀번호가 비어있습니다.";
    public static final String NO_PASSWORD_CHECK_ERROR = "비밀번호가 비어있습니다.";
    public static final String NOT_SAME_PASSWORD_CHECK_ERROR = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    public static final String NOT_EQUAL_PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";

    public static final String SAME_PASSWORD_ERROR = "새 비밀번호가 기존 비밀번호와 동일합니다.";

    public static final String NO_NICKNAME_ERROR = "닉네임이 비어있습니다.";

    public static final String NO_NOW_PASSWORD = "현재 비밀번호가 비어있습니다.";
    public static final String NO_NEW_PASSWORD = "새 비밀번호가 비어있습니다.";
    public static final String NO_NEW_PASSWORD_CHECK = "새 비밀번호 확인이 비어있습니다.";

    public static final String NO_SUCH_MEMBER = "해당하는 유저를 찾을 수 없습니다.";

    // CONTROLLER
    public static final String LAST_URL = "lastUrl";


    // URI
    public static final String NOTHING_URI = "";
    public static final String ALL_URI = "/**";
    public static final String HOME_URI = "/";
    public static final String LOGIN_URI = "/login";
    public static final String LOGOUT_URI = "/logout";
    public static final String JOIN_URI = "/join";
    public static final String VERIFY_URI = "/verify";
    public static final String RESEND_URI = "/resend";
    public static final String CHECK_VERIFY_URI = "/check-verify";
    public static final String SEARCH_URI = "/search";
    public static final String ID_URI = "/{id}";
    public static final String MOVIE_URI = "/movie";
    public static final String MEMBER_URI = "/member";
    public static final String POST_URI = "/post";
    public static final String LIKE_URI = "/likes";
    public static final String COMMENT_URI = "/comment";
    public static final String NOTIFICATION_URI = "/notification";
    public static final String BOOKMARK_URI = "/bookmark";
    public static final String SETTINGS_URI = "/settings";
    public static final String PASSWORD_URI = "/password";
    public static final String NICKNAME_URI = "/nickname";
    public static final String PROFILE_PIC_URI = "/profilepic";
    public static final String DELETE_URI = "/delete";
    public static final String IMAGE_URI = "/image";
    public static final String NATION_URI = "/nation";
    public static final String CHATGPT_URI = "/chatgpt";

    // AUTHORITIES
    public static final String AUTH_UNVERIFIED = "UNVERIFIED";
    public static final String AUTH_VERIFIED = "VERIFIED";

    // TEST
    public static final String TEST_CASE_MEMBER_LOGIN_ID = "gingNangBoyz";
    public static final String TEST_CASE_MEMBER_PASSWORD = "tokyo";
    public static final String TEST_CASE_MEMBER_NICKNAME = "babybaby";


    // NOTIFICATION
    public static final String COMMENT_TYPE = "댓글을 작성";
    public static final String LIKES_TYPE = "좋아요";
    public static final String FOLLOW_TYPE = "팔로우";
    public static final String UNFOLLOW_TYPE = "언팔로우";

    public static final String FOLLOWING = "following";
    public static final String FOLLOWER = "follower";

    // CHATGPT
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String MODEL = "text-davinci-003";
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String OPENAI_URL = "https://api.openai.com/v1/completions";
}
