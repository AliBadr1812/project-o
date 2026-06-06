<template>
  <div class="max-w-5xl mx-auto">

    <!-- ── Hero card ───────────────────────────────────────────────────── -->
    <div
      class="glass-card p-0 overflow-hidden mb-6"
      style="background: linear-gradient(135deg, rgba(185,127,255,0.18) 0%, rgba(124,94,240,0.10) 50%, rgba(90,200,250,0.08) 100%);"
    >
      <!-- Top band -->
      <div class="h-24 relative" style="background: linear-gradient(135deg,#b97fff,#7c5ef0,#5e5ce6); opacity: 0.85;"></div>

      <div class="px-6 pb-5">
        <!-- Avatar row -->
        <div class="flex items-end justify-between -mt-12 mb-4">
          <div class="relative">
            <div
              class="w-24 h-24 rounded-[22px] overflow-hidden flex-shrink-0"
              style="border: 3px solid rgba(255,255,255,0.85); box-shadow: 0 8px 24px rgba(124,94,240,0.35);"
            >
              <img v-if="userProfile.avatar" :src="userProfile.avatar" :alt="userProfile.name" class="w-full h-full object-cover">
              <div
                v-else
                class="w-full h-full flex items-center justify-center text-3xl font-bold text-white"
                style="background: linear-gradient(145deg,#b97fff,#7c5ef0);"
              >{{ getUserInitials(userProfile.name) }}</div>
            </div>
            <button
              @click="triggerAvatarUpload"
              class="absolute -bottom-1 -right-1 w-8 h-8 rounded-full flex items-center justify-center"
              style="background: var(--accent); color:#fff; box-shadow: 0 2px 8px rgba(124,94,240,0.5);"
              title="Change avatar"
            >
              <i class="fas fa-camera text-[11px]"></i>
            </button>
            <input ref="avatarInput" type="file" accept="image/*" class="hidden" @change="handleAvatarUpload">
          </div>

          <!-- Action buttons -->
          <div class="flex gap-2 mt-2">
            <button @click="editPersonalInfo" class="btn-glass text-[13px]">
              <i class="fas fa-pen text-xs mr-1.5"></i>Edit Profile
            </button>
            <button @click="showDeleteModal = true" class="btn-glass text-[13px]" style="color:#ef4444; border-color: rgba(239,68,68,0.3);">
              <i class="fas fa-trash-can text-xs mr-1.5"></i>Delete
            </button>
          </div>
        </div>

        <!-- Name + role -->
        <div class="mb-4">
          <h1 class="text-[22px] font-bold tracking-tight" style="color:var(--text-primary)">{{ userProfile.name }}</h1>
          <div class="flex items-center gap-2 mt-1">
            <span class="badge badge-info text-[11px]">{{ userProfile.role }}</span>
            <span class="text-[12px]" style="color:var(--text-muted)">{{ userProfile.email }}</span>
            <span class="w-1 h-1 rounded-full" style="background:var(--text-muted)"></span>
            <span class="text-[12px]" style="color:var(--text-muted)">
              <i class="fas fa-calendar-days mr-1 text-[10px]"></i>Since {{ formatDate(userProfile.createdAt) }}
            </span>
          </div>
        </div>

        <!-- Stats row -->
        <div class="grid grid-cols-3 gap-3">
          <div
            class="rounded-2xl p-3 text-center"
            style="background: rgba(255,255,255,0.40); border: 1px solid var(--glass-border);"
          >
            <div class="text-[22px] font-bold" style="color:var(--text-primary)">{{ userStats.orders }}</div>
            <div class="text-[11px]" style="color:var(--text-muted)">Orders managed</div>
          </div>
          <div
            class="rounded-2xl p-3 text-center"
            style="background: rgba(255,255,255,0.40); border: 1px solid var(--glass-border);"
          >
            <div class="text-[22px] font-bold" style="color:var(--text-primary)">{{ formatCurrency(userStats.revenue) }}</div>
            <div class="text-[11px]" style="color:var(--text-muted)">Revenue tracked</div>
          </div>
          <div
            class="rounded-2xl p-3 text-center"
            style="background: rgba(255,255,255,0.40); border: 1px solid var(--glass-border);"
          >
            <div class="text-[22px] font-bold" style="color:var(--text-primary)">{{ userStats.customers }}</div>
            <div class="text-[11px]" style="color:var(--text-muted)">Customers served</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ── Two-column body ─────────────────────────────────────────────── -->
    <div class="grid grid-cols-1 xl:grid-cols-[1fr_280px] gap-5">

      <!-- Left: main sections -->
      <div class="flex flex-col gap-5">

        <!-- Personal Information -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="flex items-center justify-between px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#b97fff,#7c5ef0); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-user"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Personal Information</h2>
            </div>
            <button v-if="!editingPersonalInfo" @click="editPersonalInfo" class="btn-glass-icon" style="width:32px;height:32px;" title="Edit">
              <i class="fas fa-pen text-xs"></i>
            </button>
          </div>

          <!-- View mode -->
          <div v-if="!editingPersonalInfo" class="settings-rows">
            <div class="settings-row">
              <span class="settings-label">Full Name</span>
              <span class="text-[13px] font-medium" style="color:var(--text-primary)">{{ userProfile.name }}</span>
            </div>
            <div class="settings-row">
              <span class="settings-label">Email</span>
              <span class="text-[13px]" style="color:var(--text-primary)">{{ userProfile.email }}</span>
            </div>
            <div class="settings-row">
              <span class="settings-label">Phone</span>
              <span class="text-[13px]" style="color:var(--text-primary)">{{ userProfile.phone || '—' }}</span>
            </div>
            <div class="settings-row">
              <span class="settings-label">Location</span>
              <span class="text-[13px]" style="color:var(--text-primary)">{{ userProfile.location || '—' }}</span>
            </div>
            <div class="settings-row border-none">
              <span class="settings-label">Bio</span>
              <span class="text-[13px]" style="color:var(--text-secondary); max-width:340px;">{{ userProfile.bio || 'No bio added yet.' }}</span>
            </div>
          </div>

          <!-- Edit mode -->
          <form v-else @submit.prevent="savePersonalInfo" class="px-5 pb-5 flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="up-label">Full Name *</label>
                <input v-model="personalInfoForm.name" type="text" required class="glass-input w-full">
              </div>
              <div>
                <label class="up-label">Email *</label>
                <input v-model="personalInfoForm.email" type="email" required class="glass-input w-full">
              </div>
              <div>
                <label class="up-label">Phone</label>
                <input v-model="personalInfoForm.phone" type="tel" class="glass-input w-full">
              </div>
              <div>
                <label class="up-label">Location</label>
                <input v-model="personalInfoForm.location" type="text" class="glass-input w-full">
              </div>
            </div>
            <div>
              <label class="up-label">Bio</label>
              <textarea v-model="personalInfoForm.bio" rows="3" class="glass-input w-full" placeholder="Tell us about yourself…"></textarea>
            </div>
            <div class="flex justify-end gap-2">
              <button type="button" @click="cancelEditPersonalInfo" class="btn-glass">Cancel</button>
              <button type="submit" :disabled="savingPersonalInfo" class="btn-accent">
                {{ savingPersonalInfo ? 'Saving…' : 'Save Changes' }}
              </button>
            </div>
          </form>
        </div>

        <!-- Security -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ff9f0a,#e08800); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-lock"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Security</h2>
            </div>
          </div>
          <div class="settings-rows">
            <!-- 2FA row -->
            <div class="settings-row-toggle">
              <div>
                <p class="text-[13px] font-medium" style="color:var(--text-primary)">Two-Factor Authentication</p>
                <p class="text-[12px]" style="color:var(--text-muted)">Extra layer of security on sign-in</p>
              </div>
              <button
                @click="userProfile.twoFactorEnabled = !userProfile.twoFactorEnabled; toggleTwoFactor()"
                class="toggle-switch flex-shrink-0"
                :class="userProfile.twoFactorEnabled ? 'toggle-on' : 'toggle-off'"
              ></button>
            </div>
            <!-- Password change -->
            <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <div class="flex items-center justify-between mb-3">
                <div>
                  <p class="text-[13px] font-medium" style="color:var(--text-primary)">Password</p>
                  <p class="text-[12px]" style="color:var(--text-muted)">Update your login password</p>
                </div>
                <button @click="showChangePassword = !showChangePassword" class="btn-glass text-[12px] py-1.5 px-3">
                  {{ showChangePassword ? 'Cancel' : 'Change' }}
                </button>
              </div>
              <form v-if="showChangePassword" @submit.prevent="changePassword" class="flex flex-col gap-3 pt-3" style="border-top: 1px solid var(--glass-border);">
                <div v-if="passwordError" class="text-[12px] px-3 py-2 rounded-xl" style="background: rgba(239,68,68,0.10); color:#ef4444; border: 1px solid rgba(239,68,68,0.2);">
                  {{ passwordError }}
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                  <div>
                    <label class="up-label">Current Password</label>
                    <input v-model="passwordForm.currentPassword" type="password" required class="glass-input w-full" placeholder="••••••••">
                  </div>
                  <div>
                    <label class="up-label">New Password</label>
                    <input v-model="passwordForm.newPassword" type="password" required class="glass-input w-full" placeholder="••••••••">
                  </div>
                  <div>
                    <label class="up-label">Confirm</label>
                    <input v-model="passwordForm.confirmPassword" type="password" required class="glass-input w-full" placeholder="••••••••">
                  </div>
                </div>
                <div class="flex justify-end">
                  <button type="submit" :disabled="changingPassword" class="btn-accent text-[13px]">
                    {{ changingPassword ? 'Updating…' : 'Update Password' }}
                  </button>
                </div>
              </form>
            </div>

            <!-- Active sessions -->
            <div class="px-5 py-4">
              <div class="flex items-center justify-between mb-3">
                <div>
                  <p class="text-[13px] font-medium" style="color:var(--text-primary)">Active Sessions</p>
                  <p class="text-[12px]" style="color:var(--text-muted)">Devices currently signed in</p>
                </div>
                <button @click="terminateAllSessions" class="btn-glass text-[12px] py-1.5 px-3" style="color:#ef4444;">
                  Sign out all
                </button>
              </div>
              <div class="flex flex-col gap-2">
                <div
                  v-for="session in activeSessions"
                  :key="session.id"
                  class="flex items-center gap-3 p-3 rounded-xl"
                  :style="session.isCurrent
                    ? 'background: rgba(124,94,240,0.09); border: 1px solid rgba(124,94,240,0.22);'
                    : 'background: rgba(255,255,255,0.25); border: 1px solid var(--glass-border);'"
                >
                  <div
                    class="w-9 h-9 rounded-xl flex items-center justify-center flex-shrink-0"
                    style="background: rgba(255,255,255,0.4); border: 1px solid var(--glass-border);"
                  >
                    <i :class="getDeviceIcon(session.deviceType)" class="text-sm" style="color:var(--text-secondary)"></i>
                  </div>
                  <div class="flex-1 min-w-0">
                    <div class="flex items-center gap-2">
                      <p class="text-[13px] font-medium truncate" style="color:var(--text-primary)">{{ session.deviceName }}</p>
                      <span v-if="session.isCurrent" class="badge badge-info text-[10px] py-0.5 px-2 flex-shrink-0">Current</span>
                    </div>
                    <p class="text-[11px]" style="color:var(--text-muted)">{{ session.browser }} · {{ session.location }} · {{ formatTimeAgo(session.lastActive) }}</p>
                  </div>
                  <button
                    v-if="!session.isCurrent"
                    @click="terminateSession(session.id)"
                    class="btn-glass-icon flex-shrink-0"
                    style="width:30px;height:30px;color:#ef4444;"
                    title="Sign out"
                  >
                    <i class="fas fa-right-from-bracket text-xs"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Notification Preferences -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ff6b6b,#ee5a24); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-bell"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Notification Preferences</h2>
            </div>
          </div>
          <div class="settings-rows">
            <template v-for="category in notificationPreferences" :key="category.id">
              <div class="px-5 py-2.5" style="border-bottom: 1px solid var(--glass-border); background: rgba(0,0,0,0.02);">
                <p class="text-[11px] font-semibold uppercase tracking-wide" style="color:var(--text-muted)">{{ category.name }}</p>
              </div>
              <div
                v-for="pref in category.preferences"
                :key="pref.id"
                class="flex items-center justify-between px-5 py-3"
                style="border-bottom: 1px solid var(--glass-border);"
              >
                <div>
                  <p class="text-[13px] font-medium" style="color:var(--text-primary)">{{ pref.name }}</p>
                  <p class="text-[11px]" style="color:var(--text-muted)">{{ pref.description }}</p>
                </div>
                <div class="flex items-center gap-4">
                  <label class="flex flex-col items-center gap-1 cursor-pointer">
                    <button
                      @click="pref.email = !pref.email"
                      class="toggle-switch toggle-sm"
                      :class="pref.email ? 'toggle-on' : 'toggle-off'"
                    ></button>
                    <span class="text-[10px]" style="color:var(--text-muted)">Email</span>
                  </label>
                  <label class="flex flex-col items-center gap-1 cursor-pointer">
                    <button
                      @click="pref.push = !pref.push"
                      class="toggle-switch toggle-sm"
                      :class="pref.push ? 'toggle-on' : 'toggle-off'"
                    ></button>
                    <span class="text-[10px]" style="color:var(--text-muted)">Push</span>
                  </label>
                </div>
              </div>
            </template>
          </div>
          <div class="flex justify-end px-5 py-4">
            <button @click="saveNotificationPreferences" :disabled="savingNotifications" class="btn-accent text-[13px]">
              {{ savingNotifications ? 'Saving…' : 'Save Preferences' }}
            </button>
          </div>
        </div>

      </div>

      <!-- Right: quick links -->
      <div class="flex flex-col gap-5">

        <!-- Quick actions -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#34c759,#30a84b); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-bolt"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Quick Actions</h2>
            </div>
          </div>
          <div class="settings-rows">
            <button @click="viewOrders" class="settings-action-row">
              <div class="action-icon-wrap" style="background: linear-gradient(135deg,#5ac8fa,#007aff);">
                <i class="fas fa-bag-shopping text-[11px] text-white"></i>
              </div>
              <span class="text-[13px]" style="color:var(--text-primary)">My Orders</span>
              <i class="fas fa-chevron-right text-[10px] ml-auto" style="color:var(--text-muted)"></i>
            </button>
            <button @click="viewActivity" class="settings-action-row">
              <div class="action-icon-wrap" style="background: linear-gradient(135deg,#c084fc,#9333ea);">
                <i class="fas fa-clock-rotate-left text-[11px] text-white"></i>
              </div>
              <span class="text-[13px]" style="color:var(--text-primary)">Activity Log</span>
              <i class="fas fa-chevron-right text-[10px] ml-auto" style="color:var(--text-muted)"></i>
            </button>
            <button @click="downloadData" class="settings-action-row border-none">
              <div class="action-icon-wrap" style="background: linear-gradient(135deg,#34c759,#30a84b);">
                <i class="fas fa-download text-[11px] text-white"></i>
              </div>
              <span class="text-[13px]" style="color:var(--text-primary)">Download My Data</span>
              <i class="fas fa-chevron-right text-[10px] ml-auto" style="color:var(--text-muted)"></i>
            </button>
          </div>
        </div>

        <!-- Account info -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ff9f0a,#e08800); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-circle-info"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Account Info</h2>
            </div>
          </div>
          <div class="settings-rows">
            <div class="settings-row">
              <span class="settings-label">Role</span>
              <span class="badge badge-info text-[11px]">{{ userProfile.role }}</span>
            </div>
            <div class="settings-row">
              <span class="settings-label">Member since</span>
              <span class="text-[12px]" style="color:var(--text-secondary)">{{ formatDate(userProfile.createdAt) }}</span>
            </div>
            <div class="settings-row border-none">
              <span class="settings-label">2FA</span>
              <span
                class="text-[11px] font-medium px-2 py-0.5 rounded-full"
                :style="userProfile.twoFactorEnabled
                  ? 'background: rgba(52,199,89,0.14); color: #22c55e;'
                  : 'background: rgba(120,120,128,0.14); color: var(--text-muted);'"
              >{{ userProfile.twoFactorEnabled ? 'Enabled' : 'Disabled' }}</span>
            </div>
          </div>
        </div>

        <!-- Danger zone -->
        <div class="glass-card p-0 overflow-hidden" style="border-color: rgba(239,68,68,0.25);">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ef4444,#b91c1c); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-triangle-exclamation"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:#ef4444">Danger Zone</h2>
            </div>
          </div>
          <div class="px-5 pb-5">
            <p class="text-[12px] mb-3" style="color:var(--text-muted)">Permanently delete your account and all associated data. This cannot be undone.</p>
            <button
              @click="showDeleteModal = true"
              class="w-full py-2.5 rounded-xl text-[13px] font-medium transition-all duration-150"
              style="background: rgba(239,68,68,0.10); color:#ef4444; border: 1px solid rgba(239,68,68,0.25);"
              onmouseover="this.style.background='rgba(239,68,68,0.18)'"
              onmouseout="this.style.background='rgba(239,68,68,0.10)'"
            >
              <i class="fas fa-trash-can mr-2 text-xs"></i>Delete Account
            </button>
          </div>
        </div>

      </div>
    </div>

    <!-- ── Delete confirmation modal ──────────────────────────────────── -->
    <div v-if="showDeleteModal" class="fixed inset-0 z-50 flex items-center justify-center p-4" style="background: rgba(0,0,0,0.45); backdrop-filter: blur(6px);">
      <div class="glass-card p-6 w-full max-w-md">
        <div class="flex items-center gap-3 mb-4">
          <div class="stat-icon flex-shrink-0" style="background: linear-gradient(135deg,#ef4444,#b91c1c); width:36px; height:36px; font-size:15px;">
            <i class="fas fa-triangle-exclamation"></i>
          </div>
          <div>
            <h3 class="text-[15px] font-semibold" style="color:var(--text-primary)">Delete Account</h3>
            <p class="text-[12px]" style="color:var(--text-muted)">This action is permanent and cannot be undone</p>
          </div>
        </div>
        <p class="text-[13px] mb-4" style="color:var(--text-secondary)">
          All your data — orders, settings, profile — will be permanently removed. Type <strong style="color:var(--text-primary)">DELETE</strong> to confirm.
        </p>
        <input
          v-model="deleteConfirmation"
          type="text"
          placeholder="Type DELETE here"
          class="glass-input w-full mb-4"
          :style="deleteError ? 'border-color: rgba(239,68,68,0.6);' : ''"
        >
        <p v-if="deleteError" class="text-[12px] mb-3" style="color:#ef4444">{{ deleteError }}</p>
        <div class="flex gap-2 justify-end">
          <button @click="showDeleteModal = false; deleteConfirmation = ''; deleteError = ''" class="btn-glass">Cancel</button>
          <button
            :disabled="deleteConfirmation !== 'DELETE'"
            @click="deleteAccount"
            class="px-4 py-2 rounded-xl text-[13px] font-medium transition-all duration-150 disabled:opacity-40"
            style="background: #ef4444; color:#fff;"
          >Delete Account</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { formatCurrency, formatDate, formatTimeAgo, getInitials } from '@/utils/formatters';

