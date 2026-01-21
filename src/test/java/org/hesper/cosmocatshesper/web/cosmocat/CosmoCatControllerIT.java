package org.hesper.cosmocatshesper.web.cosmocat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.hesper.cosmocatshesper.featuretoggle.FeatureToggleExtension;
import org.hesper.cosmocatshesper.featuretoggle.FeatureToggles;
import org.hesper.cosmocatshesper.featuretoggle.annotation.DisabledFeatureToggle;
import org.hesper.cosmocatshesper.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@DisplayName("CosmoCatController feature toggle tests")
class CosmoCatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    @DisplayName("should return 200 when STELLAR_CATS_DIRECTORY is enabled")
    @EnabledFeatureToggle(FeatureToggles.STELLAR_CATS_DIRECTORY)
    void shouldReturn200WhenDirectoryEnabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 503 when STELLAR_CATS_DIRECTORY is disabled")
    @DisabledFeatureToggle(FeatureToggles.STELLAR_CATS_DIRECTORY)
    void shouldReturn503WhenDirectoryDisabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 200 when NEBULA_PROMO_BANNER is enabled")
    @EnabledFeatureToggle(FeatureToggles.NEBULA_PROMO_BANNER)
    void shouldReturn200WhenPromoEnabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats/promo"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 503 when NEBULA_PROMO_BANNER is disabled")
    @DisabledFeatureToggle(FeatureToggles.NEBULA_PROMO_BANNER)
    void shouldReturn503WhenPromoDisabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats/promo"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 200 when QUANTUM_MEOW_TRANSLATOR is enabled")
    @EnabledFeatureToggle(FeatureToggles.QUANTUM_MEOW_TRANSLATOR)
    void shouldReturn200WhenTranslatorEnabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats/translate").param("meow", "meow"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 503 when QUANTUM_MEOW_TRANSLATOR is disabled")
    @DisabledFeatureToggle(FeatureToggles.QUANTUM_MEOW_TRANSLATOR)
    void shouldReturn503WhenTranslatorDisabled() {
        mockMvc.perform(get("/api/v1/cosmo-cats/translate").param("meow", "meow"))
                .andExpect(status().isServiceUnavailable());
    }
}
