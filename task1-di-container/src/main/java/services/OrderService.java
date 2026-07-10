package services;

import annotations.Component;

@Component
public class OrderService {
    private static int instanceCounter = 0;

    public OrderService() {
        instanceCounter++;
        System.out.println("OrderService создан (#" + instanceCounter + ")");
    }

    public String getOrderDetails() {
        return "Детали заказа #12345";
    }
}