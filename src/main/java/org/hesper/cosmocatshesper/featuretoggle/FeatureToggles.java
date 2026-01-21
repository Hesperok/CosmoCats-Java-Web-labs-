package org.hesper.cosmocatshesper.featuretoggle;

public enum FeatureToggles {
    STELLAR_CATS_DIRECTORY("stellarCatsDirectory"),
    QUANTUM_MEOW_TRANSLATOR("quantumMeowTranslator"),
    NEBULA_PROMO_BANNER("nebulaPromoBanner");

    private final String key;

    FeatureToggles(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
