<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Customer Segments</h1>
        <p class="page-subtitle">Group customers by behavior and value for targeted outreach</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="store.fetchAll(true)" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
        <button @click="showCreate = true" class="btn-accent text-sm">
          <i class="fas fa-plus text-xs mr-1"></i>New Segment
        </button>
      </div>
    </div>

    <!-- Summary row -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-3">
          <div class="stat-icon ni-p"><i class="fas fa-layer-group"></i></div>
        </div>
        <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ items.length }}</p>
        <p class="text-xs mt-1" style="color: var(--text-muted);">Total Segments</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-3">
          <div class="stat-icon ni-g"><i class="fas fa-users"></i></div>
        </div>
        <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ totalCustomers }}</p>
        <p class="text-xs mt-1" style="color: var(--text-muted);">Segmented Customers</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-3">
          <div class="stat-icon ni-b"><i class="fas fa-chart-pie"></i></div>
        </div>
        <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ formatCurrency(totalRevenue) }}</p>
        <p class="text-xs mt-1" style="color: var(--text-muted);">Revenue Covered</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-3">
          <div class="stat-icon ni-o"><i class="fas fa-lock"></i></div>
        </div>
        <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ systemCount }}</p>
        <p class="text-xs mt-1" style="color: var(--text-muted);">System Segments</p>
      </div>
    </div>

    <!-- Error state -->
    <div v-if="error" class="glass-card px-6 py-8 text-center">
      <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
      <p class="text-sm mb-3" style="color: var(--text-primary);">{{ error }}</p>
      <button @click="store.fetchAll()" class="btn-glass text-sm">Try again</button>
    </div>

    <!-- Segment Cards -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5">
      <div v-if="loading" v-for="n in 5" :key="n" class="glass-card p-6 animate-pulse">
        <div class="w-10 h-10 rounded-xl mb-4" style="background: var(--glass-bg);"></div>
        <div class="h-4 rounded mb-2" style="background: var(--glass-bg); width: 60%;"></div>
        <div class="h-3 rounded" style="background: var(--glass-bg); width: 80%;"></div>
      </div>
      <div v-for="seg in items" :key="seg.id" class="glass-card p-6 flex flex-col gap-4">
        <!-- Card header -->
        <div class="flex items-start justify-between">
          <div class="flex items-center gap-3">
            <div class="w-11 h-11 rounded-xl flex items-center justify-center text-white text-base"
              :style="{ background: seg.color, boxShadow: `0 4px 14px ${seg.color}55` }">
              <i :class="seg.icon"></i>
            </div>
            <div>
              <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">{{ seg.name }}</h3>
              <span v-if="seg.isSystem" class="text-[10px] px-1.5 py-0.5 rounded font-medium"
                style="background: rgba(var(--accent-rgb),0.12); color: var(--accent);">System</span>
            </div>
          </div>
          <div class="flex gap-1">
            <button @click="editSegment(seg)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit"
              :disabled="seg.isSystem">
              <i class="fas fa-pen"></i>
            </button>
            <button v-if="!seg.isSystem" @click="deleteSegment(seg)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs"
              title="Delete" style="color: var(--ni-red);">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>

        <p class="text-sm" style="color: var(--text-secondary);">{{ seg.description }}</p>

        <!-- Criteria chips -->
        <div class="flex flex-wrap gap-1.5">
          <span v-for="c in seg.criteria" :key="c.type"
            class="text-xs px-2 py-0.5 rounded-full"
            style="background: var(--glass-bg); color: var(--text-muted); border: 1px solid var(--glass-border);">
            {{ c.label }}
          </span>
        </div>

        <!-- Metrics -->
        <div class="grid grid-cols-3 gap-2 pt-2" style="border-top: 1px solid var(--glass-border);">
          <div class="text-center">
            <p class="text-lg font-bold" style="color: var(--text-primary);">{{ seg.customerCount }}</p>
            <p class="text-[10px]" style="color: var(--text-muted);">Customers</p>
          </div>
          <div class="text-center">
            <p class="text-lg font-bold" style="color: var(--text-primary);">{{ formatCurrency(seg.totalRevenue) }}</p>
            <p class="text-[10px]" style="color: var(--text-muted);">Revenue</p>
          </div>
          <div class="text-center">
            <p class="text-lg font-bold" style="color: var(--text-primary);">{{ formatCurrency(seg.avgOrderValue) }}</p>
            <p class="text-[10px]" style="color: var(--text-muted);">Avg Order</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Create / Edit Modal -->
    <Teleport to="body">
      <div v-if="showCreate || editing" class="fixed inset-0 z-50 flex items-center justify-center"
        style="background: rgba(0,0,0,0.5);" @click.self="closeModal">
        <div class="glass-card w-full max-w-md mx-4 p-6 flex flex-col gap-4">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold" style="color: var(--text-primary);">
              {{ editing ? 'Edit Segment' : 'New Segment' }}
            </h3>
            <button @click="closeModal" class="btn-glass-icon w-8 h-8 rounded-xl text-xs">
              <i class="fas fa-xmark"></i>
            </button>
          </div>
          <div class="flex flex-col gap-3">
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Name <span class="text-red-400">*</span></label>
              <input v-model="form.name" type="text" class="glass-input" placeholder="VIP Customers" required />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Description</label>
              <input v-model="form.description" type="text" class="glass-input" placeholder="Segment description" />
            </div>
            <div class="grid grid-cols-2 gap-3">
              <div class="flex flex-col gap-1.5">
                <label class="form-label">Color</label>
                <div class="flex items-center gap-2">
                  <input v-model="form.color" type="color" class="w-10 h-9 rounded-lg cursor-pointer border-0 p-0.5"
                    style="background: var(--glass-bg);" />
                  <input v-model="form.color" type="text" class="glass-input flex-1 text-sm font-mono" placeholder="#7c5ef0" />
                </div>
              </div>
              <div class="flex flex-col gap-1.5">
                <label class="form-label">Icon class</label>
                <input v-model="form.icon" type="text" class="glass-input text-sm" placeholder="fas fa-crown" />
              </div>
            </div>
          </div>
          <div class="flex gap-2 pt-2">
            <button @click="saveSegment" class="btn-accent flex-1 text-sm" :disabled="saving">
              <i class="fas fa-spinner fa-spin text-xs mr-1" v-if="saving"></i>
              {{ saving ? 'Saving…' : (editing ? 'Update' : 'Create') }}
            </button>
            <button @click="closeModal" class="btn-glass flex-1 text-sm">Cancel</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useSegmentStore } from '@/stores/segmentStore';
