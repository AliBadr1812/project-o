<template>
  <div class="user-profile">
    <div class="mb-8">
      <h1 class="text-2xl font-bold text-gray-800">My Profile</h1>
      <p class="text-gray-600 mt-1">Manage your personal information and account settings</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left Column - Profile Overview -->
      <div>
        <!-- Profile Card -->
        <Card class="mb-6">
          <div class="text-center">
            <!-- Profile Picture -->
            <div class="relative inline-block mb-4">
              <div
                class="w-32 h-32 rounded-full overflow-hidden border-4 border-white shadow-lg mx-auto"
              >
                <img
                  v-if="userProfile.avatar"
                  :src="userProfile.avatar"
                  :alt="userProfile.name"
                  class="w-full h-full object-cover"
                />
                <div
                  v-else
                  class="w-full h-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-white text-4xl font-bold"
                >
                  {{ getUserInitials(userProfile.name) }}
                </div>
              </div>
              <button
                @click="triggerAvatarUpload"
                class="absolute bottom-2 right-2 bg-blue-500 text-white rounded-full p-2 hover:bg-blue-600 shadow-lg"
              >
                <i class="fas fa-camera"></i>
              </button>
              <input
                ref="avatarInput"
                type="file"
                accept="image/*"
                class="hidden"
                @change="handleAvatarUpload"
              />
            </div>

            <!-- User Info -->
            <h2 class="text-xl font-bold text-gray-800">{{ userProfile.name }}</h2>
            <p class="text-gray-600 mb-1">{{ userProfile.email }}</p>
            <p class="text-gray-500 text-sm mb-4">{{ userProfile.role }}</p>

            <!-- Stats -->
            <div class="grid grid-cols-3 gap-4 mb-6">
              <div class="text-center">
                <div class="text-lg font-bold text-gray-800">{{ userStats.orders }}</div>
                <div class="text-xs text-gray-600">Orders</div>
              </div>
              <div class="text-center">
                <div class="text-lg font-bold text-gray-800">
                  {{ formatCurrency(userStats.revenue) }}
                </div>
                <div class="text-xs text-gray-600">Revenue</div>
              </div>
              <div class="text-center">
                <div class="text-lg font-bold text-gray-800">{{ userStats.customers }}</div>
                <div class="text-xs text-gray-600">Customers</div>
              </div>
            </div>

            <!-- Member Since -->
            <div class="text-center">
              <p class="text-sm text-gray-500">
                <i class="fas fa-calendar-alt mr-1"></i>
                Member since {{ formatDate(userProfile.createdAt) }}
              </p>
            </div>
          </div>
        </Card>

        <!-- Quick Links -->
        <Card>
          <h3 class="text-lg font-semibold text-gray-800 mb-4">Quick Links</h3>
          <div class="gap-2">
            <Button
              variant="outline"
              class="w-full justify-start"
              @click="viewOrders"
            >
              <i class="fas fa-shopping-cart mr-3"></i>
              My Orders
            </Button>
            <Button
              variant="outline"
              class="w-full justify-start"
              @click="viewActivity"
            >
              <i class="fas fa-history mr-3"></i>
              Activity Log
            </Button>
            <Button
              variant="outline"
              class="w-full justify-start"
              @click="downloadData"
            >
              <i class="fas fa-download mr-3"></i>
              Download My Data
            </Button>
            <Button
              variant="outline"
              class="w-full justify-start text-red-600 hover:text-red-700 hover:border-red-200"
              @click="showDeleteModal = true"
            >
              <i class="fas fa-trash mr-3"></i>
              Delete Account
            </Button>
          </div>
        </Card>
      </div>

      <!-- Right Column - Profile Settings -->
      <div class="lg:col-span-2">
        <!-- Personal Information -->
        <Card class="mb-6">
          <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-semibold text-gray-800">Personal Information</h2>
            <Button
              variant="outline"
              size="sm"
              @click="editPersonalInfo"
            >
              <i class="fas fa-edit mr-2"></i>
              Edit
            </Button>
          </div>

          <div v-if="!editingPersonalInfo" class="gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm text-gray-600 mb-1">Full Name</label>
                <p class="font-medium">{{ userProfile.name }}</p>
              </div>
              <div>
                <label class="block text-sm text-gray-600 mb-1">Email Address</label>
                <p class="font-medium">{{ userProfile.email }}</p>
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm text-gray-600 mb-1">Phone Number</label>
                <p class="font-medium">{{ userProfile.phone || 'Not set' }}</p>
              </div>
              <div>
                <label class="block text-sm text-gray-600 mb-1">Location</label>
                <p class="font-medium">{{ userProfile.location || 'Not set' }}</p>
              </div>
            </div>

            <div>
              <label class="block text-sm text-gray-600 mb-1">Bio</label>
              <p class="text-gray-700">{{ userProfile.bio || 'No bio added yet.' }}</p>
            </div>
          </div>

          <form v-else @submit.prevent="savePersonalInfo" class="gap-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Full Name *
                </label>
                <input
                  type="text"
                  v-model="personalInfoForm.name"
                  required
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Email Address *
                </label>
                <input
                  type="email"
                  v-model="personalInfoForm.email"
                  required
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Phone Number
                </label>
                <input
                  type="tel"
                  v-model="personalInfoForm.phone"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Location
                </label>
                <input
                  type="text"
                  v-model="personalInfoForm.location"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Bio
              </label>
              <textarea
                v-model="personalInfoForm.bio"
                rows="3"
                placeholder="Tell us a little about yourself..."
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              ></textarea>
            </div>

            <div class="flex justify-end gap-4">
              <Button
                type="button"
                variant="outline"
                @click="cancelEditPersonalInfo"
              >
                Cancel
              </Button>
              <Button
                type="submit"
                variant="primary"
                :loading="savingPersonalInfo"
              >
                Save Changes
              </Button>
            </div>
          </form>
        </Card>

        <!-- Security Settings -->
        <Card class="mb-6">
          <h2 class="text-xl font-semibold text-gray-800 mb-6">Security Settings</h2>
          <div class="gap-6">
            <!-- Password Change -->
            <div>
              <div class="flex justify-between items-center mb-4">
                <div>
                  <h3 class="text-lg font-medium text-gray-800">Password</h3>
                  <p class="text-sm text-gray-600">Update your password regularly</p>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  @click="showChangePassword = !showChangePassword"
                >
                  {{ showChangePassword ? 'Cancel' : 'Change Password' }}
                </Button>
              </div>

              <form
                v-if="showChangePassword"
                @submit.prevent="changePassword"
                class="gap-4"
              >
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Current Password *
                  </label>
                  <div class="relative">
                    <input
                      :type="showCurrentPassword ? 'text' : 'password'"
                      v-model="passwordForm.currentPassword"
                      required
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 pr-10"
                    />
                    <button
                      type="button"
                      @click="showCurrentPassword = !showCurrentPassword"
                      class="absolute right-3 top-2 text-gray-500"
                    >
                      <i :class="showCurrentPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                    </button>
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      New Password *
                    </label>
                    <div class="relative">
                      <input
                        :type="showNewPassword ? 'text' : 'password'"
                        v-model="passwordForm.newPassword"
                        required
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 pr-10"
                      />
                      <button
                        type="button"
                        @click="showNewPassword = !showNewPassword"
                        class="absolute right-3 top-2 text-gray-500"
                      >
                        <i :class="showNewPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                      </button>
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Confirm Password *
                    </label>
                    <div class="relative">
                      <input
                        :type="showConfirmPassword ? 'text' : 'password'"
                        v-model="passwordForm.confirmPassword"
                        required
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 pr-10"
                      />
                      <button
                        type="button"
                        @click="showConfirmPassword = !showConfirmPassword"
                        class="absolute right-3 top-2 text-gray-500"
                      >
                        <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                      </button>
                    </div>
                  </div>
                </div>

                <div v-if="passwordError" class="text-sm text-red-600">
                  {{ passwordError }}
                </div>

                <div class="flex justify-end">
                  <Button
                    type="submit"
                    variant="primary"
                    :loading="changingPassword"
                  >
                    Update Password
                  </Button>
                </div>
              </form>
            </div>

            <!-- Two-Factor Authentication -->
            <div class="border-t border-gray-200 pt-6">
              <div class="flex justify-between items-center">
                <div>
                  <h3 class="text-lg font-medium text-gray-800">Two-Factor Authentication</h3>
                  <p class="text-sm text-gray-600">Add an extra layer of security to your account</p>
                </div>
                <label class="relative inline-flex items-center cursor-pointer">
                  <input
                    type="checkbox"
                    v-model="userProfile.twoFactorEnabled"
                    class="sr-only peer"
                    @change="toggleTwoFactor"
                  />
                  <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                </label>
              </div>

              <div v-if="userProfile.twoFactorEnabled" class="mt-4 p-4 bg-blue-50 rounded-lg">
                <div class="flex items-start">
                  <i class="fas fa-shield-alt text-blue-600 mt-1 mr-3"></i>
                  <div>
                    <p class="text-sm text-blue-800 font-medium">2FA is enabled</p>
                    <p class="text-xs text-blue-700 mt-1">
                      Your account is protected with two-factor authentication.
                      Last used: {{ formatDate(userProfile.twoFactorLastUsed) }}
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Active Sessions -->
            <div class="border-t border-gray-200 pt-6">
              <div class="flex justify-between items-center mb-4">
                <div>
                  <h3 class="text-lg font-medium text-gray-800">Active Sessions</h3>
                  <p class="text-sm text-gray-600">Manage your active login sessions</p>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  @click="refreshSessions"
                >
                  <i class="fas fa-sync-alt mr-2"></i>
                  Refresh
                </Button>
              </div>

              <div v-if="activeSessions.length === 0" class="text-center py-4 text-gray-500">
                <p>No active sessions found</p>
              </div>

              <div v-else class="gap-3">
                <div
                  v-for="session in activeSessions"
                  :key="session.id"
                  class="flex items-center justify-between p-3 border border-gray-200 rounded-lg"
                  :class="{ 'border-blue-300 bg-blue-50': session.isCurrent }"
                >
                  <div class="flex items-center">
                    <div class="mr-3">
                      <i
                        :class="getDeviceIcon(session.deviceType)"
                        class="text-xl text-gray-500"
                      ></i>
                    </div>
                    <div>
                      <p class="font-medium">{{ session.deviceName }}</p>
                      <p class="text-xs text-gray-600">
                        {{ session.browser }} • {{ session.location }}
                      </p>
                      <p class="text-xs text-gray-500">
                        Last active: {{ formatTimeAgo(session.lastActive) }}
                      </p>
                    </div>
                  </div>
                  <div class="flex items-center gap-2">
                    <Badge
                      v-if="session.isCurrent"
                      variant="info"
                    >
                      Current
                    </Badge>
                    <Button
                      v-if="!session.isCurrent"
                      variant="ghost"
                      size="sm"
                      @click="terminateSession(session.id)"
                    >
                      <i class="fas fa-sign-out-alt text-red-600"></i>
                    </Button>
                  </div>
                </div>
              </div>

              <div class="mt-4 text-right">
                <Button
                  variant="outline"
                  size="sm"
                  @click="terminateAllSessions"
                >
                  <i class="fas fa-sign-out-alt mr-2"></i>
                  Terminate All Other Sessions
                </Button>
              </div>
            </div>
          </div>
        </Card>

        <!-- Notification Preferences -->
        <Card>
          <h2 class="text-xl font-semibold text-gray-800 mb-6">Notification Preferences</h2>
          <div class="gap-6">
            <div v-for="category in notificationPreferences" :key="category.id">
              <h3 class="text-lg font-medium text-gray-800 mb-4">{{ category.name }}</h3>
              <div class="gap-3">
                <div
                  v-for="pref in category.preferences"
                  :key="pref.id"
                  class="flex items-center justify-between"
                >
                  <div>
                    <label class="font-medium text-gray-800">{{ pref.name }}</label>
                    <p class="text-sm text-gray-600">{{ pref.description }}</p>
                  </div>
                  <div class="flex items-center gap-4">
                    <label class="flex items-center gap-2">
                      <input
                        type="checkbox"
                        v-model="pref.email"
                        class="rounded text-blue-500"
                      />
                      <span class="text-sm">Email</span>
                    </label>
                    <label class="flex items-center gap-2">
                      <input
                        type="checkbox"
                        v-model="pref.push"
                        class="rounded text-blue-500"
                      />
                      <span class="text-sm">Push</span>
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div class="flex justify-end pt-6 border-t border-gray-200">
              <Button
                variant="primary"
                @click="saveNotificationPreferences"
                :loading="savingNotifications"
              >
                Save Preferences
              </Button>
            </div>
          </div>
        </Card>
      </div>
    </div>

    <!-- Delete Account Modal -->
    <Modal
      v-if="showDeleteModal"
      title="Delete Account"
      @close="showDeleteModal = false"
    >
      <div class="gap-4">
        <div class="flex items-start">
          <div class="flex-shrink-0">
            <i class="fas fa-exclamation-triangle text-red-600 text-xl"></i>
          </div>
          <div class="ml-3">
            <h3 class="text-lg font-medium text-gray-900">Are you sure you want to delete your account?</h3>
            <div class="mt-2 text-sm text-gray-600">
              <p>This action cannot be undone. All of your data will be permanently removed from our servers.</p>
              <p class="mt-2 font-medium">Please type "DELETE" to confirm:</p>
            </div>
          </div>
        </div>

        <div>
          <input
            type="text"
            v-model="deleteConfirmation"
            placeholder="Type DELETE here"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg"
            :class="{ 'border-red-500': deleteError }"
          />
          <p v-if="deleteError" class="mt-1 text-sm text-red-600">{{ deleteError }}</p>
        </div>

        <div class="flex justify-end gap-4">
          <Button
            variant="outline"
            @click="showDeleteModal = false"
          >
            Cancel
          </Button>
          <Button
            variant="danger"
            :disabled="deleteConfirmation !== 'DELETE'"
            @click="deleteAccount"
          >
            Delete Account
          </Button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script setup lang="ts">
import Modal from '@/components/ui/Modal.vue';
import Button from '@/components/ui/Button.vue';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate, formatTimeAgo } from '@/utils/formatters';
</script>
