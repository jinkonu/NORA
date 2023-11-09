package nora.movlog.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import nora.movlog.domain.movie.WatchGrade;

import java.util.*;

import static nora.movlog.domain.movie.WatchGrade.*;

@Data
public class MovieTmdbDto {
    private String id;

    private String titleKo;
    private String titleEn;

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

        dto.setId(node.get("id").asText());
        titleMapper(dto, node);
        dto.setRunTime(node.get("runtime").asText());
        dto.setPrdtYear(prdtYearMapper(node));
        dto.setNation(nationMapper(node));
        dto.setGenres(genreMapper(node));
        creditMapper(dto, node.get("credits"));
        dto.setWatchGrade(gradeMapper(gradeNode));

        return dto;
    }

    private static void titleMapper(MovieTmdbDto dto, JsonNode node) {
        // 한국 영화
        if (node.get("original_language").asText().equals("ko")) {
            dto.setTitleKo(node.get("title").asText());
            dto.setTitleEn("");
        }

        // 해외 영화
        else {
            dto.setTitleKo(node.get("title").asText());
            dto.setTitleEn(node.get("original_title").asText());
        }
    }

    private static String prdtYearMapper(JsonNode node) {
        String yearStr=node.get("release_date").asText();
        if(yearStr.length()<4) return "";
        else return yearStr.substring(0, 4);
    }

    private static Set<String> nationMapper(JsonNode node) {
        Set<String> nations = new HashSet<>();

        if (!node.get("production_countries").isEmpty())
            for (JsonNode country : node.get("production_countries"))
                nations.add(country.get("iso_3166_1").asText());

        return nations;
    }

    private static Map<Integer, String> genreMapper(JsonNode node) {
        Map<Integer, String> genres = new HashMap<>();

        if (!node.get("genres").isEmpty())
            for (JsonNode genre : node.get("genres"))
                genres.put(genre.get("id").asInt(), genre.get("name").asText());

        return genres;
    }

    private static void creditMapper(MovieTmdbDto dto, JsonNode node) {
        JsonNode actorNode = node.get("cast");
        JsonNode directorNode = node.get("crew");

        dto.setActors(actorMapper(actorNode));
        dto.setDirectors(directorMapper(directorNode));
    }

    private static Map<String, String> actorMapper(JsonNode actorNode) {
        Map<String, String> actors = new HashMap<>();

        for (int i = 0; i < Math.min(3, actorNode.size()); i++)
            actors.put(actorNode.get(i).get("id").asText(), actorNode.get(i).get("original_name").asText());

        return actors;
    }

    private static Map<String, String> directorMapper(JsonNode directorNode) {
        Map<String, String> directors = new HashMap<>();

        for (int i = 0; i < directorNode.size(); i++)
            if (directorNode.get(i).get("job").asText().equals("Director"))
                directors.put(directorNode.get(i).get("id").asText(), directorNode.get(i).get("original_name").asText());

        return directors;
    }

    private static WatchGrade gradeMapper(JsonNode node) {
        JsonNode krGradeNode = null;
        JsonNode usGradeNode = null;
        JsonNode results = node.get("results");

        for (JsonNode grade : results) {
            if (grade.get("iso_3166_1").asText().equals("KR") && !grade.get("release_dates").get(0).get("certification").asText().isEmpty())
                krGradeNode = grade;
            else if (grade.get("iso_3166_1").asText().equals("US"))
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
            return switch (node.get("release_dates").get(0).get("certification").asText()) {
                case "All" -> ALL;
                case "12"  -> TWELVE;
                case "15"  -> FIFTEEN;
                case "18"  -> ADULT;
                default    -> NONE;
            };
        }

        private static WatchGrade usGradeMapper(JsonNode node) {
            return switch (node.get("release_dates").get(0).get("certification").asText()) {
                case "G"            -> ALL;
                case "PG", "PG-13"  -> TWELVE;
                case "R", "NC-17"   -> ADULT;
                default             -> NONE;
            };
        }
}
