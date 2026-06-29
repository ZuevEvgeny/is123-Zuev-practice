package container;

import annotations.Autowired;
import annotations.Component;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC-контейнер, управляющий созданием и внедрением зависимостей бинов.
 * Поддерживает аннотации {@link Component} и {@link Autowired}.
 *
 * <p>Основные возможности:</p>
 * <ul>
 *   <li>Сканирование пакетов для поиска компонентов</li>
 *   <li>Регистрация бинов с автоматическим или явным именем</li>
 *   <li>Ленивое создание экземпляров при первом запросе</li>
 *   <li>Внедрение зависимостей через конструктор и поля</li>
 * </ul>
 */
public class AnnotationContainer {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationContainer.class);

    // Хранилище определений бинов (имя -> класс)
    private final Map<String, Class<?>> beanDefinitions = new ConcurrentHashMap<>();

    // Хранилище созданных экземпляров (имя -> экземпляр)
    private final Map<String, Object> beanInstances = new ConcurrentHashMap<>();

    // Кэш соответствия типа -> имя бина (для быстрого поиска по типу)
    private final Map<Class<?>, String> typeToBeanName = new ConcurrentHashMap<>();

    /**
     * Сканирует указанный пакет и все подпакеты, находит классы с аннотацией {@link Component}.
     *
     * @param packageName имя пакета для сканирования
     * @throws IllegalArgumentException если бин с таким именем уже зарегистрирован
     */
    public void scan(String packageName) {
        logger.info("Начало сканирования пакета: {}", packageName);

        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(packagePath);

        if (resource == null) {
            logger.warn("Пакет {} не найден", packageName);
            return;
        }

        File directory = new File(resource.getFile());
        if (!directory.exists()) {
            logger.warn("Директория {} не существует", directory.getPath());
            return;
        }

        scanDirectory(directory, packageName);
        logger.info("Завершено сканирование пакета: {}. Найдено бинов: {}", packageName, beanDefinitions.size());
    }

    /**
     * Рекурсивно сканирует директорию для поиска классов с аннотацией @Component.
     */
    private void scanDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        register(clazz);
                        logger.debug("Найден компонент: {}", className);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("Не удалось загрузить класс: {}", className, e);
                }
            }
        }
    }

    /**
     * Регистрирует класс как бин с автоматическим определением имени.
     *
     * @param beanClass класс, помеченный аннотацией {@link Component}
     * @throws IllegalArgumentException если класс не аннотирован @Component
     * @throws IllegalStateException если бин с таким именем уже зарегистрирован
     */
    public void register(Class<?> beanClass) {
        logger.info("Регистрация бина: {}", beanClass.getName());
        validateComponent(beanClass);

        Component component = beanClass.getAnnotation(Component.class);
        String beanName = component.value().isEmpty()
                ? toLowerCaseFirstLetter(beanClass.getSimpleName())
                : component.value();

        registerBean(beanName, beanClass);
    }

    /**
     * Регистрирует класс с явно указанным именем.
     *
     * @param name имя бина
     * @param beanClass класс, помеченный аннотацией {@link Component}
     * @throws IllegalArgumentException если класс не аннотирован @Component
     * @throws IllegalStateException если бин с таким именем уже зарегистрирован
     */
    public void register(String name, Class<?> beanClass) {
        logger.info("Регистрация бина с именем '{}': {}", name, beanClass.getName());
        validateComponent(beanClass);
        registerBean(name, beanClass);
    }

    /**
     * Внутренний метод регистрации бина.
     */
    private void registerBean(String name, Class<?> beanClass) {
        if (beanDefinitions.containsKey(name)) {
            throw new IllegalStateException("Бин с именем '" + name + "' уже зарегистрирован");
        }

        beanDefinitions.put(name, beanClass);
        typeToBeanName.put(beanClass, name);

        // Если класс реализует интерфейсы, регистрируем их для поиска по интерфейсу
        for (Class<?> interfaceClass : beanClass.getInterfaces()) {
            typeToBeanName.put(interfaceClass, name);
        }

        logger.debug("Бин '{}' зарегистрирован (класс: {})", name, beanClass.getName());
    }

    /**
     * Проверяет, что класс аннотирован @Component.
     */
    private void validateComponent(Class<?> beanClass) {
        if (!beanClass.isAnnotationPresent(Component.class)) {
            throw new IllegalArgumentException(
                    "Класс " + beanClass.getName() + " не аннотирован @Component"
            );
        }
    }

    /**
     * Возвращает бин по типу (ленивая инициализация).
     *
     * @param requiredType требуемый тип
     * @return экземпляр бина
     * @throws NoSuchBeanException если бин не найден
     * @throws NonUniqueBeanException если найдено несколько бинов данного типа
     */
    public <T> T getBean(Class<T> requiredType) {
        logger.debug("Запрос бина по типу: {}", requiredType.getName());

        String beanName = resolveBeanNameByType(requiredType);
        if (beanName == null) {
            throw new NoSuchBeanException("Бин типа " + requiredType.getName() + " не найден");
        }

        return getBean(beanName, requiredType);
    }

    /**
     * Возвращает бин по имени (ленивая инициализация).
     *
     * @param name имя бина
     * @return экземпляр бина
     * @throws NoSuchBeanException если бин не найден
     */
    public Object getBean(String name) {
        logger.debug("Запрос бина по имени: {}", name);

        if (!containsBean(name)) {
            throw new NoSuchBeanException("Бин с именем '" + name + "' не найден");
        }

        return getOrCreateBean(name);
    }

    /**
     * Возвращает бин по имени с проверкой типа.
     *
     * @param name имя бина
     * @param requiredType требуемый тип
     * @return экземпляр бина
     * @throws NoSuchBeanException если бин не найден
     * @throws BeanTypeMismatchException если тип бина несовместим с требуемым
     */
    public <T> T getBean(String name, Class<T> requiredType) {
        logger.debug("Запрос бина по имени '{}' с проверкой типа {}", name, requiredType.getName());

        Object bean = getBean(name);

        if (!requiredType.isAssignableFrom(bean.getClass())) {
            throw new BeanTypeMismatchException(
                    "Бин '" + name + "' имеет тип " + bean.getClass().getName() +
                            ", а ожидался " + requiredType.getName()
            );
        }

        return requiredType.cast(bean);
    }

    /**
     * Проверяет наличие определения бина по имени.
     *
     * @param name имя бина
     * @return true если определение существует
     */
    public boolean containsBean(String name) {
        return beanDefinitions.containsKey(name);
    }

    /**
     * Проверяет наличие ровно одного бина данного типа.
     *
     * @param type тип для проверки
     * @return true если существует ровно один бин данного типа
     */
    public boolean containsBean(Class<?> type) {
        long count = typeToBeanName.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(type) || type.isAssignableFrom(entry.getKey()))
                .count();
        return count == 1;
    }

    /**
     * Освобождает ресурсы и очищает хранилище созданных бинов.
     */
    public void close() {
        logger.info("Закрытие контейнера. Очистка {} созданных бинов", beanInstances.size());
        beanInstances.clear();
    }

    /**
     * Создаёт экземпляр бина (внутренний метод).
     */
    private Object getOrCreateBean(String name) {
        if (beanInstances.containsKey(name)) {
            logger.trace("Возврат существующего экземпляра бина '{}'", name);
            return beanInstances.get(name);
        }

        Class<?> beanClass = beanDefinitions.get(name);
        if (beanClass == null) {
            throw new NoSuchBeanException("Бин с именем '" + name + "' не зарегистрирован");
        }

        logger.info("Создание бина '{}' (класс: {})", name, beanClass.getName());
        Object instance = createBeanInstance(beanClass);
        autowireFields(instance);

        beanInstances.put(name, instance);
        logger.info("Бин '{}' создан и инициализирован", name);

        return instance;
    }

    /**
     * Определяет имя бина по типу.
     */
    private String resolveBeanNameByType(Class<?> type) {
        // Прямое соответствие
        if (typeToBeanName.containsKey(type)) {
            return typeToBeanName.get(type);
        }

        // Поиск по интерфейсам
        for (Map.Entry<Class<?>, String> entry : typeToBeanName.entrySet()) {
            if (entry.getKey().isAssignableFrom(type) && entry.getKey() != type) {
                return entry.getValue();
            }
        }

        // Проверка на множественное соответствие
        List<String> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, String> entry : typeToBeanName.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                candidates.add(entry.getValue());
            }
        }

        if (candidates.size() > 1) {
            throw new NonUniqueBeanException(
                    "Найдено несколько бинов типа " + type.getName() + ": " + candidates
            );
        }

        return candidates.isEmpty() ? null : candidates.get(0);
    }

    /**
     * Создаёт экземпляр класса, определяя подходящий конструктор.
     *
     * @param clazz класс для создания
     * @return созданный экземпляр
     * @throws IllegalStateException если нет подходящего конструктора
     * @throws BeanCreationException если создание провалилось
     */
    private Object createBeanInstance(Class<?> clazz) {
        logger.trace("Создание экземпляра класса: {}", clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> autowiredConstructor = null;
        int autowiredCount = 0;

        // Поиск конструктора с @Autowired
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                autowiredConstructor = constructor;
                autowiredCount++;
            }
        }

        if (autowiredCount > 1) {
            throw new IllegalStateException(
                    "Найдено несколько конструкторов с @Autowired в классе " + clazz.getName()
            );
        }

        // Если есть @Autowired конструктор - используем его
        if (autowiredConstructor != null) {
            logger.trace("Использование @Autowired конструктора в {}", clazz.getName());
            return createBeanWithAutowiredConstructor(autowiredConstructor);
        }

        // Иначе пытаемся использовать конструктор без параметров
        try {
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            logger.trace("Использование конструктора по умолчанию в {}", clazz.getName());
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Нет подходящего конструктора для класса " + clazz.getName() +
                            " (нет ни @Autowired конструктора, ни конструктора по умолчанию)"
            );
        } catch (Exception e) {
            throw new BeanCreationException(
                    "Не удалось создать экземпляр класса " + clazz.getName(), e
            );
        }
    }

    /**
     * Создаёт экземпляр бина через конструктор с @Autowired.
     *
     * @param constructor конструктор с @Autowired
     * @return созданный экземпляр с внедрёнными зависимостями через конструктор
     * @throws NoSuchBeanException если зависимость не найдена
     * @throws NonUniqueBeanException если найдено несколько кандидатов
     * @throws BeanCreationException если создание провалилось
     */
    private Object createBeanWithAutowiredConstructor(Constructor<?> constructor) {
        logger.trace("Создание экземпляра через @Autowired конструктор: {}", constructor.getName());

        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = getBean(paramTypes[i]);
            logger.trace("Параметр {}: внедрён бин типа {}", i, paramTypes[i].getName());
        }

        try {
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new BeanCreationException(
                    "Не удалось создать экземпляр через конструктор " + constructor.getName(), e
            );
        }
    }

    /**
     * Внедряет зависимости в поля, помеченные @Autowired.
     *
     * @param instance экземпляр для внедрения
     * @throws NoSuchBeanException если зависимость не найдена
     * @throws NonUniqueBeanException если найдено несколько кандидатов
     */
    private void autowireFields(Object instance) {
        Class<?> clazz = instance.getClass();
        logger.trace("Внедрение зависимостей в поля класса: {}", clazz.getName());

        // Рекурсивно обходим все поля класса, включая приватные из суперклассов
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    logger.trace("Внедрение поля: {}", field.getName());

                    Class<?> fieldType = field.getType();
                    Object dependency = getBean(fieldType);

                    try {
                        field.setAccessible(true);
                        field.set(instance, dependency);
                        logger.trace("Поле {} успешно внедрено", field.getName());
                    } catch (IllegalAccessException e) {
                        throw new BeanCreationException(
                                "Не удалось внедрить поле " + field.getName() +
                                        " в классе " + clazz.getName(), e
                        );
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Вспомогательный метод для приведения первой буквы к нижнему регистру.
     */
    private String toLowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}