const router = useRouter();

const userProfile = reactive({
  name: 'Ali',
  email: 'alib181220@gmail.com',
  phone: '+31 6 00 000 000',
  location: 'Amsterdam, Netherlands',
  bio: 'Store administrator with a passion for clean design and efficient e-commerce management.',
  role: 'Administrator',
  avatar: '',
  createdAt: '2024-01-15T10:30:00Z',
  twoFactorEnabled: true,
  twoFactorLastUsed: '2024-01-10T14:30:00Z',
});

const userStats = reactive({ orders: 42, revenue: 12500, customers: 128 });

const editingPersonalInfo = ref(false);
const savingPersonalInfo = ref(false);
const personalInfoForm = reactive({ name: '', email: '', phone: '', location: '', bio: '' });

const showChangePassword = ref(false);
const changingPassword = ref(false);
const passwordForm = reactive({ currentPassword: '', newPassword: '', confirmPassword: '' });
const passwordError = ref('');

const activeSessions = ref([
  { id: 1, deviceType: 'desktop', deviceName: 'MacBook Pro',  browser: 'Chrome 120', location: 'Amsterdam, NL',   lastActive: '2024-01-15T14:30:00Z', isCurrent: true  },
  { id: 2, deviceType: 'mobile',  deviceName: 'iPhone 15',    browser: 'Safari 17',  location: 'Amsterdam, NL',   lastActive: '2024-01-14T09:15:00Z', isCurrent: false },
  { id: 3, deviceType: 'tablet',  deviceName: 'iPad Pro',     browser: 'Safari 17',  location: 'Rotterdam, NL',  lastActive: '2024-01-12T16:45:00Z', isCurrent: false },
]);

