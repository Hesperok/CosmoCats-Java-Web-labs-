package org.hesper.cosmocatshesper.featuretoggle.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hesper.cosmocatshesper.featuretoggle.annotation.FeatureToggle;
import org.hesper.cosmocatshesper.featuretoggle.exception.FeatureNotAvailableException;
import org.hesper.cosmocatshesper.featuretoggle.service.FeatureToggleService;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Before("@annotation(featureToggle)")
    public void checkFeature(FeatureToggle featureToggle) {
        String key = featureToggle.value().key();
        if (!featureToggleService.isEnabled(featureToggle.value())) {
            throw new FeatureNotAvailableException(key);
        }
    }
}
