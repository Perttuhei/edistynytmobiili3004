package com.example.edistynytmobiili3004.api

import com.example.edistynytmobiili3004.model.AddItemReq
import com.example.edistynytmobiili3004.model.EditItemReq
import com.example.edistynytmobiili3004.model.Item
import com.example.edistynytmobiili3004.model.ItemResponse
import com.example.edistynytmobiili3004.model.ItemsResponse
import com.example.edistynytmobiili3004.model.RentItemReq
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private val retrofit = createClient()

val itemsService = retrofit.create(ItemsApi::class.java)

interface ItemsApi {

    @GET("category/{category_id}/items")
    suspend fun getItems(
        @Path("category_id") categoryId: Int
    ): ItemsResponse

    @POST("category/{category_id}/items")
    suspend fun createItem(
        @Path("category_id") categoryId: Int,
        @Body req: AddItemReq,
        @Header("Authorization") bearerToken: String
    ): Item

    @POST("rentalitem/{rental_item_id}/rent")
    suspend fun rentItem(
        @Path("rental_item_id") itemId: Int,
        @Body req: RentItemReq,
        @Header("Authorization") bearerToken: String
    )

    @GET("rentalitem/{rental_item_id}")
    suspend fun getItem(
        @Path("rental_item_id") itemId: Int
    ): Item

    @DELETE("rentalitem/{id}")
    suspend fun removeItem(
        @Path("id") id: Int,
        @Header("Authorization") bearerToken: String
    )

    @PUT("rentalitem/{rental_item_id}")
    suspend fun editItem(
        @Path("rental_item_id") id: Int,
        @Body editItemReq: EditItemReq,
        @Header("Authorization") bearerToken: String
    ): ItemResponse

}