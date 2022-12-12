package com.example.cbcnewsandsports.model

data class NewsFeedModel(
    val active: Boolean,
    val description: String,
    val draft: Boolean,
    val embedTypes: Any,
    val id: Int,
    val publishedAt: Long,
    val readablePublishedAt: String,
    val readableUpdatedAt: String,
    val source: String,
    val sourceId: String,
    val title: String,
    val type: String,
    val updatedAt: Long,
    val version: String,
    val typeAttributes: TypeAttribute,
) {
    override fun toString(): String {
        return type
    }
}

data class TypeAttribute(val imageLarge: String)