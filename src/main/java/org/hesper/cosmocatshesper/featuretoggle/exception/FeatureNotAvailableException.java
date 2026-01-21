package org.hesper.cosmocatshesper.featuretoggle.exception;

public class FeatureNotAvailableException extends RuntimeException {
    public static final String FEATURE_NOT_AVAILABLE_TEMPLATE = "Feature %s is not available";

    public FeatureNotAvailableException(String featureKey) {
        super(String.format(FEATURE_NOT_AVAILABLE_TEMPLATE, featureKey));
    }
}
