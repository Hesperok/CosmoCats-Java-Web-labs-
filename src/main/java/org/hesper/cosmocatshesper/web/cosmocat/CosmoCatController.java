package org.hesper.cosmocatshesper.web.cosmocat;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hesper.cosmocatshesper.service.cosmocat.CosmoCatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cosmo-cats")
@RequiredArgsConstructor
public class CosmoCatController {

    private final CosmoCatService cosmoCatService;

    @GetMapping
    public List<String> listCosmoCats() {
        return cosmoCatService.listCosmoCats();
    }

    @GetMapping("/translate")
    public String translateMeow(@RequestParam @NotBlank String meow) {
        return cosmoCatService.translateMeow(meow);
    }

    @GetMapping("/promo")
    public String getPromoBanner() {
        return cosmoCatService.getPromoBanner();
    }
}
