package nora.movlog.controller.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.movie.NationService;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.ID_URI;
import static nora.movlog.utils.constant.StringConstant.NATION_URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(NATION_URI)
@RestController
public class NationController {

    private final NationService nationService;



    // 국가명 수정
    @PostMapping(ID_URI + "/edit")
    public String edit(@PathVariable String id,
                       @RequestParam String name) {
        return nationService.edit(id, name);
    }
}
