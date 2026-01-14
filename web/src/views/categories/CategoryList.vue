<template>
  <div class="category-list">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Categories</h1>
      <Button variant="primary" @click="handleCreate">
        <i class="fas fa-plus mr-2"></i> Add Category
      </Button>
    </div>

    <Card class="mb-6">
      <div class="flex items-center justify-between mb-4">
        <div class="flex items-center gap-4">
          <div class="relative">
            <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search categories..."
              class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <select
            v-model="statusFilter"
            class="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
          </select>
        </div>
        <div class="flex items-center gap-2">
          <Button variant="outline" @click="handleExport">
            <i class="fas fa-download mr-2"></i> Export
          </Button>
          <Button variant="outline" @click="handleRefresh">
            <i class="fas fa-sync-alt mr-2"></i> Refresh
          </Button>
        </div>
      </div>

      <div v-if="loading" class="py-8">
        <LoadingSpinner />
      </div>

      <div v-else-if="categories.length === 0" class="py-8">
        <EmptyState
          title="No categories found"
          description="Get started by creating your first category."
          icon="folder"
        >
          <Button variant="primary" @click="handleCreate">
            Create Category
          </Button>
        </EmptyState>
      </div>

      <div v-else>
        <Table>
          <thead>
            <tr>
              <th class="w-12">
                <input type="checkbox" v-model="selectAll" />
              </th>
              <th>Name</th>
              <th>Description</th>
              <th>Products</th>
              <th>Status</th>
              <th>Created</th>
              <th class="text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="category in filteredCategories" :key="category.id">
              <td>
                <input
                  type="checkbox"
                  :value="category.id"
                  v-model="selectedCategories"
                />
              </td>
              <td>
                <div class="flex items-center">
                  <div
                    v-if="category.icon"
                    class="w-8 h-8 rounded-lg bg-blue-100 flex items-center justify-center mr-3"
                  >
                    <i :class="category.icon" class="text-blue-600"></i>
                  </div>
                  <span class="font-medium">{{ category.name }}</span>
                </div>
              </td>
              <td class="text-gray-600">
                {{ truncateText(category.description, 60) }}
              </td>
              <td>
                <Badge variant="info">{{ category.productCount }}</Badge>
              </td>
              <td>
                <Badge
                  :variant="category.status === 'active' ? 'success' : 'warning'"
                >
                  {{ category.status }}
                </Badge>
              </td>
              <td class="text-gray-600">
                {{ formatDate(category.createdAt) }}
              </td>
              <td class="text-right">
                <div class="flex justify-end gap-2">
                  <Button
                    variant="ghost"
                    size="sm"
                    @click="handleView(category.id)"
                  >
                    <i class="fas fa-eye"></i>
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    @click="handleEdit(category.id)"
                  >
                    <i class="fas fa-edit"></i>
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    @click="handleDelete(category.id)"
                  >
                    <i class="fas fa-trash text-red-600"></i>
                  </Button>
                </div>
              </td>
            </tr>
          </tbody>
        </Table>

        <div v-if="totalPages > 1" class="mt-6">
          <Pagination
            :current-page="currentPage"
            :total-pages="totalPages"
            :per-page="perPage"
            :total-items="totalItems"
            @page-changed="handlePageChange"
          />
        </div>
      </div>
    </Card>

    <div v-if="selectedCategories.length > 0" class="fixed bottom-4 right-4">
      <Card class="shadow-lg">
        <div class="flex items-center gap-4">
          <span class="text-gray-700">
            {{ selectedCategories.length }} selected
          </span>
          <Button variant="outline" size="sm" @click="handleBulkStatusChange">
            <i class="fas fa-toggle-on mr-2"></i> Change Status
          </Button>
          <Button variant="danger" size="sm" @click="handleBulkDelete">
            <i class="fas fa-trash mr-2"></i> Delete
          </Button>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import LoadingSpinner from '@/components/shared/LoadingSpinner.vue';
import Card from '@/components/ui/Card.vue';
import Button from '@/components/ui/Button.vue';
import Pagination from '@/components/ui/Pagination.vue';
import Table from '@/components/ui/Table.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatDate, truncateText } from '@/utils/formatters';
</script>
