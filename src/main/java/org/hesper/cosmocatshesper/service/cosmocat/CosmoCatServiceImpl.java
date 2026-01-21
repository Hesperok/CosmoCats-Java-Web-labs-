package org.hesper.cosmocatshesper.service.cosmocat;

import java.util.List;
import org.hesper.cosmocatshesper.featuretoggle.FeatureToggles;
import org.hesper.cosmocatshesper.featuretoggle.annotation.FeatureToggle;
import org.springframework.stereotype.Service;

@Service
public class CosmoCatServiceImpl implements CosmoCatService {

    @Override
    @FeatureToggle(FeatureToggles.STELLAR_CATS_DIRECTORY)
    public List<String> listCosmoCats() {
        return List.of("Orion", "Nebula", "Comet", "Starwhisker");
    }

    @Override
    @FeatureToggle(FeatureToggles.QUANTUM_MEOW_TRANSLATOR)
    public String translateMeow(String meow) {
        return "Translated: " + meow;
    }

    @Override
    @FeatureToggle(FeatureToggles.NEBULA_PROMO_BANNER)
    public String getPromoBanner() {
        return "Nebula Promo: -42% on stardust snacks!";
    }
}
