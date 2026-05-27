package com.casamassa.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.casamassa.prospera.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Query("SELECT * FROM categorias WHERE tipo = :type")
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categorias WHERE parent_category_id = :parentId")
    fun getSubcategoriesByParentId(parentId: Long): Flow<List<CategoryEntity>>
}