const savingNotifications = ref(false);
const notificationPreferences = ref([
  {
    id: 1, name: 'Orders',
    preferences: [
      { id: 1, name: 'New Orders',     description: 'When a customer places a new order',   email: true,  push: true  },
      { id: 2, name: 'Order Updates',  description: 'When an order status changes',          email: true,  push: false },
      { id: 3, name: 'Cancellations',  description: 'When an order is cancelled',            email: true,  push: true  },
    ]
  },
  {
    id: 2, name: 'Inventory',
    preferences: [
      { id: 4, name: 'Low Stock',   description: 'When a product is running low',     email: true,  push: false },
      { id: 5, name: 'Out of Stock',description: 'When a product goes out of stock',  email: true,  push: true  },
    ]
  },
  {
    id: 3, name: 'Customers',
    preferences: [
      { id: 6, name: 'New Customers', description: 'When new customers register',      email: true,  push: false },
      { id: 7, name: 'Reviews',       description: 'When customers leave reviews',     email: true,  push: true  },
    ]
  },
]);

const showDeleteModal = ref(false);
const deleteConfirmation = ref('');
const deleteError = ref('');
const avatarInput = ref<HTMLInputElement | null>(null);

const getUserInitials = (name: string) => getInitials(name);

const getDeviceIcon = (deviceType: string) => {
  if (deviceType === 'mobile') return 'fas fa-mobile-screen';
  if (deviceType === 'tablet') return 'fas fa-tablet-screen-button';
  return 'fas fa-laptop';
};

