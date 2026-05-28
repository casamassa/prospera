package com.casamassa.prospera.domain.repository

import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    fun getSubcategoriesByParentId(parentId: Long): Flow<List<Category>>
    fun getAllCategories(): Flow<List<Category>>
}