import { segmentService } from '@/services/segmentService';
import { formatCurrency } from '@/utils/formatters';
import type { Segment } from '@/types/segment';

const store = useSegmentStore();
const { items, loading, error } = storeToRefs(store);

const showCreate = ref(false);
const editing    = ref<Segment | null>(null);
const saving     = ref(false);

const form = ref({ name: '', description: '', color: '#7c5ef0', icon: 'fas fa-users' });

onMounted(() => store.fetchAll());

const totalCustomers = computed(() => items.value.reduce((s, g) => s + (g.customerCount ?? 0), 0));
const totalRevenue   = computed(() => items.value.reduce((s, g) => s + (g.totalRevenue ?? 0), 0));
const systemCount    = computed(() => items.value.filter(g => g.isSystem).length);

function editSegment(seg: Segment) {
  editing.value    = seg;
  form.value       = { name: seg.name, description: seg.description ?? '', color: seg.color ?? '#7c5ef0', icon: seg.icon ?? 'fas fa-users' };
}

function closeModal() {
  showCreate.value = false;
  editing.value    = null;
  form.value       = { name: '', description: '', color: '#7c5ef0', icon: 'fas fa-users' };
}

async function saveSegment() {
  if (!form.value.name.trim()) return;
  saving.value = true;
  try {
    if (editing.value) {
      const updated = await segmentService.updateSegment(editing.value.id, form.value);
      store.updateItem(editing.value.id, updated);
    } else {
      const created = await segmentService.createSegment(form.value);
      store.prependItem(created);
    }
    closeModal();
  } catch (e: any) {
    alert(e?.message ?? 'Save failed');
  } finally {
    saving.value = false;
  }
}

async function deleteSegment(seg: Segment) {
  if (!confirm(`Delete segment "${seg.name}"?`)) return;
  try {
    await store.remove(seg.id);
  } catch (e: any) {
    alert(e?.message ?? 'Delete failed');
  }
}
</script>
