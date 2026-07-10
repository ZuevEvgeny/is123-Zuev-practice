package services;

import annotations.Component;

@Component
public class ShoppingCart {
    private static int instanceCounter = 0;

    public ShoppingCart() {
        instanceCounter++;
        System.out.println("ShoppingCart создан (#" + instanceCounter + ")");
    }

    public void addItem(String item) {
        System.out.println("Товар добавлен в корзину: " + item);
    }
}