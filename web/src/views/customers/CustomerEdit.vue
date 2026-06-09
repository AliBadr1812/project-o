<template>
  <div class="flex flex-col gap-6">

    <!-- ── Page header ──────────────────────────────────────────────── -->
    <div class="page-header">
      <div>
        <div class="flex items-center gap-2 mb-1">
          <router-link to="/customers" class="text-sm hover:opacity-70 transition-opacity" style="color: var(--text-muted);">
            Customers
          </router-link>
          <i class="fas fa-chevron-right text-[10px]" style="color: var(--text-muted);"></i>
          <span class="text-sm" style="color: var(--text-secondary);">{{ customer?.name }}</span>
        </div>
        <h1 class="page-title">Edit Customer</h1>
        <p class="page-subtitle">Update personal information and account settings</p>
      </div>
      <div class="flex items-center gap-2">
        <router-link to="/customers" class="btn-glass">Cancel</router-link>
        <button @click="handleSave" :disabled="saving" class="btn-accent">
          <i v-if="saving" class="fas fa-spinner fa-spin text-xs mr-1.5"></i>
          <i v-else class="fas fa-check text-xs mr-1.5"></i>
          {{ saving ? 'Saving…' : 'Save Changes' }}
        </button>
      </div>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="grid grid-cols-1 xl:grid-cols-[1fr_280px] gap-6">
      <div class="flex flex-col gap-6">
        <div v-for="i in 3" :key="i" class="glass-card h-48 animate-pulse"></div>
      </div>
      <div class="flex flex-col gap-6">
        <div v-for="i in 3" :key="i" class="glass-card h-36 animate-pulse"></div>
      </div>
    </div>

    <!-- Form -->
    <div v-else-if="customer" class="grid grid-cols-1 xl:grid-cols-[1fr_280px] gap-6">

      <!-- ── Left column ──────────────────────────────────────────── -->
      <div class="flex flex-col gap-6">

        <!-- Personal Information -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-p" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-user"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Personal Information</h2>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-4">
            <div class="flex items-center gap-5 pb-4" style="border-bottom: 1px solid var(--glass-border);">
              <div class="w-16 h-16 rounded-2xl flex items-center justify-center text-xl font-bold flex-shrink-0"
                :style="`background: linear-gradient(135deg, ${stringToColor(customer.name)}, ${stringToColor(customer.name + '2')}); color: #fff; box-shadow: 0 4px 16px ${stringToColor(customer.name)}44;`">
                {{ getInitials(customer.name) }}
              </div>
              <div>
                <p class="text-[15px] font-bold" style="color: var(--text-primary);">{{ form.name || customer.name }}</p>
                <p class="text-[12px]" style="color: var(--text-muted);">Customer since {{ formatDate(customer.joinedAt) }}</p>
              </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="ef-label">Full Name <span style="color:#dc2626">*</span></label>
                <input v-model="form.name" type="text" required class="glass-input w-full" placeholder="Full name" />
              </div>
              <div>
                <label class="ef-label">Phone Number</label>
                <input v-model="form.phone" type="tel" class="glass-input w-full" placeholder="+1 (555) 000-0000" />
              </div>
            </div>
            <div>
              <label class="ef-label">Email Address <span style="color:#dc2626">*</span></label>
              <input v-model="form.email" type="email" required class="glass-input w-full" placeholder="customer@example.com" />
            </div>
          </div>
        </Card>

        <!-- Shipping Address -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-g" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-location-dot"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Default Address</h2>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-4">
            <div>
              <label class="ef-label">Street Address</label>
              <input v-model="form.address.street" type="text" class="glass-input w-full" placeholder="123 Main St" />
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="ef-label">City</label>
                <input v-model="form.address.city" type="text" class="glass-input w-full" placeholder="City" />
              </div>
              <div>
                <label class="ef-label">State</label>
                <input v-model="form.address.state" type="text" class="glass-input w-full" placeholder="State" />
              </div>
              <div>
                <label class="ef-label">ZIP</label>
                <input v-model="form.address.zip" type="text" class="glass-input w-full" placeholder="00000" />
              </div>
            </div>
            <div>
              <label class="ef-label">Country</label>
              <select v-model="form.address.country" class="glass-select w-full">
                <option value="US">United States</option>
                <option value="CA">Canada</option>
                <option value="GB">United Kingdom</option>
                <option value="AU">Australia</option>
                <option value="DE">Germany</option>
                <option value="FR">France</option>
                <option value="NL">Netherlands</option>
                <option value="other">Other</option>
              </select>
            </div>
          </div>
        </Card>

        <!-- Internal Notes -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-o" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-note-sticky"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Internal Notes</h2>
            </div>
          </div>
          <div class="p-5">
            <textarea
              v-model="form.notes"
              rows="4"
              class="glass-input w-full resize-none"
              placeholder="Private notes about this customer (not visible to customer)…"
            ></textarea>
          </div>
        </Card>

      </div>

      <!-- ── Right column ─────────────────────────────────────────── -->
      <div class="flex flex-col gap-6">

        <!-- Account Type -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-b" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-tag"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Account Type</h2>
            </div>
          </div>
          <div class="p-4 flex flex-col gap-2">
            <label
              v-for="t in typeOptions"
              :key="t.value"
              class="flex items-center gap-3 p-3 rounded-xl cursor-pointer transition-all duration-150"
              :style="form.type === t.value
                ? 'background: rgba(124,94,240,0.10); border: 1px solid rgba(124,94,240,0.25);'
                : 'background: transparent; border: 1px solid transparent;'"
            >
              <input type="radio" v-model="form.type" :value="t.value" style="accent-color: var(--accent);">
              <div class="flex items-center gap-2.5">
                <i :class="t.icon" class="w-4 text-center" :style="`color: ${t.color};`"></i>
                <div>
                  <p class="text-[13px] font-medium" style="color: var(--text-primary);">{{ t.label }}</p>
                  <p class="text-[11px]" style="color: var(--text-muted);">{{ t.description }}</p>
                </div>
              </div>
            </label>
          </div>
        </Card>

        <!-- Account Status -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Status</h2>
          </div>
          <div class="p-4 flex flex-col gap-2">
            <label
              v-for="s in statusOptions"
              :key="s.value"
              class="flex items-center gap-3 p-3 rounded-xl cursor-pointer transition-all duration-150"
              :style="form.status === s.value
                ? 'background: rgba(124,94,240,0.10); border: 1px solid rgba(124,94,240,0.25);'
                : 'background: transparent; border: 1px solid transparent;'"
            >
              <input type="radio" v-model="form.status" :value="s.value" style="accent-color: var(--accent);">
              <div>
                <p class="text-[13px] font-medium" style="color: var(--text-primary);">{{ s.label }}</p>
                <p class="text-[11px]" style="color: var(--text-muted);">{{ s.description }}</p>
              </div>
            </label>
          </div>
        </Card>

        <!-- Account Stats (read-only) -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Account Stats</h2>
          </div>
          <div class="p-4 flex flex-col gap-2 text-[13px]">
            <div class="flex justify-between py-2" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Total Orders</span>
              <span class="font-semibold" style="color: var(--text-primary);">{{ customer.orderCount }}</span>
            </div>
            <div class="flex justify-between py-2" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Total Spent</span>
              <span class="font-semibold" style="color: var(--text-accent);">{{ formatCurrency(customer.totalSpent) }}</span>
            </div>
            <div class="flex justify-between py-2">
              <span style="color: var(--text-secondary);">Last Order</span>
              <span style="color: var(--text-primary);">{{ formatDate(customer.lastOrderDate) }}</span>
            </div>
          </div>
        </Card>

        <!-- Danger Zone -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[14px] font-semibold" style="color: #ef4444;">Danger Zone</h2>
          </div>
          <div class="p-4 flex flex-col gap-2">
            <button
              @click="handleDeactivate"
              class="btn-glass text-sm w-full justify-center"
              style="border-color: rgba(234,179,8,0.3); color: #b45309;"
            >
              <i class="fas fa-eye-slash text-xs mr-1.5"></i>Deactivate Account
            </button>
            <button
              @click="handleDelete"
              class="btn-glass text-sm w-full justify-center"
              style="border-color: rgba(239,68,68,0.3); color: #dc2626;"
            >
              <i class="fas fa-trash text-xs mr-1.5"></i>Delete Customer
            </button>
          </div>
        </Card>

      </div>
    </div>

    <!-- Not found -->
    <div v-else class="glass-card p-12 text-center">
      <i class="fas fa-user-slash text-4xl mb-4" style="color: var(--text-muted);"></i>
      <p class="text-lg font-semibold mb-2" style="color: var(--text-primary);">Customer not found</p>
      <p class="text-sm mb-6" style="color: var(--text-muted);">The customer you're looking for doesn't exist.</p>
      <router-link to="/customers" class="btn-accent">Back to Customers</router-link>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter, onBeforeRouteLeave } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import { customerService } from '@/services/customerService';
