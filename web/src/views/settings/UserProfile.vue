<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="pb-5">
      <h1 class="text-2xl font-bold text-white">My Profile</h1>
      <p class="text-[var(--color-icon)] mt-1">Manage your personal information and account settings</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left Column - Profile Overview -->
      <div>
        <!-- Profile Card -->
        <Card class="mb-6 border border-[var(--color-border)] bg-[var(--color-card)]">
          <div class="text-center p-6">
            <!-- Profile Picture -->
            <div class="relative inline-block mb-4">
              <div
                class="w-32 h-32 rounded-full overflow-hidden border-4 border-[var(--color-card)] shadow-lg mx-auto"
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
                class="absolute bottom-2 right-2 bg-[var(--primary-color)] text-white rounded-full p-2 hover:bg-blue-700 shadow-lg transition-colors duration-150 cursor-pointer"
              >
                <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M4 5a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2V7a2 2 0 00-2-2h-1.586a1 1 0 01-.707-.293l-1.121-1.121A2 2 0 0011.172 3H8.828a2 2 0 00-1.414.586L6.293 4.707A1 1 0 015.586 5H4zm6 9a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>
                </svg>
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
            <h2 class="text-xl font-bold text-white">{{ userProfile.name }}</h2>
            <p class="text-[var(--color-icon)] mb-1">{{ userProfile.email }}</p>
            <div class="inline-block">
              <span class="text-xs text-gray-400 bg-gray-800/50 px-3 py-1 rounded-full">{{ userProfile.role }}</span>
            </div>

            <!-- Stats -->
            <div class="grid grid-cols-3 gap-4 pb-4">
              <div class="text-center">
                <div class="text-lg font-bold text-white">{{ userStats.orders }}</div>
                <div class="text-xs text-[var(--color-icon)]">Orders</div>
              </div>
              <div class="text-center">
                <div class="text-lg font-bold text-white">
                  {{ formatCurrency(userStats.revenue) }}
                </div>
                <div class="text-xs text-[var(--color-icon)]">Revenue</div>
              </div>
              <div class="text-center">
                <div class="text-lg font-bold text-white">{{ userStats.customers }}</div>
                <div class="text-xs text-[var(--color-icon)]">Customers</div>
              </div>
            </div>

            <!-- Member Since -->
            <div class="text-center pt-4 border-t border-[var(--color-border)]">
              <p class="text-sm text-[var(--color-icon)]">
                <svg class="w-4 h-4 inline mr-1 -mt-1" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/>
                </svg>
                Member since {{ formatDate(userProfile.createdAt) }}
              </p>
            </div>
          </div>
        </Card>

        <!-- Quick Links -->
        <Card class="border border-[var(--color-border)] bg-[var(--color-card)] p-6">
          <h3 class="text-lg font-semibold text-white pb-4">Quick Links</h3>
          <div class="grid grid-cols-1 gap-2">
            <button
              @click="viewOrders"
              class="cursor-pointer w-full text-left px-4 py-3 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150 flex items-center"
            >
              <svg class="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z"/>
              </svg>
              My Orders
            </button>
            <button
              @click="viewActivity"
              class="cursor-pointer w-full text-left px-4 py-3 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150 flex items-center"
            >
              <svg class="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd"/>
              </svg>
              Activity Log
            </button>
            <button
              @click="downloadData"
              class="cursor-pointer w-full text-left px-4 py-3 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150 flex items-center"
            >
              <svg class="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd"/>
              </svg>
              Download My Data
            </button>
            <button
              @click="showDeleteModal = true"
              class="cursor-pointer w-full text-left px-4 py-3 border border-red-500/30 rounded-lg text-red-400 hover:bg-red-500/10 hover:text-red-300 transition-colors duration-150 flex items-center"
            >
              <svg class="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"/>
              </svg>
              Delete Account
            </button>
          </div>
        </Card>
      </div>

      <!-- Right Column - Profile Settings -->
      <div class="lg:col-span-2">
        <!-- Personal Information -->
        <Card class="mb-6 border border-[var(--color-border)] bg-[var(--color-card)] p-6">
          <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-semibold text-white">Personal Information</h2>
            <button
              @click="editPersonalInfo"
              class="flex items-center gap-2 px-4 py-2 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150"
            >
              <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
              </svg>
              <span>Edit</span>
            </button>
          </div>

          <div v-if="!editingPersonalInfo" class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm text-[var(--color-icon)] mb-1">Full Name</label>
              <p class="font-medium text-white">{{ userProfile.name }}</p>
            </div>
            <div>
              <label class="block text-sm text-[var(--color-icon)] mb-1">Email Address</label>
              <p class="font-medium text-white">{{ userProfile.email }}</p>
            </div>
            <div>
              <label class="block text-sm text-[var(--color-icon)] mb-1">Phone Number</label>
              <p class="font-medium text-white">{{ userProfile.phone || 'Not set' }}</p>
            </div>
            <div>
              <label class="block text-sm text-[var(--color-icon)] mb-1">Location</label>
              <p class="font-medium text-white">{{ userProfile.location || 'Not set' }}</p>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm text-[var(--color-icon)] mb-1">Bio</label>
              <p class="text-[var(--color-icon)]">{{ userProfile.bio || 'No bio added yet.' }}</p>
            </div>
          </div>

          <form v-else @submit.prevent="savePersonalInfo" class="gap-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Full Name *
                </label>
                <input
                  type="text"
                  v-model="personalInfoForm.name"
                  required
                  class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
                  placeholder="Enter your full name"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Email Address *
                </label>
                <input
                  type="email"
                  v-model="personalInfoForm.email"
                  required
                  class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
                  placeholder="Enter your email"
                />
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Phone Number
                </label>
                <input
                  type="tel"
                  v-model="personalInfoForm.phone"
                  class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
                  placeholder="Enter your phone number"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Location
                </label>
                <input
                  type="text"
                  v-model="personalInfoForm.location"
                  class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
                  placeholder="Enter your location"
                />
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                Bio
              </label>
              <textarea
                v-model="personalInfoForm.bio"
                rows="3"
                placeholder="Tell us a little about yourself..."
                class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
              ></textarea>
            </div>

            <div class="flex justify-end gap-4 pt-4">
              <button
                type="button"
                @click="cancelEditPersonalInfo"
                class="px-6 py-2.5 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] transition-colors duration-150"
              >
                Cancel
              </button>
              <button
                type="submit"
                :disabled="savingPersonalInfo"
                class="px-6 py-2.5 bg-[var(--primary-color)] text-white rounded-lg hover:bg-blue-700 transition-colors duration-150 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
              >
                <svg v-if="savingPersonalInfo" class="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <span>{{ savingPersonalInfo ? 'Saving...' : 'Save Changes' }}</span>
              </button>
            </div>
          </form>
        </Card>

        <!-- Security Settings -->
        <Card class="mb-6 border border-[var(--color-border)] bg-[var(--color-card)] p-6">
          <h2 class="text-xl font-semibold text-white mb-6">Security Settings</h2>
          <div class="gap-6">
            <!-- Password Change -->
            <div>
              <div class="flex justify-between items-center mb-4">
                <div>
                  <h3 class="text-lg font-medium text-white">Password</h3>
                  <p class="text-sm text-[var(--color-icon)]">Update your password regularly</p>
                </div>
                <button
                  @click="showChangePassword = !showChangePassword"
                  class="px-4 py-2 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150"
                >
                  {{ showChangePassword ? 'Cancel' : 'Change Password' }}
                </button>
              </div>

              <form
                v-if="showChangePassword"
                @submit.prevent="changePassword"
                class="gap-4 pt-4 border-t border-[var(--color-border)]"
              >
                <div>
                  <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                    Current Password *
                  </label>
                  <div class="relative">
                    <input
                      :type="showCurrentPassword ? 'text' : 'password'"
                      v-model="passwordForm.currentPassword"
                      required
                      class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500 pr-10"
                      placeholder="Enter current password"
                    />
                    <button
                      type="button"
                      @click="showCurrentPassword = !showCurrentPassword"
                      class="absolute right-3 top-2.5 text-[var(--color-icon)] hover:text-white"
                    >
                      <!-- Eye icon for show, Eye-slash for hide -->
                      <svg v-if="showCurrentPassword" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd"/>
                        <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z"/>
                      </svg>
                      <svg v-else class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                        <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                      </svg>
                    </button>
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                      New Password *
                    </label>
                    <div class="relative">
                      <input
                        :type="showNewPassword ? 'text' : 'password'"
                        v-model="passwordForm.newPassword"
                        required
                        class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500 pr-10"
                        placeholder="Enter new password"
                      />
                      <button
                        type="button"
                        @click="showNewPassword = !showNewPassword"
                        class="absolute right-3 top-2.5 text-[var(--color-icon)] hover:text-white"
                      >
                        <!-- Eye icon for show, Eye-slash for hide -->
                        <svg v-if="showNewPassword" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                          <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd"/>
                          <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z"/>
                        </svg>
                        <svg v-else class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                          <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                          <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                        </svg>
                      </button>
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                      Confirm Password *
                    </label>
                    <div class="relative">
                      <input
                        :type="showConfirmPassword ? 'text' : 'password'"
                        v-model="passwordForm.confirmPassword"
                        required
                        class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500 pr-10"
                        placeholder="Confirm new password"
                      />
                      <button
                        type="button"
                        @click="showConfirmPassword = !showConfirmPassword"
                        class="absolute right-3 top-2.5 text-[var(--color-icon)] hover:text-white"
                      >
                        <!-- Eye icon for show, Eye-slash for hide -->
                        <svg v-if="showConfirmPassword" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                          <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd"/>
                          <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z"/>
                        </svg>
                        <svg v-else class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                          <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                          <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>

                <div v-if="passwordError" class="text-sm text-red-500 p-3 bg-red-500/10 rounded-lg">
                  {{ passwordError }}
                </div>

                <div class="flex justify-end">
                  <button
                    type="submit"
                    :disabled="changingPassword"
                    class="px-6 py-2.5 bg-[var(--primary-color)] text-white rounded-lg hover:bg-blue-700 transition-colors duration-150 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
                  >
                    <svg v-if="changingPassword" class="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <span>{{ changingPassword ? 'Updating...' : 'Update Password' }}</span>
                  </button>
                </div>
              </form>
            </div>

            <!-- Two-Factor Authentication -->
            <div class="border-t border-[var(--color-border)] pt-6">
              <div class="flex justify-between items-center">
                <div>
                  <h3 class="text-lg font-medium text-white">Two-Factor Authentication</h3>
                  <p class="text-sm text-[var(--color-icon)]">Add an extra layer of security to your account</p>
                </div>
                <label class="relative inline-flex items-center cursor-pointer">
                  <input
                    type="checkbox"
                    v-model="userProfile.twoFactorEnabled"
                    class="sr-only peer"
                    @change="toggleTwoFactor"
                  />
                  <div class="w-11 h-6 bg-gray-700 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                </label>
              </div>

              <div v-if="userProfile.twoFactorEnabled" class="mt-4 p-4 bg-blue-500/10 rounded-lg border border-blue-500/20">
                <div class="flex items-start">
                  <svg class="w-5 h-5 text-blue-400 mt-1 mr-3" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
                  </svg>
                  <div>
                    <p class="text-sm text-blue-400 font-medium">2FA is enabled</p>
                    <p class="text-xs text-blue-400/80 mt-1">
                      Your account is protected with two-factor authentication.
                      Last used: {{ formatDate(userProfile.twoFactorLastUsed) }}
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Active Sessions -->
            <div class="border-t border-[var(--color-border)] pt-6">
              <div class="flex justify-between items-center mb-4">
                <div>
                  <h3 class="text-lg font-medium text-white">Active Sessions</h3>
                  <p class="text-sm text-[var(--color-icon)]">Manage your active login sessions</p>
                </div>
                <button
                  @click="refreshSessions"
                  class="flex items-center gap-2 px-4 py-2 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] hover:text-white transition-colors duration-150"
                >
                  <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z" clip-rule="evenodd"/>
                  </svg>
                  <span>Refresh</span>
                </button>
              </div>

              <div v-if="activeSessions.length === 0" class="text-center py-4 text-[var(--color-icon)]">
                <p>No active sessions found</p>
              </div>

              <div v-else class="grid grid-cols-1 gap-3">
                <div
                  v-for="session in activeSessions"
                  :key="session.id"
                  class="flex items-center justify-between p-3 border border-[var(--color-border)] rounded-lg hover:border-blue-500 transition-colors duration-150"
                  :class="{ 'border-blue-500 bg-blue-500/10': session.isCurrent }"
                >
                  <div class="flex items-center">
                    <div class="mr-3">
                      <svg class="w-6 h-6 text-[var(--color-icon)]" fill="currentColor" viewBox="0 0 20 20">
                        <path v-if="session.deviceType === 'desktop'" fill-rule="evenodd" d="M3 5a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2h-2.22l.123.489.804.804A1 1 0 0113 18H7a1 1 0 01-.707-1.707l.804-.804L7.22 15H5a2 2 0 01-2-2V5zm5.771 7H5V5h10v7H8.771z" clip-rule="evenodd"/>
                        <path v-if="session.deviceType === 'mobile'" d="M7 2a2 2 0 00-2 2v12a2 2 0 002 2h6a2 2 0 002-2V4a2 2 0 00-2-2H7zM7 4h6v12H7V4z"/>
                        <path v-if="session.deviceType === 'tablet'" d="M2 5a2 2 0 012-2h12a2 2 0 012 2v10a2 2 0 01-2 2H4a2 2 0 01-2-2V5zm3.293 1.293a1 1 0 011.414 0l3 3a1 1 0 010 1.414l-3 3a1 1 0 01-1.414-1.414L7.586 10 5.293 7.707a1 1 0 010-1.414zM11 12a1 1 0 100 2h3a1 1 0 100-2h-3z"/>
                      </svg>
                    </div>
                    <div>
                      <p class="font-medium text-white">{{ session.deviceName }}</p>
                      <p class="text-xs text-[var(--color-icon)]">
                        {{ session.browser }} • {{ session.location }}
                      </p>
                      <p class="text-xs text-gray-500">
                        Last active: {{ formatTimeAgo(session.lastActive) }}
                      </p>
                    </div>
                  </div>
                  <div class="flex items-center gap-2">
                    <span
                      v-if="session.isCurrent"
                      class="px-2 py-1 text-xs bg-blue-500/20 text-blue-400 rounded-full"
                    >
                      Current
                    </span>
                    <button
                      v-if="!session.isCurrent"
                      @click="terminateSession(session.id)"
                      class="p-2 text-[var(--color-icon)] hover:text-red-500 hover:bg-red-500/10 rounded-lg transition-all duration-150"
                    >
                      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"/>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>

              <div class="mt-4 text-right">
                <button
                  @click="terminateAllSessions"
                  class="px-4 py-2 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-red-500/10 hover:text-red-400 hover:border-red-500/30 transition-colors duration-150 flex items-center gap-2"
                >
                  <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"/>
                  </svg>
                  Terminate All Other Sessions
                </button>
              </div>
            </div>
          </div>
        </Card>

        <!-- Notification Preferences -->
        <Card class="border border-[var(--color-border)] bg-[var(--color-card)] p-6">
          <h2 class="text-xl font-semibold text-white mb-6">Notification Preferences</h2>
          <div class="gap-6">
            <div v-for="category in notificationPreferences" :key="category.id" class="mb-6 last:mb-0">
              <h3 class="text-lg font-medium text-white mb-4">{{ category.name }}</h3>
              <div class="grid grid-cols-1 gap-4">
                <div
                  v-for="pref in category.preferences"
                  :key="pref.id"
                  class="flex items-center justify-between py-3 border-b border-[var(--color-border)] last:border-b-0"
                >
                  <div>
                    <label class="font-medium text-white">{{ pref.name }}</label>
                    <p class="text-sm text-[var(--color-icon)]">{{ pref.description }}</p>
                  </div>
                  <div class="flex items-center gap-4">
                    <label class="flex items-center gap-2">
                      <input
                        type="checkbox"
                        v-model="pref.email"
                        class="rounded text-blue-500 bg-transparent border-[var(--color-border)]"
                      />
                      <span class="text-sm text-[var(--color-icon)]">Email</span>
                    </label>
                    <label class="flex items-center gap-2">
                      <input
                        type="checkbox"
                        v-model="pref.push"
                        class="rounded text-blue-500 bg-transparent border-[var(--color-border)]"
                      />
                      <span class="text-sm text-[var(--color-icon)]">Push</span>
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div class="flex justify-end pt-6 border-t border-[var(--color-border)]">
              <button
                @click="saveNotificationPreferences"
                :disabled="savingNotifications"
                class="px-6 py-2.5 bg-[var(--primary-color)] text-white rounded-lg hover:bg-blue-700 transition-colors duration-150 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
              >
                <svg v-if="savingNotifications" class="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <span>{{ savingNotifications ? 'Saving...' : 'Save Preferences' }}</span>
              </button>
            </div>
          </div>
        </Card>
      </div>
    </div>

    <!-- Delete Account Modal -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg w-full max-w-md p-6">
        <div class="flex items-start mb-4">
          <div class="flex-shrink-0">
            <svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
            </svg>
          </div>
          <div class="ml-3">
            <h3 class="text-lg font-medium text-white">Are you sure you want to delete your account?</h3>
            <div class="mt-2 text-sm text-[var(--color-icon)]">
              <p>This action cannot be undone. All of your data will be permanently removed from our servers.</p>
              <p class="mt-2 font-medium text-white">Please type "DELETE" to confirm:</p>
            </div>
          </div>
        </div>

        <div class="mb-4">
          <input
            type="text"
            v-model="deleteConfirmation"
            placeholder="Type DELETE here"
            class="w-full px-4 py-2.5 border border-[var(--color-border)] rounded-lg bg-transparent text-white focus:ring-2 focus:ring-red-500 focus:border-red-500 placeholder:text-gray-500"
            :class="{ 'border-red-500': deleteError }"
          />
          <p v-if="deleteError" class="mt-1 text-sm text-red-500">{{ deleteError }}</p>
        </div>

        <div class="flex justify-end gap-4">
          <button
            @click="showDeleteModal = false"
            class="px-6 py-2.5 border border-[var(--color-border)] rounded-lg text-[var(--color-icon)] hover:bg-[var(--color-hover)] transition-colors duration-150"
          >
            Cancel
          </button>
          <button
            :disabled="deleteConfirmation !== 'DELETE'"
            @click="deleteAccount"
            class="px-6 py-2.5 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors duration-150 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Delete Account
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import { formatCurrency, formatDate, formatTimeAgo, getInitials } from '@/utils/formatters';

