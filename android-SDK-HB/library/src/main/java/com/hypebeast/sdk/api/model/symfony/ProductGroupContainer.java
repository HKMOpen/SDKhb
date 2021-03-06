package com.hypebeast.sdk.api.model.symfony;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hesk on 10/4/15.
 */
public class ProductGroupContainer {
    @SerializedName("href")
    public String href;
    @SerializedName("title")
    public String title;
    @SerializedName("display_color")
    public String color;
    @SerializedName("id")
    public long rel_product_id;
}
