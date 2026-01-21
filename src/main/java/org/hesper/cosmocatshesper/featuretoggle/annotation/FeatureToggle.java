package org.hesper.cosmocatshesper.featuretoggle.annotation;

import java.lang.annotation.*;
import org.hesper.cosmocatshesper.featuretoggle.FeatureToggles;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureToggle {
    FeatureToggles value();
}