const router = useRouter();

// User Profile Data
const userProfile = reactive({
  name: 'Alex Johnson',
  email: 'alex.johnson@example.com',
  phone: '+1 (555) 123-4567',
  location: 'San Francisco, CA',
  bio: 'Store administrator with 5+ years of experience in e-commerce management.',
  role: 'Administrator',
  avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop',
  createdAt: '2023-01-15T10:30:00Z',
  twoFactorEnabled: true,
  twoFactorLastUsed: '2024-01-10T14:30:00Z'
});

const userStats = reactive({
  orders: 42,
  revenue: 12500,
  customers: 128
});

// Personal Info Form
const editingPersonalInfo = ref(false);
const savingPersonalInfo = ref(false);
const personalInfoForm = reactive({
  name: '',
  email: '',
  phone: '',
  location: '',
  bio: ''
});

// Password Change
const showChangePassword = ref(false);
const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);
const changingPassword = ref(false);
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});
const passwordError = ref('');

// Active Sessions
const activeSessions = ref([
  {
    id: 1,
    deviceType: 'desktop',
    deviceName: 'MacBook Pro',
    browser: 'Chrome 120',
    location: 'San Francisco, CA',
    lastActive: '2024-01-15T14:30:00Z',
    isCurrent: true
  },
  {
    id: 2,
    deviceType: 'mobile',
    deviceName: 'iPhone 15',
    browser: 'Safari 17',
    location: 'San Francisco, CA',
    lastActive: '2024-01-14T09:15:00Z',
    isCurrent: false
  },
  {
    id: 3,
    deviceType: 'tablet',
    deviceName: 'iPad Pro',
    browser: 'Safari 17',
    location: 'New York, NY',
    lastActive: '2024-01-12T16:45:00Z',
    isCurrent: false
  }
]);

