package com.casamassa.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.casamassa.prospera.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Update
    suspend fun update(category: CategoryEntity): Int

    @Delete
    suspend fun delete(category: CategoryEntity): Int

    @Query("SELECT * FROM categorias")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categorias WHERE tipo = :type")
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categorias WHERE parent_category_id = :parentId")
    fun getSubcategoriesByParentId(parentId: Long): Flow<List<CategoryEntity>>
}