import { useCustomerStore } from '@/stores/customerStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';

const route  = useRoute();
const router = useRouter();
const store  = useCustomerStore();
const toast  = useToast();
const { confirm } = useConfirm();

const loading = ref(true);
const saving  = ref(false);

const isDirty   = ref(false);
const formSaved = ref(false);
let watchPaused = true;
setTimeout(() => { watchPaused = false; }, 500);

onBeforeRouteLeave(async () => {
  if (isDirty.value && !formSaved.value) {
    const ok = await confirm({
      title:       'Unsaved changes',
      message:     'You have unsaved changes.',
      detail:      'Leave without saving?',
      confirmText: 'Leave',
      variant:     'danger',
    });
    if (!ok) return false;
  }
});

interface Customer {
  id:            number;
  name:          string;
  email:         string;
  phone:         string;
  orderCount:    number;
  totalSpent:    number;
  lastOrderDate: string;
  joinedAt:      string;
  status:        'active' | 'inactive';
  type:          'new' | 'returning' | 'vip';
  address?: { street: string; city: string; state: string; zip: string; country: string; };
  notes?: string;
}

const customer = ref<Customer | null>(null);

const form = reactive({
  name:   '',
  email:  '',
  phone:  '',
  status: 'active'  as 'active' | 'inactive',
  type:   'new'     as 'new' | 'returning' | 'vip',
  notes:  '',
  address: { street: '', city: '', state: '', zip: '', country: 'US' },
});