// Notification Preferences
const savingNotifications = ref(false);
const notificationPreferences = ref([
  {
    id: 1,
    name: 'Order Notifications',
    preferences: [
      {
        id: 1,
        name: 'New Orders',
        description: 'When a customer places a new order',
        email: true,
        push: true
      },
      {
        id: 2,
        name: 'Order Updates',
        description: 'When an order status changes',
        email: true,
        push: false
      },
      {
        id: 3,
        name: 'Cancellations',
        description: 'When an order is cancelled',
        email: true,
        push: true
      }
    ]
  },
  {
    id: 2,
    name: 'Product Notifications',
    preferences: [
      {
        id: 4,
        name: 'Low Stock',
        description: 'When product inventory is running low',
        email: true,
        push: false
      },
      {
        id: 5,
        name: 'Out of Stock',
        description: 'When products go out of stock',
        email: true,
        push: true
      }
    ]
  },
  {
    id: 3,
    name: 'Customer Notifications',
    preferences: [
      {
        id: 6,
        name: 'New Customers',
        description: 'When new customers register',
        email: true,
        push: false
      },
      {
        id: 7,
        name: 'Reviews',
        description: 'When customers leave reviews',
        email: true,
        push: true
      }
    ]
  }
]);

// Delete Account
const showDeleteModal = ref(false);
const deleteConfirmation = ref('');
const deleteError = ref('');

