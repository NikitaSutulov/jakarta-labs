package com.team5.jakarta.data;

import com.team5.jakarta.model.Category;
import com.team5.jakarta.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final List<Category> categories = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final AtomicInteger categoryIdSeq = new AtomicInteger();
    private final AtomicInteger productIdSeq = new AtomicInteger();

    private DataStore() {
        initDemoData();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    private void initDemoData() {
        Category appliances = addCategory(new Category(0, "Побутова техніка", "Електронна побутова техніка", null));
        Category computers = addCategory(new Category(0, "Комп'ютери та ноутбуки", "Персональні комп'ютери, ноутбуки та аксесуари", null));
        Category smartphones = addCategory(new Category(0, "Смартфони та планшети", "Мобільні пристрої та аксесуари", null));

        Category largAppl = addCategory(new Category(0, "Велика побутова техніка", "Холодильники, пральні машини, плити", appliances.getId()));
        Category smallAppl = addCategory(new Category(0, "Дрібна побутова техніка", "Кавоварки, блендери, праски", appliances.getId()));

        Category fridges = addCategory(new Category(0, "Холодильники", "Дво- та однокамерні холодильники", largAppl.getId()));
        Category washing = addCategory(new Category(0, "Пральні машини", "Фронтальне та вертикальне завантаження", largAppl.getId()));

        Category laptops = addCategory(new Category(0, "Ноутбуки", "Портативні комп'ютери", computers.getId()));
        Category desktops = addCategory(new Category(0, "Настільні ПК", "Системні блоки та моноблоки", computers.getId()));

        Category androidPhones = addCategory(new Category(0, "Android-смартфони", "Смартфони на базі Android", smartphones.getId()));
        Category tablets = addCategory(new Category(0, "Планшети", "Android та iOS планшети", smartphones.getId()));

        addProduct(new Product(0, "BOSCH KGN39VI306",
                "Двокамерний холодильник з NoFrost, 366 л, нержавіюча сталь",
                23999.0, "https://content1.rozetka.com.ua/goods/images/big/18122527.jpg",
                fridges.getId(), true));
        addProduct(new Product(0, "Samsung RB5000A",
                "Двокамерний холодильник, 367 л, срібний",
                18499.0, "https://images.samsung.com/is/image/samsung/kz-ru-rb37a5200sawt-rb37a5200sa-wt-rperspactivesilver-308486889?$Q90_1248_936_F_PNG$",
                fridges.getId(), true));

        addProduct(new Product(0, "LG F2V5HS0W",
                "Пральна машина з паровою функцією, 9 кг, біла",
                15999.0, "https://content.rozetka.com.ua/goods/images/big/248080752.jpg",
                washing.getId(), true));

        addProduct(new Product(0, "Apple MacBook Air M2 13\"",
                "Ноутбук з чипом Apple M2, 8 ГБ RAM, 256 ГБ SSD, 13.6\"",
                54999.0, "https://content2.rozetka.com.ua/goods/images/big/269256826.jpg",
                laptops.getId(), true));
        addProduct(new Product(0, "ASUS VivoBook 15 X1500",
                "Ноутбук Intel Core i5-1235U, 16 ГБ RAM, 512 ГБ SSD, 15.6\" FHD",
                22499.0, "", laptops.getId(), true));
        addProduct(new Product(0, "Lenovo IdeaPad 5 Pro",
                "Ноутбук AMD Ryzen 7 5800H, 16 ГБ RAM, 512 ГБ SSD, 16\" 2.5K",
                31999.0, "", laptops.getId(), false));

        addProduct(new Product(0, "Apple iMac 24\" M3",
                "Моноблок з чипом Apple M3, 8 ГБ RAM, 256 ГБ SSD, 24\" Retina 4.5K",
                79999.0, "https://content2.rozetka.com.ua/goods/images/big/485318740.jpg",
                desktops.getId(), true));

        addProduct(new Product(0, "Samsung Galaxy S24 Ultra",
                "6.8\" QHD+, Snapdragon 8 Gen 3, 12 ГБ RAM, 256 ГБ, 200 МП",
                55999.0, "https://content1.rozetka.com.ua/goods/images/big/429440267.jpg",
                androidPhones.getId(), true));
        addProduct(new Product(0, "Google Pixel 8 Pro",
                "6.7\" LTPO OLED, Google Tensor G3, 12 ГБ RAM, 128 ГБ",
                42999.0, "https://content.rozetka.com.ua/goods/images/big/381556378.png",
                androidPhones.getId(), true));

        addProduct(new Product(0, "Apple iPad Air 11\" M2",
                "11\" Liquid Retina, Apple M2, 8 ГБ RAM, 128 ГБ, Wi-Fi",
                29999.0, "https://content1.rozetka.com.ua/goods/images/big/433639273.jpg",
                tablets.getId(), true));

        addProduct(new Product(0, "DeLonghi Dinamica ECAM350.55.B",
                "Автоматична кавоварка, 15 бар, 1,8 л, чорна",
                21999.0, "https://content2.rozetka.com.ua/goods/images/big/163132518.jpg",
                smallAppl.getId(), true));
    }

    public synchronized List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public synchronized List<Category> getRootCategories() {
        return categories.stream()
                .filter(Category::isRoot)
                .collect(Collectors.toList());
    }

    public synchronized List<Category> getChildCategories(int parentId) {
        return categories.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() == parentId)
                .collect(Collectors.toList());
    }

    public synchronized Category getCategoryById(int id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized List<Category> getCategoryBreadcrumb(int id) {
        List<Category> breadcrumb = new ArrayList<>();
        Category current = getCategoryById(id);
        while (current != null) {
            breadcrumb.add(0, current);
            if (current.getParentId() == null) break;
            current = getCategoryById(current.getParentId());
        }
        return breadcrumb;
    }

    public synchronized Category addCategory(Category category) {
        category.setId(categoryIdSeq.incrementAndGet());
        categories.add(category);
        return category;
    }

    public synchronized boolean updateCategory(Category updated) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == updated.getId()) {
                categories.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteCategory(int id) {
        List<Category> children = getChildCategories(id);
        for (Category child : children) {
            deleteCategory(child.getId());
        }
        products.removeIf(p -> p.getCategoryId() == id);
        return categories.removeIf(c -> c.getId() == id);
    }

    public synchronized List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public synchronized List<Product> getProductsByCategoryId(int categoryId) {
        return products.stream()
                .filter(p -> p.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }

    public synchronized Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized Product addProduct(Product product) {
        product.setId(productIdSeq.incrementAndGet());
        products.add(product);
        return product;
    }

    public synchronized boolean updateProduct(Product updated) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updated.getId()) {
                products.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }
}

