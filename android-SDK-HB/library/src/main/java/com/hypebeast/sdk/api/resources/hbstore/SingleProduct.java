package com.hypebeast.sdk.api.resources.hbstore;

import com.hypebeast.sdk.api.exception.ApiException;
import com.hypebeast.sdk.api.model.hypebeaststore.ReponseNormal;
import com.hypebeast.sdk.api.model.hypebeaststore.ResponseListOrder;
import com.hypebeast.sdk.api.model.hypebeaststore.ResponseSingleProduct;
import com.hypebeast.sdk.api.model.symfony.OrderItem;
import com.hypebeast.sdk.api.model.symfony.ShoppingCart;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hesk on 7/1/2015.
 */
public interface SingleProduct {




    @GET("/products/{product_identification_no}")
    void PIDReq(
            final @Path("product_identification_no") long product_id, final Callback<ResponseSingleProduct> result) throws ApiException;

    @GET("/cart/add")
    void addCart(
            final @Query("variant_id") long product_id, final @Query("quantity") int amount, final Callback<ResponseListOrder> result) throws ApiException;

    @GET("/cart")
    void checkCart(final Callback<ShoppingCart> result) throws ApiException;

    @GET("/account/wishlist/add/{product_id}")
    void addWish(
            final @Path("product_id") long product_id, final Callback<ResponseSingleProduct> result) throws ApiException;

    @GET("/")
    void retrieve(
            final Callback<ResponseSingleProduct> result
    ) throws ApiException;



}