// File upload refs
const avatarInput = ref<HTMLInputElement | null>(null);

// Helper Functions
const getUserInitials = (name: string) => {
  return getInitials(name);
};

const getDeviceIcon = (deviceType: string) => {
  switch (deviceType) {
    case 'desktop': return 'fas fa-desktop';
    case 'mobile': return 'fas fa-mobile-alt';
    case 'tablet': return 'fas fa-tablet-alt';
    default: return 'fas fa-laptop';
  }
};

// Methods
const triggerAvatarUpload = () => {
  avatarInput.value?.click();
};

const handleAvatarUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    const url = URL.createObjectURL(file);
    userProfile.avatar = url;
    alert('Profile picture updated successfully!');
  }
};

const editPersonalInfo = () => {
  editingPersonalInfo.value = true;
  Object.assign(personalInfoForm, {
    name: userProfile.name,
    email: userProfile.email,
    phone: userProfile.phone,
    location: userProfile.location,
    bio: userProfile.bio
  });
};

const cancelEditPersonalInfo = () => {
  editingPersonalInfo.value = false;
};

const savePersonalInfo = async () => {
  savingPersonalInfo.value = true;

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 800));

  // Update user profile
  Object.assign(userProfile, personalInfoForm);
  editingPersonalInfo.value = false;
  savingPersonalInfo.value = false;

  alert('Personal information updated successfully!');
};

