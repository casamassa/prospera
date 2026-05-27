package com.casamassa.prospera.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categorias",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["parent_category_id"])]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "tipo") val tipo: String, // "RECEITA" ou "DESPESA"
    @ColumnInfo(name = "parent_category_id") val parentCategoryId: Long? = null
)
