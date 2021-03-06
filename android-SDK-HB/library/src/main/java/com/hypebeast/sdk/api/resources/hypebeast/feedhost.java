package com.hypebeast.sdk.api.resources.hypebeast;

import com.hypebeast.sdk.api.exception.ApiException;
import com.hypebeast.sdk.api.model.hbeditorial.Foundation;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostFromSearch;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostW;
import com.hypebeast.sdk.api.model.hbeditorial.ResponseSingle;
import com.hypebeast.sdk.api.model.hypebeaststore.ResponseSingleProduct;
import com.squareup.okhttp.Response;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hesk on 13/8/15.
 */
public interface feedhost {


    /**
     * @param page_number   page number
     * @param post_per_page limit by request and the max is 30
     * @param cb            callback
     * @throws ApiException error
     */
    @GET("/page/{page_no}")
    void the_recent_page(
            final @Path("page_no") int page_number,
            final @Query("limit") int post_per_page,
            final Callback<ResponsePostW> cb
    ) throws ApiException;

    /**
     * assume the limit is 10
     *
     * @param page_number page number
     * @param cb          callback
     * @throws ApiException error
     */
    @GET("/page/{page_no}")
    void the_recent_page(
            final @Path("page_no") int page_number,
            final Callback<ResponsePostW> cb
    ) throws ApiException;


    @GET("/{cate_name}/page/{page_no}")
    void cate_list(
            final @Path("page_no") int pagepage_number_no,
            final @Query("limit") int post_per_page,
            final @Path("cate_name") String tag_keyword,
            final Callback<ResponsePostW> cb
    ) throws ApiException;


    @GET("/{cate_name}/page/{page_no}")
    void cate_list(
            final @Path("page_no") int pagepage_number_no,
            final @Path("cate_name") String tag_keyword,
            final Callback<ResponsePostW> cb
    ) throws ApiException;



    @GET("/tags/{tag_text}/page/{page_no}")
    void tag_list(
            final @Path("page_no") int pagepage_number_no,
            final @Query("limit") int post_per_page,
            final @Path("tag_text") String tag_keyword,
            final Callback<ResponsePostW> cb
    ) throws ApiException;


    @GET("/tags/{tag_text}/page/{page_no}")
    void tag_list(
            final @Path("page_no") int pagepage_number_no,
            final @Path("tag_text") String tag_keyword,
            final Callback<ResponsePostW> cb
    ) throws ApiException;


    @GET("/search/page/{page_no}")
    void search(
            final @Query("s") String search_keyword,
            final @Query("limit") int post_per_page,
            final @Path("page_no") int page_number,
            final Callback<ResponsePostFromSearch> cb) throws ApiException;

    @GET("/search/page/{page_no}")
    void search(
            final @Query("s") String search_keyword,
            final @Path("page_no") int page_number,
            final Callback<ResponsePostFromSearch> cb) throws ApiException;


    @GET("/page/{page_no}")
    void atPage(
            final @Path("page_no") int page_number,
            final @Query("limit") int post_per_page,
            final Callback<ResponsePostW> cb
    ) throws ApiException;

    /**
     * adding additional page number to call the pagination API
     *
     * @param page_number the index of the page number
     * @param cb          the call back only
     * @throws ApiException n
     */
    @GET("/page/{page_no}")
    void atPage(
            final @Path("page_no") int page_number,
            final Callback<ResponsePostW> cb
    ) throws ApiException;

    /**
     * only used on installation override
     *
     * @param pid post ID
     * @param cb  call back object
     * @throws ApiException the exceptions
     */
    @GET("/wp-json/posts/{pid}")
    void the_post(
            final @Path("pid") long pid,
            final Callback<ResponseSingle> cb
    ) throws ApiException;


    /**
     * get the list of items
     *
     * @param post_per_page page per limit
     * @param cb            callback
     * @throws ApiException error
     */
    @GET("/")
    void getList(
            final @Query("limit") int post_per_page,
            final Callback<ResponsePostW> cb
    ) throws ApiException;

    /**
     * retrieve a list of post articles with the data continue from the full path calls
     *
     * @param cb the call back only
     * @throws ApiException n
     */
    @GET("/")
    void getList(
            final Callback<ResponsePostW> cb
    ) throws ApiException;

    /**
     * retrieve a single article with the full path url
     *
     * @param cb the call back only
     * @throws ApiException n
     */
    @GET("/")
    void getSingleArticle(
            final Callback<ResponseSingle> cb
    ) throws ApiException;

}