watch(form, () => { if (!watchPaused) isDirty.value = true; }, { deep: true });

// ── Options ───────────────────────────────────────────────────────────
const typeOptions = [
  { value: 'new',       label: 'New',       description: 'First-time customer',            icon: 'fas fa-star',    color: '#3b82f6' },
  { value: 'returning', label: 'Returning', description: 'Has ordered more than once',      icon: 'fas fa-repeat',  color: '#8b5cf6' },
  { value: 'vip',       label: 'VIP',       description: 'High-value loyal customer',       icon: 'fas fa-crown',   color: '#f59e0b' },
];

const statusOptions = [
  { value: 'active',   label: 'Active',   description: 'Can log in and place orders' },
  { value: 'inactive', label: 'Inactive', description: 'Account is suspended' },
];

// ── Helpers ───────────────────────────────────────────────────────────
function stringToColor(str: string) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return `hsl(${hash % 360}, 70%, 50%)`;
}

// ── Lifecycle ─────────────────────────────────────────────────────────
onMounted(async () => {
  const id = Number(route.params.id);
  await store.fetchAll();
  const found = store.items.find(c => c.id === id);
  if (found) {
    customer.value = {
      id:            found.id,
      name:          found.fullName,
      email:         found.email,
      phone:         found.phone ?? '',
      orderCount:    found.orderCount,
      totalSpent:    found.totalSpent,
      lastOrderDate: found.lastOrderDate ?? '',
      joinedAt:      found.lastOrderDate ?? new Date().toISOString(),
      status:        found.status as 'active' | 'inactive',
      type:          found.type as 'new' | 'returning' | 'vip',
    };
    form.name   = found.fullName;
    form.email  = found.email;
    form.phone  = found.phone ?? '';
    form.status = found.status as 'active' | 'inactive';
    form.type   = found.type as 'new' | 'returning' | 'vip';
  }
  loading.value = false;
});

// ── Methods ───────────────────────────────────────────────────────────
async function handleSave() {
  if (!form.name.trim() || !form.email.trim()) return;
  saving.value = true;
  try {
    const id = customer.value!.id;
    const updated = await customerService.updateCustomer(id, {
      fullName: form.name,
      email:    form.email,
      phone:    form.phone,
      status:   form.status,
      type:     form.type,
    });
    store.updateItem(id, updated);
    formSaved.value = true;
    toast.success('Customer saved', 'Saved');
    router.push('/customers');
  } catch (e: unknown) {
    toast.error(e instanceof Error ? e.message : 'Save failed', 'Error');
  } finally {
    saving.value = false;
  }
}

async function handleDeactivate() {
  if (!customer.value) return;
  const ok = await confirm({
    title:       'Deactivate account',
    message:     `Deactivate ${customer.value.name}?`,
    detail:      'They will lose access to their account immediately.',
    confirmText: 'Deactivate',
    variant:     'warning',
  });
  if (ok) {
    form.status = 'inactive';
    toast.info('Account deactivated. Save to apply.', 'Deactivated');
  }
}

async function handleDelete() {
  if (!customer.value) return;
  const ok = await confirm({
    title:       'Delete customer',
    message:     `Permanently delete ${customer.value.name}?`,
    detail:      'All their data will be removed. This cannot be undone.',
    confirmText: 'Delete',
    variant:     'danger',
  });
  if (!ok) return;
  try {
    await store.remove(customer.value.id);
    toast.success('Customer deleted');
    router.push('/customers');
  } catch (e: unknown) {
    toast.error(e instanceof Error ? e.message : 'Delete failed', 'Error');
  }
}
</script>

<style scoped>
.ef-label {
  display: block;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary);
  margin-bottom: 6px;
}
</style>
