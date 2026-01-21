package org.hesper.cosmocatshesper.domain.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Order {
    UUID id;

    @Singular
    List<OrderItem> items;

    BigDecimal totalPrice;

    @Value
    @Builder(toBuilder = true)
    public static class OrderItem {
        UUID productId;
        int quantity;
        BigDecimal unitPrice;
    }
}
