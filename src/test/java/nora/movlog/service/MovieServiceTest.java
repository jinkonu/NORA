package nora.movlog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieServiceTest {

    @Autowired MovieService movieService;

    @Test
    @DisplayName("findByKobisId_KOBIS_API_키_기반으로_영화_검색")
    void findByKobisId_KOBIS_API_키_기반으로_영화_검색() {

    }
}