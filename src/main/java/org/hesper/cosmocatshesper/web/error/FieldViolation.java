package org.hesper.cosmocatshesper.web.error;

import lombok.Value;

@Value
public class FieldViolation {
    String field;
    String message;
}
