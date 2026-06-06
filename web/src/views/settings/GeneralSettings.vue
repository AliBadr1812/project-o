<template>
  <div class="max-w-5xl mx-auto">

    <!-- Page header -->
    <div class="mb-7">
      <h1 class="page-title">Settings</h1>
      <p class="page-subtitle">Manage your store preferences and account</p>
    </div>

    <!-- Tab bar — iOS segmented style -->
    <div
      class="flex gap-1 p-1 rounded-2xl mb-7"
      style="background: rgba(255,255,255,0.30); border: 1px solid var(--glass-border); width: fit-content;"
    >
      <button
        v-for="tab in tabs"
        :key="tab.id"
        @click="activeTab = tab.id"
        class="flex items-center gap-2 px-4 py-2 rounded-xl text-[13px] font-medium transition-all duration-200"
        :style="activeTab === tab.id
          ? 'background: rgba(255,255,255,0.75); color: var(--accent); box-shadow: 0 1px 6px rgba(100,80,160,0.15), inset 0 1px 0 rgba(255,255,255,0.9);'
          : 'color: var(--text-secondary);'"
      >
        <i :class="tab.icon" class="text-xs"></i>
        {{ tab.label }}
      </button>
    </div>

    <!-- ── STORE ─────────────────────────────────────────────────────── -->
    <div v-if="activeTab === 'store'" class="flex flex-col gap-5">

      <!-- Store identity -->
      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#b97fff,#7c5ef0); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-store"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Store Identity</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row">
            <label class="settings-label">Store Name</label>
            <input v-model="store.name" type="text" class="glass-input settings-input" placeholder="My Awesome Store">
          </div>
          <div class="settings-row">
            <label class="settings-label">Store Email</label>
            <input v-model="store.email" type="email" class="glass-input settings-input" placeholder="store@example.com">
          </div>
          <div class="settings-row">
            <label class="settings-label">Phone</label>
            <input v-model="store.phone" type="tel" class="glass-input settings-input" placeholder="+1 (555) 000-0000">
          </div>
          <div class="settings-row border-none">
            <label class="settings-label">Website</label>
            <input v-model="store.website" type="url" class="glass-input settings-input" placeholder="https://mystore.com">
          </div>
        </div>
      </div>

      <!-- Address -->
      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#34c759,#30a84b); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-location-dot"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Address</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row">
            <label class="settings-label">Country</label>
            <select v-model="store.country" class="glass-select settings-input">
              <option value="US">United States</option>
              <option value="NL">Netherlands</option>
              <option value="GB">United Kingdom</option>
              <option value="DE">Germany</option>
            </select>
          </div>
          <div class="settings-row">
            <label class="settings-label">City</label>
            <input v-model="store.city" type="text" class="glass-input settings-input" placeholder="New York">
          </div>
          <div class="settings-row border-none">
            <label class="settings-label">Currency</label>
            <select v-model="store.currency" class="glass-select settings-input">
              <option value="USD">USD — US Dollar</option>
              <option value="EUR">EUR — Euro</option>
              <option value="GBP">GBP — British Pound</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Logo upload -->
      <div class="glass-card p-5">
        <div class="flex items-center gap-2 mb-4">
          <div class="stat-icon" style="background: linear-gradient(135deg,#ff9f0a,#e08800); width:30px; height:30px; font-size:13px;">
            <i class="fas fa-image"></i>
          </div>
          <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Store Logo</h2>
        </div>
        <div class="flex items-center gap-5">
          <div
            class="w-20 h-20 rounded-2xl flex items-center justify-center flex-shrink-0"
            style="background: linear-gradient(145deg,#b97fff,#7c5ef0); box-shadow: 0 4px 16px rgba(124,94,240,0.35);"
          >
            <i class="fas fa-store text-2xl" style="color:rgba(255,255,255,0.9)"></i>
          </div>
          <div>
            <p class="text-[13px] font-medium mb-2" style="color:var(--text-primary)">Upload your store logo</p>
            <p class="text-[12px] mb-3" style="color:var(--text-muted)">PNG, JPG up to 2MB. Recommended 512×512px.</p>
            <label class="btn-glass cursor-pointer text-[13px] px-4 py-2">
              <i class="fas fa-arrow-up-from-bracket mr-2 text-xs"></i>Choose File
              <input type="file" accept="image/*" class="hidden">
            </label>
          </div>
        </div>
      </div>

      <div class="flex justify-end">
        <button @click="saveStore" class="btn-accent">
          <i class="fas fa-check mr-2 text-xs"></i>Save Changes
        </button>
      </div>
    </div>

    <!-- ── NOTIFICATIONS ──────────────────────────────────────────────── -->
    <div v-if="activeTab === 'notifications'" class="flex flex-col gap-5">

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#ff6b6b,#ee5a24); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-bell"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Order Notifications</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row-toggle" v-for="n in orderNotifications" :key="n.key">
            <div>
              <p class="text-[13px] font-medium" style="color:var(--text-primary)">{{ n.label }}</p>
              <p class="text-[12px]" style="color:var(--text-muted)">{{ n.description }}</p>
            </div>
            <button
              @click="n.enabled = !n.enabled"
              class="toggle-switch flex-shrink-0"
              :class="n.enabled ? 'toggle-on' : 'toggle-off'"
            ></button>
          </div>
        </div>
      </div>

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#5ac8fa,#007aff); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-envelope"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Email Preferences</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row-toggle" v-for="n in emailPreferences" :key="n.key">
            <div>
              <p class="text-[13px] font-medium" style="color:var(--text-primary)">{{ n.label }}</p>
              <p class="text-[12px]" style="color:var(--text-muted)">{{ n.description }}</p>
            </div>
            <button
              @click="n.enabled = !n.enabled"
              class="toggle-switch flex-shrink-0"
              :class="n.enabled ? 'toggle-on' : 'toggle-off'"
            ></button>
          </div>
        </div>
      </div>
    </div>

    <!-- ── SECURITY ───────────────────────────────────────────────────── -->
    <div v-if="activeTab === 'security'" class="flex flex-col gap-5">

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#ff9f0a,#e08800); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-lock"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Change Password</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row">
            <label class="settings-label">Current Password</label>
            <input type="password" class="glass-input settings-input" placeholder="••••••••">
          </div>
          <div class="settings-row">
            <label class="settings-label">New Password</label>
            <input type="password" class="glass-input settings-input" placeholder="••••••••">
          </div>
          <div class="settings-row border-none">
            <label class="settings-label">Confirm Password</label>
            <input type="password" class="glass-input settings-input" placeholder="••••••••">
          </div>
        </div>
      </div>

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#5ac8fa,#007aff); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-shield-halved"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Two-Factor Authentication</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row-toggle">
            <div>
              <p class="text-[13px] font-medium" style="color:var(--text-primary)">Enable 2FA</p>
              <p class="text-[12px]" style="color:var(--text-muted)">Add an extra layer of security to your account</p>
            </div>
            <button @click="security.twoFactor = !security.twoFactor" class="toggle-switch flex-shrink-0" :class="security.twoFactor ? 'toggle-on' : 'toggle-off'"></button>
          </div>
          <div class="settings-row-toggle border-none">
            <div>
              <p class="text-[13px] font-medium" style="color:var(--text-primary)">Login Alerts</p>
              <p class="text-[12px]" style="color:var(--text-muted)">Get notified of new sign-ins to your account</p>
            </div>
            <button @click="security.loginAlerts = !security.loginAlerts" class="toggle-switch flex-shrink-0" :class="security.loginAlerts ? 'toggle-on' : 'toggle-off'"></button>
          </div>
        </div>
      </div>

      <div class="flex justify-end">
        <button class="btn-accent">
          <i class="fas fa-check mr-2 text-xs"></i>Update Password
        </button>
      </div>
    </div>

    <!-- ── APPEARANCE ─────────────────────────────────────────────────── -->
    <div v-if="activeTab === 'appearance'" class="flex flex-col gap-5">

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#c084fc,#9333ea); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-palette"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Theme</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row border-none">
            <label class="settings-label">Color Scheme</label>
            <div class="flex gap-2">
              <button
                v-for="theme in themes"
                :key="theme.id"
                @click="appearance.theme = theme.id"
                class="flex flex-col items-center gap-1.5 p-2.5 rounded-xl transition-all duration-150"
                :style="appearance.theme === theme.id
                  ? 'border: 2px solid var(--accent); background: rgba(124,94,240,0.08);'
                  : 'border: 1px solid var(--glass-border); background: transparent;'"
              >
                <div class="w-8 h-8 rounded-full" :style="`background: ${theme.color};`"></div>
                <span class="text-[11px]" style="color:var(--text-secondary)">{{ theme.label }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="glass-card p-0 overflow-hidden">
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2 mb-1">
            <div class="stat-icon" style="background: linear-gradient(135deg,#34c759,#30a84b); width:30px; height:30px; font-size:13px;">
              <i class="fas fa-language"></i>
            </div>
            <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Regional</h2>
          </div>
        </div>
        <div class="settings-rows">
          <div class="settings-row">
            <label class="settings-label">Language</label>
            <select v-model="appearance.language" class="glass-select settings-input">
              <option value="en">English</option>
              <option value="nl">Dutch</option>
              <option value="de">German</option>
              <option value="fr">French</option>
            </select>
          </div>
          <div class="settings-row border-none">
            <label class="settings-label">Date Format</label>
            <select v-model="appearance.dateFormat" class="glass-select settings-input">
              <option value="MM/DD/YYYY">MM/DD/YYYY</option>
              <option value="DD/MM/YYYY">DD/MM/YYYY</option>
              <option value="YYYY-MM-DD">YYYY-MM-DD</option>
            </select>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';

const activeTab = ref('store');

const tabs = [
  { id: 'store',         label: 'Store',         icon: 'fas fa-store' },
  { id: 'notifications', label: 'Notifications',  icon: 'fas fa-bell' },
  { id: 'security',      label: 'Security',       icon: 'fas fa-lock' },
  { id: 'appearance',    label: 'Appearance',     icon: 'fas fa-palette' },
];

const store = reactive({
  name: 'ShopAdmin', email: 'admin@shopadmin.com', phone: '+1 (555) 000-0000',
  website: 'https://shopadmin.com', country: 'US', city: 'New York', currency: 'USD',
});

const orderNotifications = ref([
  { key: 'new_order',   label: 'New Order',          description: 'Get notified when a new order is placed',          enabled: true  },
  { key: 'shipped',     label: 'Order Shipped',       description: 'Notify when an order has been shipped',             enabled: true  },
  { key: 'cancelled',   label: 'Order Cancelled',     description: 'Notify when a customer cancels their order',        enabled: false },
  { key: 'low_stock',   label: 'Low Stock Alert',     description: 'Warn when a product falls below its stock threshold', enabled: true  },
]);

const emailPreferences = ref([
  { key: 'weekly',      label: 'Weekly Summary',      description: 'Receive a weekly report of your store performance', enabled: true  },
  { key: 'marketing',   label: 'Product Updates',     description: 'News and updates from ShopAdmin',                  enabled: false },
]);

const security = reactive({ twoFactor: false, loginAlerts: true });

const themes = [
  { id: 'purple', label: 'Purple',  color: 'linear-gradient(135deg,#b97fff,#7c5ef0)' },
  { id: 'blue',   label: 'Blue',    color: 'linear-gradient(135deg,#5ac8fa,#007aff)' },
  { id: 'green',  label: 'Green',   color: 'linear-gradient(135deg,#34c759,#30a84b)' },
  { id: 'orange', label: 'Orange',  color: 'linear-gradient(135deg,#ff9f0a,#e08800)' },
];

const appearance = reactive({ theme: 'purple', language: 'en', dateFormat: 'MM/DD/YYYY' });

const saveStore = () => console.log('Store saved:', store);
</script>

<style scoped>
/* Settings row layout */
.settings-rows {
  display: flex;
  flex-direction: column;
}

.settings-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  border-bottom: 1px solid var(--glass-border);
  gap: 16px;
}

.settings-row-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid var(--glass-border);
  gap: 20px;
}

.settings-row-toggle:last-child,
.settings-row.border-none {
  border-bottom: none;
}

.settings-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-primary);
  flex-shrink: 0;
  min-width: 130px;
}

.settings-input {
  max-width: 280px;
  width: 100%;
  font-size: 13px;
  padding: 7px 12px;
}

/* iOS toggle switch */
.toggle-switch {
  width: 51px;
  height: 31px;
  border-radius: 999px;
  position: relative;
  transition: background 0.25s ease;
  border: none;
  cursor: pointer;
  flex-shrink: 0;
}

.toggle-switch::after {
  content: '';
  position: absolute;
  top: 2px;
  width: 27px;
  height: 27px;
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.25);
  transition: left 0.25s ease;
}

.toggle-off {
  background: rgba(120,120,128,0.28);
}

.toggle-off::after {
  left: 2px;
}

.toggle-on {
  background: var(--accent);
  box-shadow: 0 0 12px rgba(124,94,240,0.4);
}

.toggle-on::after {
  left: 22px;
}
</style>
