package nora.movlog.dto.movie;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import nora.movlog.domain.movie.WatchGrade;

import java.util.*;

import static nora.movlog.domain.movie.WatchGrade.*;
import static nora.movlog.domain.constant.NumberConstant.*;
import static nora.movlog.domain.constant.StringConstant.*;

@Data
public class MovieTmdbDto {
    private String id;

    private String titleKo;
    private String titleEn;

    private double popularity;
    private String runTime;
    private String prdtYear;
    private Set<String> nation;
    private Map<Integer, String> genres;
    private Map<String, String> directors;
    private Map<String, String> actors;
    private WatchGrade watchGrade;

    public static MovieTmdbDto create(List<JsonNode> nodes) {
        MovieTmdbDto dto = new MovieTmdbDto();
        JsonNode node = nodes.get(0);
        JsonNode gradeNode = nodes.get(1);

        dto.setId(node.get(JSON_NODE_ID).asText());
        titleMapper(dto, node);
        dto.setPopularity(node.get(JSON_NODE_POPULARITY).asDouble(DEFAULT_POPULARITY));
        dto.setRunTime(node.get(JSON_NODE_RUNTIME).asText());
        dto.setPrdtYear(prdtYearMapper(node));
        dto.setNation(nationMapper(node));
        dto.setGenres(genreMapper(node));
        creditMapper(dto, node.get(JSON_NODE_CREDITS));
        dto.setWatchGrade(gradeMapper(gradeNode));

        return dto;
    }


    private static void titleMapper(MovieTmdbDto dto, JsonNode node) {
        // 한국 영화
        if (node.get(JSON_NODE_ORIGINAL_LANGUAGE).asText().equals(JSON_NODE_ORIGINAL_LANGUAGE_KO)) {
            dto.setTitleKo(node.get(JSON_NODE_TITLE).asText());
            dto.setTitleEn(EMPTY);
        }

        // 해외 영화
        else {
            dto.setTitleKo(node.get(JSON_NODE_TITLE).asText());
            dto.setTitleEn(node.get(JSON_NODE_TITLE_ORIGINAL).asText());
        }
    }

    private static String prdtYearMapper(JsonNode node) {
        String yearStr = node.get(JSON_NODE_RELEASE_DATE).asText();

        if (yearStr.length() < YEAR_SIZE) return EMPTY;

        else return yearStr.substring(ZERO, YEAR_SIZE);
    }

    private static Set<String> nationMapper(JsonNode node) {
        Set<String> nations = new HashSet<>();

        if (!node.get(JSON_NODE_PRODUCTION_COUNTRIES).isEmpty())
            for (JsonNode country : node.get(JSON_NODE_PRODUCTION_COUNTRIES))
                nations.add(country.get(JSON_NODE_ISO).asText());

        return nations;
    }

    private static Map<Integer, String> genreMapper(JsonNode node) {
        Map<Integer, String> genres = new HashMap<>();

        if (!node.get(JSON_NODE_GENRES).isEmpty())
            for (JsonNode genre : node.get(JSON_NODE_GENRES))
                genres.put(genre.get(JSON_NODE_ID).asInt(), genre.get(JSON_NODE_NAME).asText());

        return genres;
    }

    private static void creditMapper(MovieTmdbDto dto, JsonNode node) {
        JsonNode actorNode = node.get(JSON_NODE_CAST);
        JsonNode directorNode = node.get(JSON_NODE_CREW);

        dto.setActors(actorMapper(actorNode));
        dto.setDirectors(directorMapper(directorNode));
    }

    private static Map<String, String> actorMapper(JsonNode actorNode) {
        Map<String, String> actors = new HashMap<>();

        for (int i = 0; i < Math.min(3, actorNode.size()); i++)
            actors.put(
                    actorNode.get(i).get(JSON_NODE_ID).asText(),
                    actorNode.get(i).get(JSON_NODE_NAME_ORIGINAL).asText()
            );

        return actors;
    }

    private static Map<String, String> directorMapper(JsonNode directorNode) {
        Map<String, String> directors = new HashMap<>();

        for (int i = 0; i < directorNode.size(); i++)
            if (directorNode.get(i).get(JSON_NODE_JOB).asText().equalsIgnoreCase(JSON_NODE_DIRECTOR))
                directors.put(
                        directorNode.get(i).get(JSON_NODE_ID).asText(),
                        directorNode.get(i).get(JSON_NODE_NAME_ORIGINAL).asText()
                );

        return directors;
    }

    private static WatchGrade gradeMapper(JsonNode node) {
        JsonNode krGradeNode = null;
        JsonNode usGradeNode = null;
        JsonNode results = node.get(JSON_NODE_RESULTS);

        for (JsonNode grade : results) {
            if (
                    grade.get(JSON_NODE_ISO).asText().equals(JSON_NODE_ISO_KR) &&
                    !grade.get(JSON_NODE_RELEASE_DATES).get(ZERO).get(JSON_NODE_CERTIFICATION).asText().isEmpty()
            )
                krGradeNode = grade;
            else if (grade.get(JSON_NODE_ISO).asText().equals(JSON_NODE_ISO_US))
                usGradeNode = grade;
        }

        if (krGradeNode != null) {
            return krGradeMapper(krGradeNode);
        }
        else if (usGradeNode != null) {
            return usGradeMapper(usGradeNode);
        }

        return NONE;
    }

        private static WatchGrade krGradeMapper(JsonNode node) {
            return switch (node.get(JSON_NODE_RELEASE_DATES).get(ZERO).get(JSON_NODE_CERTIFICATION).asText()) {
                case KR_ALL -> ALL;
                case KR_TWELVE  -> TWELVE;
                case KR_FIFTEEN  -> FIFTEEN;
                case KR_ADULT  -> ADULT;
                default    -> NONE;
            };
        }

        private static WatchGrade usGradeMapper(JsonNode node) {
            return switch (node.get(JSON_NODE_RELEASE_DATES).get(ZERO).get(JSON_NODE_CERTIFICATION).asText()) {
                case US_ALL            -> ALL;
                case US_TWELVE_1, US_TWELVE_2  -> TWELVE;
                case US_FIFTEEN_1, US_FIFTEEN_2   -> ADULT;
                default             -> NONE;
            };
        }
}