const changePassword = async () => {
  // Validate passwords
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    passwordError.value = 'New passwords do not match';
    return;
  }

  if (passwordForm.newPassword.length < 8) {
    passwordError.value = 'Password must be at least 8 characters long';
    return;
  }

  changingPassword.value = true;

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 1000));

  // Reset form
  passwordForm.currentPassword = '';
  passwordForm.newPassword = '';
  passwordForm.confirmPassword = '';
  passwordError.value = '';
  showChangePassword.value = false;
  changingPassword.value = false;

  alert('Password changed successfully!');
};

const toggleTwoFactor = async () => {
  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 500));

  if (userProfile.twoFactorEnabled) {
    alert('Two-factor authentication has been enabled.');
    userProfile.twoFactorLastUsed = new Date().toISOString();
  } else {
    alert('Two-factor authentication has been disabled.');
  }
};

const refreshSessions = async () => {
  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 500));
  alert('Sessions refreshed!');
};

const terminateSession = (sessionId: number) => {
  if (confirm('Are you sure you want to terminate this session?')) {
    activeSessions.value = activeSessions.value.filter(session => session.id !== sessionId);
    alert('Session terminated successfully!');
  }
};

const terminateAllSessions = () => {
  if (confirm('Are you sure you want to terminate all other sessions?')) {
    activeSessions.value = activeSessions.value.filter(session => session.isCurrent);
    alert('All other sessions have been terminated!');
  }
};

