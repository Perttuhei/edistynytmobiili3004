package com.example.edistynytmobiili3004.model

import com.google.gson.annotations.SerializedName

data class ItemsState(
    val list: List<Item> = emptyList(),
    val loading: Boolean = false,
    val err: String? = null,
    val isAddingItem: Boolean = false
)

data class AddItemState(
    val loading: Boolean = false,
    val ok: Boolean = false,
    val err: String? = null,
    val name: String = ""
)
data class RentItemState(
    val loading: Boolean = false,
    val err: String? = null,
    val item: Item = Item(),
    val status: String = ""
)

data class ItemState(
    val item: Item = Item(),
    val loading: Boolean = false,
    val ok: Boolean = false,
    val err: String? = null
)

data class DeleteItemState(
    val id: Int = 0,
    val err: String? = null
)

data class Item(
    @SerializedName("rental_item_id")
    val id: Int = 0,
    @SerializedName("rental_item_name")
    val name: String = "",
    @SerializedName("category_category")
    val category: Category = Category()
)
data class Category(
    @SerializedName("category_id")
    val id: Int = 0,
    @SerializedName("category_name")
    val name: String = ""
)

data class ItemsResponse(val items: List<Item> = emptyList())
data class ItemResponse(val item: Item = Item())

data class EditItemReq(
    @SerializedName("rental_item_name")
    val name: String
)

data class AddItemReq(
    @SerializedName("rental_item_name")
    val name: String,
    @SerializedName("created_by_user_id")
    val id: Int = 0
)
data class RentItemReq(
    @SerializedName("auth_user_auth_user_id")
    val userId: Int = 0
)