const triggerAvatarUpload = () => avatarInput.value?.click();

const handleAvatarUpload = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0];
  if (file) userProfile.avatar = URL.createObjectURL(file);
};

const editPersonalInfo = () => {
  editingPersonalInfo.value = true;
  Object.assign(personalInfoForm, {
    name: userProfile.name, email: userProfile.email,
    phone: userProfile.phone, location: userProfile.location, bio: userProfile.bio,
  });
};

const cancelEditPersonalInfo = () => { editingPersonalInfo.value = false; };

const savePersonalInfo = async () => {
  savingPersonalInfo.value = true;
  await new Promise(r => setTimeout(r, 700));
  Object.assign(userProfile, personalInfoForm);
  editingPersonalInfo.value = false;
  savingPersonalInfo.value = false;
};

const changePassword = async () => {
  passwordError.value = '';
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    passwordError.value = 'Passwords do not match'; return;
  }
  if (passwordForm.newPassword.length < 8) {
    passwordError.value = 'Password must be at least 8 characters'; return;
  }
  changingPassword.value = true;
  await new Promise(r => setTimeout(r, 800));
  Object.assign(passwordForm, { currentPassword: '', newPassword: '', confirmPassword: '' });
  showChangePassword.value = false;
  changingPassword.value = false;
};

