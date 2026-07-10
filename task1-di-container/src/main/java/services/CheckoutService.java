package services;

import annotations.Autowired;
import annotations.Component;

@Component
public class CheckoutService {
    private static int instanceCounter = 0;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCart cart;

    public CheckoutService() {
        instanceCounter++;
        System.out.println("CheckoutService создан (#" + instanceCounter + ")");
    }

    public void checkout() {
        System.out.println("\n=== Оформление заказа ===");
        System.out.println(orderService.getOrderDetails());
        cart.addItem("Ноутбук");
        cart.addItem("Мышь");
        System.out.println("Заказ оформлен успешно!");
        System.out.println("=======================\n");
    }
}