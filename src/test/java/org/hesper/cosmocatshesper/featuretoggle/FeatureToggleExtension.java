package org.hesper.cosmocatshesper.featuretoggle;

import java.lang.reflect.Method;
import java.util.Optional;
import org.hesper.cosmocatshesper.config.FeatureToggleProperties;
import org.hesper.cosmocatshesper.featuretoggle.annotation.DisabledFeatureToggle;
import org.hesper.cosmocatshesper.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureToggleExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Optional<Method> testMethod = context.getTestMethod();
        if (testMethod.isEmpty()) {
            return;
        }

        FeatureToggleProperties props = getProperties(context);
        Method method = testMethod.get();

        if (method.isAnnotationPresent(EnabledFeatureToggle.class)) {
            String key = method.getAnnotation(EnabledFeatureToggle.class).value().key();
            setEnabled(props, key, true);
        }

        if (method.isAnnotationPresent(DisabledFeatureToggle.class)) {
            String key = method.getAnnotation(DisabledFeatureToggle.class).value().key();
            setEnabled(props, key, false);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Optional<Method> testMethod = context.getTestMethod();
        if (testMethod.isEmpty()) {
            return;
        }

        Method method = testMethod.get();
        String key = extractKey(method);
        if (key == null) {
            return;
        }

        boolean original = getOriginalFromYaml(context, key);
        FeatureToggleProperties props = getProperties(context);
        setEnabled(props, key, original);
    }

    private String extractKey(Method method) {
        if (method.isAnnotationPresent(EnabledFeatureToggle.class)) {
            return method.getAnnotation(EnabledFeatureToggle.class).value().key();
        }
        if (method.isAnnotationPresent(DisabledFeatureToggle.class)) {
            return method.getAnnotation(DisabledFeatureToggle.class).value().key();
        }
        return null;
    }

    private boolean getOriginalFromYaml(ExtensionContext context, String key) {
        Environment env = SpringExtension.getApplicationContext(context).getEnvironment();
        return env.getProperty("feature.toggles." + key + ".enabled", Boolean.class, Boolean.FALSE);
    }

    private FeatureToggleProperties getProperties(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(FeatureToggleProperties.class);
    }

    private void setEnabled(FeatureToggleProperties props, String key, boolean enabled) {
        FeatureToggleProperties.Toggle toggle =
                props.getToggles().computeIfAbsent(key, k -> new FeatureToggleProperties.Toggle());
        toggle.setEnabled(enabled);
    }
}
