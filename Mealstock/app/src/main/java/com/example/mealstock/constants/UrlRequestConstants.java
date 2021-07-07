package com.example.mealstock.constants;

public class UrlRequestConstants {

    public static final String NOVA_CODE_EXPLANATION = "https://de.openfoodfacts.org/nova";
    public static final String NUTRI_SCORE_EXPLANATION = "https://de.openfoodfacts.org/nutriscore";
    // Important! Insert ".json" after barcode number
    public static final String OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE= "https://de-en.openfoodfacts.org/api/v0/product/";
    public static final String OPENFOODFACTS_SEARCH_PRODUCT_WTIH_PRODUCT_NAME = "https://de-en.openfoodfacts.org/cgi/search.pl?action=process&json=true&sort_by=unique_scans_n&page_size=15&search_terms=";

    public static final String EDAMAM_BASE_URL= "https://api.edamam.com";
    public static final String EDAMAM_RECIPE_SEARCH= "https://api.edamam.com/api/recipes/v2?type=public&q=";
    public static final String EDAMAM_RECIPE_APP_ID_APP_KEY= "&app_id=f52c9d97&app_key=bb3d74e71e8993fdfd1d723e3843f383";


}
