package org.hesper.cosmocatshesper.featuretoggle.service;

import lombok.RequiredArgsConstructor;
import org.hesper.cosmocatshesper.config.FeatureToggleProperties;
import org.hesper.cosmocatshesper.featuretoggle.FeatureToggles;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleProperties properties;

    public boolean isEnabled(FeatureToggles toggle) {
        FeatureToggleProperties.Toggle cfg = properties.getToggles().get(toggle.key());
        return cfg != null && cfg.isEnabled();
    }
}
