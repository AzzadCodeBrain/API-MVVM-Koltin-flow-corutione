package com.example.apicalling.model

import com.google.gson.annotations.SerializedName

data class ItemsModel(

  @SerializedName("status"  ) var status  : Boolean? = null,
  @SerializedName("message" ) var message : String?  = null,
  @SerializedName("data"    ) var data    : Data?    = Data(),
  @SerializedName("ip"      ) var ip      : String?  = null

)
data class Support (

  @SerializedName("url"  ) var url  : String? = null,
  @SerializedName("text" ) var text : String? = null

)
data class Data (

  @SerializedName("page"        ) var page       : Int?            = null,
  @SerializedName("per_page"    ) var perPage    : Int?            = null,
  @SerializedName("total"       ) var total      : Int?            = null,
  @SerializedName("total_pages" ) var totalPages : Int?            = null,
  @SerializedName("data"        ) var data       : ArrayList<SubData> = arrayListOf(),
  @SerializedName("support"     ) var support    : Support?        = Support()

)

data class SubData (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("email"      ) var email     : String? = null,
  @SerializedName("first_name" ) var firstName : String? = null,
  @SerializedName("last_name"  ) var lastName  : String? = null,
  @SerializedName("avatar"     ) var avatar    : String? = null

)