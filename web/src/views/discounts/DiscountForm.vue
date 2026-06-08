<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">{{ isEdit ? 'Edit Discount' : 'New Discount' }}</h1>
        <p class="page-subtitle">{{ isEdit ? 'Update coupon code details' : 'Create a new coupon code' }}</p>
      </div>
      <router-link to="/discounts" class="btn-glass text-sm">
        <i class="fas fa-arrow-left text-xs mr-1"></i>Back to Discounts
      </router-link>
    </div>

    <!-- Form -->
    <form @submit.prevent="submit" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Main fields -->
      <div class="lg:col-span-2 flex flex-col gap-6">
        <div class="glass-card p-6">
          <h2 class="text-[15px] font-semibold mb-5" style="color: var(--text-primary);">Code Details</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Coupon Code <span class="text-red-400">*</span></label>
              <input v-model="form.code" type="text" class="glass-input uppercase"
                placeholder="SUMMER20" required :disabled="isEdit" />
              <p class="text-xs" style="color: var(--text-muted);">Customers enter this at checkout</p>
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Description <span class="text-red-400">*</span></label>
              <input v-model="form.description" type="text" class="glass-input"
                placeholder="Summer 20% off" required />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Discount Type <span class="text-red-400">*</span></label>
              <select v-model="form.type" class="glass-input" required>
                <option value="PERCENTAGE">Percentage (%)</option>
                <option value="FIXED">Fixed Amount ($)</option>
              </select>
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">
                {{ form.type === 'PERCENTAGE' ? 'Percentage Off (%)' : 'Amount Off ($)' }}
                <span class="text-red-400">*</span>
              </label>
              <input v-model.number="form.value" type="number" step="0.01" min="0"
                :max="form.type === 'PERCENTAGE' ? 100 : undefined"
                class="glass-input" placeholder="20" required />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Minimum Order Amount ($)</label>
              <input v-model.number="form.minimumOrderAmount" type="number" step="0.01" min="0"
                class="glass-input" placeholder="0" />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Usage Limit <span class="text-xs" style="color: var(--text-muted);">(blank = unlimited)</span></label>
              <input v-model.number="form.usageLimit" type="number" min="1" step="1"
                class="glass-input" placeholder="Unlimited" />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Start Date</label>
              <input v-model="form.startDate" type="date" class="glass-input" />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">End Date</label>
              <input v-model="form.endDate" type="date" class="glass-input" />
            </div>
          </div>
        </div>
      </div>

      <!-- Sidebar options -->
      <div class="flex flex-col gap-6">
        <div class="glass-card p-6">
          <h2 class="text-[15px] font-semibold mb-5" style="color: var(--text-primary);">Status</h2>
          <label class="flex items-center gap-3 cursor-pointer">
            <div class="relative">
              <input v-model="form.isActive" type="checkbox" class="sr-only" />
              <div class="w-10 h-6 rounded-full transition-colors"
                :class="form.isActive ? 'bg-green-500' : 'bg-gray-400'"
                style="background: var(--glass-bg)">
                <div class="w-10 h-6 rounded-full transition-colors flex items-center px-1"
                  :class="form.isActive ? 'bg-green-500 justify-end' : 'bg-gray-400/50 justify-start'">
                  <div class="w-4 h-4 bg-white rounded-full shadow"></div>
                </div>
              </div>
            </div>
            <span class="text-sm font-medium" style="color: var(--text-primary);">
              {{ form.isActive ? 'Active' : 'Inactive' }}
            </span>
          </label>
          <p class="text-xs mt-2" style="color: var(--text-muted);">
            {{ form.isActive ? 'Customers can use this code' : 'Code is disabled and cannot be used' }}
          </p>
        </div>

        <!-- Preview -->
        <div class="glass-card p-6">
          <h2 class="text-[15px] font-semibold mb-4" style="color: var(--text-primary);">Preview</h2>
          <div class="flex flex-col gap-2">
            <div class="flex items-center gap-2">
              <span class="font-mono text-sm font-bold px-2 py-1 rounded"
                style="background: rgba(var(--accent-rgb),0.12); color: var(--accent);">
                {{ form.code || 'CODE' }}
              </span>
              <span class="badge" :class="form.isActive ? 'badge-success' : 'badge-danger'">
                {{ form.isActive ? 'Active' : 'Inactive' }}
              </span>
            </div>
            <p class="text-sm" style="color: var(--text-secondary);">
              {{ form.type === 'PERCENTAGE'
                  ? form.value + '% off'
                  : formatCurrency(form.value || 0) + ' off' }}
              {{ form.minimumOrderAmount ? ' orders over ' + formatCurrency(form.minimumOrderAmount) : '' }}
            </p>
            <p v-if="form.endDate" class="text-xs" style="color: var(--text-muted);">
              Expires {{ form.endDate }}
            </p>
          </div>
        </div>

        <!-- Save actions -->
        <div class="glass-card p-5 flex flex-col gap-2">
          <button type="submit" class="btn-accent w-full" :disabled="saving">
            <i class="fas fa-spinner fa-spin text-xs mr-1" v-if="saving"></i>
            <i class="fas fa-floppy-disk text-xs mr-1" v-else></i>
            {{ saving ? 'Saving…' : (isEdit ? 'Update Discount' : 'Create Discount') }}
          </button>
          <router-link to="/discounts" class="btn-glass w-full text-center text-sm">Cancel</router-link>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useDiscountStore } from '@/stores/discountStore';
import { discountService } from '@/services/discountService';
import { useToast } from '@/composables/useToast';
import { formatCurrency } from '@/utils/formatters';

const route  = useRoute();
const router = useRouter();
const store  = useDiscountStore();
const toast  = useToast();

const isEdit = computed(() => !!route.params.id);
const saving = ref(false);

const form = ref({
  code:               '',
  description:        '',
  type:               'PERCENTAGE' as 'PERCENTAGE' | 'FIXED',
  value:              0,
  minimumOrderAmount: 0,
  usageLimit:         null as number | null,
  startDate:          '',
  endDate:            '',
  isActive:           true,
});

onMounted(async () => {
  if (!isEdit.value) return;
  await store.fetchAll();
  const id   = Number(route.params.id);
  const disc = store.items.find(d => d.id === id);
  if (!disc) return;
  form.value = {
    code:               disc.code,
    description:        disc.description,
    type:               disc.type,
    value:              disc.value,
    minimumOrderAmount: disc.minimumOrderAmount ?? 0,
    usageLimit:         disc.usageLimit ?? null,
    startDate:          disc.startDate?.split('T')[0] ?? '',
    endDate:            disc.endDate?.split('T')[0] ?? '',
    isActive:           disc.isActive,
  };
});

async function submit() {
  saving.value = true;
  try {
    const payload = {
      ...form.value,
      code:       form.value.code.toUpperCase(),
      usageLimit: form.value.usageLimit || null,
    };
    if (isEdit.value) {
      const id      = Number(route.params.id);
      const updated = await discountService.updateDiscount(id, payload);
      store.updateItem(id, updated);
    } else {
      const created = await discountService.createDiscount(payload);
      store.prependItem(created);
    }
    toast.success(isEdit.value ? 'Discount updated' : 'Discount created');
    router.push('/discounts');
  } catch (e: any) {
    toast.error(e?.message ?? 'Save failed', 'Error');
  } finally {
    saving.value = false;
  }
}
</script>