const saveNotificationPreferences = async () => {
  savingNotifications.value = true;

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 800));

  savingNotifications.value = false;
  alert('Notification preferences saved successfully!');
};

const viewOrders = () => {
  router.push('/orders');
};

const viewActivity = () => {
  router.push('/activity');
};

const downloadData = () => {
  // Create a JSON file with user data
  const data = {
    profile: userProfile,
    stats: userStats,
    sessions: activeSessions.value,
    preferences: notificationPreferences.value
  };

  const dataStr = JSON.stringify(data, null, 2);
  const dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);
  const exportFileDefaultName = `user_data_${new Date().toISOString().split('T')[0]}.json`;

  const linkElement = document.createElement('a');
  linkElement.setAttribute('href', dataUri);
  linkElement.setAttribute('download', exportFileDefaultName);
  linkElement.click();

  alert('Data download started!');
};

const deleteAccount = async () => {
  if (deleteConfirmation.value !== 'DELETE') {
    deleteError.value = 'Please type "DELETE" exactly as shown';
    return;
  }

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 1500));

  showDeleteModal.value = false;
  deleteConfirmation.value = '';
  deleteError.value = '';

  alert('Account deletion initiated. You will be logged out shortly.');
  // In a real app, you would redirect to logout/login page
  // router.push('/login');
};

onMounted(() => {
  console.log('UserProfile component mounted');
});
</script>

<style scoped>
/* Custom scrollbar */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) transparent;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: transparent;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 3px;
}

/* Input focus states */
input:focus, textarea:focus, select:focus {
  outline: none;
}

/* Checkbox styling */
input[type="checkbox"] {
  background-color: transparent;
  border: 1px solid var(--color-border);
}

input[type="checkbox"]:checked {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

/* Button transitions */
button {
  transition: all 0.15s ease-out;
}

button:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Avatar hover effect */
.relative:hover .absolute {
  transform: scale(1.1);
}

/* Smooth transitions */
* {
  transition: background-color 0.15s ease-out, border-color 0.15s ease-out, color 0.15s ease-out;
}

/* Modal backdrop */
.fixed {
  z-index: 50;
}

/* Gradient backgrounds */
.bg-gradient-to-br {
  background-size: 200% 200%;
  animation: gradient 15s ease infinite;
}

@keyframes gradient {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>
