package com.student;

import container.AnnotationContainer;
import services.CheckoutService;
import services.OrderService;
import services.ShoppingCart;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Запуск DI-контейнера ===\n");

        // 1. Создаём контейнер
        AnnotationContainer container = new AnnotationContainer();

        // 2. Сканируем пакет с сервисами
        System.out.println("Сканирование пакета...");
        container.scan("services");
        System.out.println();

        // 3. Проверяем наличие бинов
        System.out.println("Проверка наличия бинов:");
        System.out.println("Есть бин 'orderService'? " + container.containsBean("orderService"));
        System.out.println("Есть бин 'shoppingCart'? " + container.containsBean("shoppingCart"));
        System.out.println("Есть бин 'checkoutService'? " + container.containsBean("checkoutService"));
        System.out.println();

        // 4. Получаем бины (ленивая инициализация - создаются только сейчас)
        System.out.println("Получение бинов (ленивая инициализация):");
        CheckoutService checkout = container.getBean(CheckoutService.class);
        OrderService order = container.getBean(OrderService.class);
        ShoppingCart cart = container.getBean(ShoppingCart.class);
        System.out.println();

        // 5. Проверяем, что бины инициализированы (синглтоны)
        System.out.println("Проверка, что бины - синглтоны:");
        CheckoutService checkout2 = container.getBean(CheckoutService.class);
        System.out.println("checkout == checkout2? " + (checkout == checkout2));
        System.out.println();

        // 6. Используем сервис
        checkout.checkout();

        // 7. Закрываем контейнер
        container.close();
        System.out.println("\n=== Работа контейнера завершена ===");
    }
}