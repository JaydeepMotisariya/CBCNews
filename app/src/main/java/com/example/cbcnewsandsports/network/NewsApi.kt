package com.example.cbcnewsandsports.network

import com.example.cbcnewsandsports.model.NewsFeedModel
import retrofit2.Response
import retrofit2.http.GET


interface NewsApi {
    @GET("items?lineupSlug=news")
    suspend fun getNewsFeedList() : Response<ArrayList<NewsFeedModel>>
}