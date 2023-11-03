package nora.movlog.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import nora.movlog.domain.Movie;
import nora.movlog.domain.WatchGrade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nora.movlog.domain.WatchGrade.*;

@Data
public class MovieTmdbDto {
    private String id;

    private String titleKo;
    private String titleEn;

    private String runTime;
    private String prdtYear;
    private Set<String> nation;
    private Set<String> genres;
    private Set<String> directors;
    private Set<String> actors;
    private WatchGrade watchGrade;

    public static Movie createMovie(List<JsonNode> nodes) {
        MovieTmdbDto dto = create(nodes);

        return Movie.createFromTmdbDto(dto);
    }

    public static MovieTmdbDto create(List<JsonNode> nodes) {
        MovieTmdbDto dto = new MovieTmdbDto();
        JsonNode node = nodes.get(0);
        JsonNode gradeNode = nodes.get(1);

        dto.setId(node.get("id").textValue());
        titleMapper(dto, node);
        dto.setRunTime(node.get("runtime").textValue());
        dto.setPrdtYear(prdtYearMapper(node));
        dto.setNation(nationMapper(node));
        dto.setGenres(genreMapper(node));
        creditMapper(dto, node);
        dto.setWatchGrade(gradeMapper(gradeNode));

        return dto;
    }

    private static WatchGrade gradeMapper(JsonNode node) {
        JsonNode krGradeNode = null;
        JsonNode usGradeNode = null;
        JsonNode results = node.get("results");

        for (JsonNode grade : results) {
            if (!grade.get("iso_3166_1").get("certification").textValue().isEmpty())
                krGradeNode = grade;
            else if (grade.get("iso_3166_1").textValue().equals("US"))
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
            return switch (node.get("release_dates").get(0).get("certification").textValue()) {
                case "All" -> ALL;
                case "12"  -> TWELVE;
                case "15"  -> FIFTEEN;
                case "18"  -> ADULT;
                default    -> NONE;
            };
        }

        private static WatchGrade usGradeMapper(JsonNode node) {
            return switch (node.get("release_dates").get(0).get("certification").textValue()) {
                case "G"            -> ALL;
                case "PG", "PG-13"  -> TWELVE;
                case "R", "NC-17"   -> ADULT;
                default             -> NONE;
            };
        }

    private static void titleMapper(MovieTmdbDto dto, JsonNode node) {
        if (node.get("original_language").textValue().equals("ko")) {
            dto.setTitleKo(node.get("title").textValue());
            dto.setTitleEn("");
        }
        else {
            dto.setTitleKo(node.get("title").textValue());
            dto.setTitleEn(node.get("original_title").textValue());
        }
    }

    private static String prdtYearMapper(JsonNode node) {
        return node.get("release_date").textValue().substring(0, 4);
    }

    private static Set<String> nationMapper(JsonNode node) {
        Set<String> nations = new HashSet<>();

        if (!node.get("production_countries").isEmpty())
            for (JsonNode country : node.get("production_countries"))
                nations.add(country.get("iso_3166_1").textValue());

        return nations;
    }

    private static Set<String> genreMapper(JsonNode node) {
        Set<String> genres = new HashSet<>();

        if (!node.get("genres").isEmpty())
            for (JsonNode genre : node.get("genres"))
                genres.add(genre.get("name").textValue());

        return genres;
    }

    private static void creditMapper(MovieTmdbDto dto, JsonNode node) {
        JsonNode actorNode = node.get("cast");
        JsonNode directorNode = node.get("crew");

        Set<String> actors = actorMapper(actorNode);
        Set<String> director = directorMapper(directorNode);

        dto.setActors(actors);
        dto.setDirectors(director);
    }

    private static Set<String> actorMapper(JsonNode actorNode) {
        Set<String> actors = new HashSet<>();

        for (int i = 0; i < Math.min(3, actorNode.size()); i++)
            actors.add(actorNode.get(i).get("original_name").textValue());

        return actors;
    }

    private static Set<String> directorMapper(JsonNode directorNode) {
        Set<String> directors = new HashSet<>();

        for (int i = 0; i < directorNode.size(); i++)
            if (directorNode.get(i).get("job").textValue().equals("director"))
                directors.add(directorNode.get(i).get("original_name").textValue());

        return directors;
    }
}
