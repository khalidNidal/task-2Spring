package com.example.demo.routes;

public final class ProductRoutes {

    private ProductRoutes() {}

    public static final String API_V0_BASE = "/api/v0";

    public static final String BASE = API_V0_BASE + "/products";

    public static final String ID = "/{id}";
    public static final String INCREASE = "/{id}/increase";
    public static final String DECREASE = "/{id}/decrease";
    public static final String LOW_STOCK = "/low-stock";
    public static final String CATEGORY = "/category/{category}";
    public static final String INVENTORY_VALUE = "/inventory-value";

    public static final String SEARCH_CATEGORY = "/search/category";
    public static final String SEARCH_NAME = "/search/name";
    public static final String SEARCH_PRICE_RANGE = "/search/price-range";
}
