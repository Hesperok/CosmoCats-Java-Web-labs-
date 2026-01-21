package org.hesper.cosmocatshesper.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleProperties {

    private Map<String, Toggle> toggles = new HashMap<>();

    @Data
    public static class Toggle {
        private boolean enabled;
    }
}
