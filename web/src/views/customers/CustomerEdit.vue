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
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';

const route  = useRoute();
const router = useRouter();

const loading = ref(true);
const saving  = ref(false);

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

// ── Mock data (same list as CustomerList) ─────────────────────────────
const mockCustomers: Customer[] = [
  { id: 1,  name: 'John Smith',      email: 'john.smith@example.com',  phone: '+1 (555) 123-4567', orderCount: 24, totalSpent: 2899.99, lastOrderDate: '2024-01-15T14:30:00Z', joinedAt: '2023-03-01T00:00:00Z', status: 'active',   type: 'vip',       address: { street: '123 Oak St',    city: 'New York',    state: 'NY', zip: '10001', country: 'US' } },
  { id: 2,  name: 'Emma Johnson',    email: 'emma.j@example.com',      phone: '+1 (555) 234-5678', orderCount: 12, totalSpent: 1499.50, lastOrderDate: '2024-01-14T11:20:00Z', joinedAt: '2023-05-12T00:00:00Z', status: 'active',   type: 'returning', address: { street: '456 Maple Ave',  city: 'Boston',      state: 'MA', zip: '02101', country: 'US' } },
  { id: 3,  name: 'Robert Brown',    email: 'robert.b@example.com',    phone: '+1 (555) 345-6789', orderCount: 8,  totalSpent: 899.99,  lastOrderDate: '2024-01-12T09:15:00Z', joinedAt: '2023-09-20T00:00:00Z', status: 'active',   type: 'new',       address: { street: '789 Pine Rd',    city: 'Chicago',     state: 'IL', zip: '60601', country: 'US' } },
  { id: 4,  name: 'Sarah Davis',     email: 'sarah.d@example.com',     phone: '+1 (555) 456-7890', orderCount: 32, totalSpent: 3899.99, lastOrderDate: '2024-01-10T16:45:00Z', joinedAt: '2023-01-05T00:00:00Z', status: 'inactive', type: 'vip',       address: { street: '321 Elm St',     city: 'Seattle',     state: 'WA', zip: '98101', country: 'US' } },
  { id: 5,  name: 'Michael Wilson',  email: 'michael.w@example.com',   phone: '+1 (555) 567-8901', orderCount: 5,  totalSpent: 599.99,  lastOrderDate: '2024-01-08T11:30:00Z', joinedAt: '2023-11-14T00:00:00Z', status: 'active',   type: 'new',       address: { street: '654 Cedar Blvd', city: 'Austin',      state: 'TX', zip: '73301', country: 'US' } },
  { id: 6,  name: 'Jessica Miller',  email: 'jessica.m@example.com',   phone: '+1 (555) 678-9012', orderCount: 18, totalSpent: 2299.50, lastOrderDate: '2024-01-07T13:20:00Z', joinedAt: '2023-04-08T00:00:00Z', status: 'active',   type: 'returning', address: { street: '987 Birch Ln',   city: 'Denver',      state: 'CO', zip: '80201', country: 'US' } },
  { id: 7,  name: 'David Taylor',    email: 'david.t@example.com',     phone: '+1 (555) 789-0123', orderCount: 3,  totalSpent: 399.99,  lastOrderDate: '2024-01-05T15:10:00Z', joinedAt: '2023-12-01T00:00:00Z', status: 'active',   type: 'new',       address: { street: '147 Spruce Way', city: 'Portland',    state: 'OR', zip: '97201', country: 'US' } },
  { id: 8,  name: 'Lisa Anderson',   email: 'lisa.a@example.com',      phone: '+1 (555) 890-1234', orderCount: 15, totalSpent: 1799.99, lastOrderDate: '2024-01-04T10:45:00Z', joinedAt: '2023-06-18T00:00:00Z', status: 'inactive', type: 'returning', address: { street: '258 Walnut St',  city: 'Phoenix',     state: 'AZ', zip: '85001', country: 'US' } },
  { id: 9,  name: 'James Thomas',    email: 'james.t@example.com',     phone: '+1 (555) 901-2345', orderCount: 27, totalSpent: 3299.99, lastOrderDate: '2024-01-03T14:30:00Z', joinedAt: '2023-02-22T00:00:00Z', status: 'active',   type: 'vip',       address: { street: '369 Willow Dr',  city: 'Miami',       state: 'FL', zip: '33101', country: 'US' } },
  { id: 10, name: 'Jennifer White',  email: 'jennifer.w@example.com',  phone: '+1 (555) 012-3456', orderCount: 9,  totalSpent: 1099.99, lastOrderDate: '2024-01-02T09:20:00Z', joinedAt: '2023-10-31T00:00:00Z', status: 'active',   type: 'new',       address: { street: '741 Aspen Ct',   city: 'Nashville',   state: 'TN', zip: '37201', country: 'US' } },
];

// ── Lifecycle ─────────────────────────────────────────────────────────
onMounted(async () => {
  const id = Number(route.params.id);
  await new Promise(r => setTimeout(r, 350));

  const found = mockCustomers.find(c => c.id === id);
  if (found) {
    customer.value = found;
    form.name   = found.name;
    form.email  = found.email;
    form.phone  = found.phone;
    form.status = found.status;
    form.type   = found.type;
    form.notes  = found.notes ?? '';
    if (found.address) Object.assign(form.address, found.address);
  }
  loading.value = false;
});

// ── Methods ───────────────────────────────────────────────────────────
async function handleSave() {
  if (!form.name.trim() || !form.email.trim()) return;
  saving.value = true;
  await new Promise(r => setTimeout(r, 700));
  console.log('Customer saved:', { id: customer.value?.id, ...form });
  saving.value = false;
  router.push('/customers');
}

function handleDeactivate() {
  if (!customer.value) return;
  if (confirm(`Deactivate ${customer.value.name}? They will lose access to their account.`)) {
    form.status = 'inactive';
    alert('Account deactivated. Save to apply.');
  }
}

function handleDelete() {
  if (!customer.value) return;
  if (confirm(`Permanently delete ${customer.value.name}? This cannot be undone.`)) {
    alert('Customer deleted.');
    router.push('/customers');
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
