package org.hesper.cosmocatshesper.domain.cart;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Cart {
    UUID id;

    @Singular
    List<CartItem> items;

    @Value
    @Builder(toBuilder = true)
    public static class CartItem {
        UUID productId;
        int quantity;
    }
}
