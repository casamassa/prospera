package com.casamassa.prospera.data.repository

import com.casamassa.prospera.data.local.dao.CategoryDao
import com.casamassa.prospera.data.mapper.toDomain
import com.casamassa.prospera.data.mapper.toEntity
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun insertCategory(category: Category): Long {
        return dao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        dao.update(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        dao.delete(category.toEntity())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return dao.getCategoriesByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getSubcategoriesByParentId(parentId: Long): Flow<List<Category>> {
        return dao.getSubcategoriesByParentId(parentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