const toggleTwoFactor = async () => {
  await new Promise(r => setTimeout(r, 300));
};

const terminateSession = (id: number) => {
  activeSessions.value = activeSessions.value.filter(s => s.id !== id);
};

const terminateAllSessions = () => {
  activeSessions.value = activeSessions.value.filter(s => s.isCurrent);
};

const saveNotificationPreferences = async () => {
  savingNotifications.value = true;
  await new Promise(r => setTimeout(r, 700));
  savingNotifications.value = false;
};

const viewOrders   = () => router.push('/orders');
const viewActivity = () => router.push('/analytics');

const downloadData = () => {
  const data = { profile: userProfile, stats: userStats };
  const uri = 'data:application/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(data, null, 2));
  const a = document.createElement('a');
  a.href = uri; a.download = `profile_${new Date().toISOString().split('T')[0]}.json`; a.click();
};

const deleteAccount = async () => {
  if (deleteConfirmation.value !== 'DELETE') { deleteError.value = 'Please type DELETE exactly'; return; }
  await new Promise(r => setTimeout(r, 1200));
  showDeleteModal.value = false;
};

onMounted(() => {});
</script>

<style scoped>
.settings-rows { display: flex; flex-direction: column; }

.settings-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 11px 20px;
  border-bottom: 1px solid var(--glass-border);
  gap: 12px;
}

.settings-row.border-none { border-bottom: none; }

.settings-row-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid var(--glass-border);
  gap: 16px;
}

.settings-label {
  font-size: 12px;
  font-weight: 500;
  color: var(--text-muted);
  flex-shrink: 0;
  min-width: 90px;
}

.settings-action-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 12px 20px;
  border-bottom: 1px solid var(--glass-border);
  transition: background 0.15s ease;
  cursor: pointer;
}

.settings-action-row:hover { background: rgba(255,255,255,0.25); }
.settings-action-row.border-none { border-bottom: none; }

.action-icon-wrap {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.up-label {
  display: block;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary);
  margin-bottom: 5px;
}

/* iOS toggle — large (default 51×31) */
.toggle-switch {
  width: 51px;
  height: 31px;
  border-radius: 999px;
  position: relative;
  border: none;
  cursor: pointer;
  transition: background 0.25s ease;
  flex-shrink: 0;
}

.toggle-switch::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px; /* default — overridden by on/off below */
  width: 27px;
  height: 27px;
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.25);
  transition: left 0.25s ease;
}

.toggle-off { background: rgba(120,120,128,0.28); }
.toggle-off::after { left: 2px; }

.toggle-on { background: var(--accent); box-shadow: 0 0 12px rgba(124,94,240,0.4); }
.toggle-on::after { left: 22px; }

/* Small toggle (36×22) — used for notification rows */
.toggle-sm {
  width: 36px;
  height: 22px;
}

.toggle-sm::after {
  width: 18px;
  height: 18px;
  top: 2px;
}

.toggle-sm.toggle-off::after { left: 2px; }
.toggle-sm.toggle-on::after  { left: 16px; }
</style>
