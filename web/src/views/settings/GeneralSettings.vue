<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="pb-5">
      <h1 class="page-title">Settings</h1>
      <p class="text-[var(--text-secondary)] mt-1">Manage your store settings and preferences</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
      <!-- Settings Sidebar -->
      <div class="lg:col-span-1">
        <Card class="sticky top-6 border border-[var(--glass-border)] p-2 bg-[var(--glass-bg)]">
          <nav class="grid grid-cols-1 gap-2">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="activeTab = tab.id"
              class="w-full text-left px-4 py-3 rounded-lg transition-all duration-150 flex items-center group"
              :class="activeTab === tab.id
                ? 'bg-[rgba(255,255,255,0.35)] text-[var(--accent)] border-l-3 border-[var(--accent)] font-semibold'
                : 'text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] hover:text-[var(--text-primary)] hover:border-l-3 hover:border-gray-500/50'"
            >
              <i :class="tab.icon" class="mr-3 w-5 h-5 flex items-center justify-center"></i>
              {{ tab.label }}
            </button>
          </nav>
        </Card>
      </div>

      <!-- Settings Content -->
      <div class="lg:col-span-3">
        <Card class="p-6 border border-[var(--glass-border)] bg-[var(--glass-bg)]">
          <!-- Store Settings -->
          <div v-if="activeTab === 'store'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-[var(--text-primary)] mb-6">Store Information</h2>
              <div class="gap-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Store Name *
                    </label>
                    <input
                      type="text"
                      v-model="storeSettings.name"
                      required
                      class="w-full px-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
                      placeholder="Enter store name"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Store Email *
                    </label>
                    <input
                      type="email"
                      v-model="storeSettings.email"
                      required
                      class="w-full px-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
                      placeholder="Enter store email"
                    />
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Phone Number
                    </label>
                    <input
                      type="tel"
                      v-model="storeSettings.phone"
                      class="w-full px-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
                      placeholder="Enter phone number"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Store Currency *
                    </label>
                    <div class="relative">
                      <select
                        v-model="storeSettings.currency"
                        class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                      >
                        <option value="USD">USD ($)</option>
                        <option value="EUR">EUR (€)</option>
                        <option value="GBP">GBP (£)</option>
                        <option value="JPY">JPY (¥)</option>
                      </select>
                      <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                    Store Address
                  </label>
                  <textarea
                    v-model="storeSettings.address"
                    rows="3"
                    class="w-full px-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
                    placeholder="Enter store address"
                  ></textarea>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Timezone *
                    </label>
                    <div class="relative">
                      <select
                        v-model="storeSettings.timezone"
                        class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                      >
                        <option value="UTC">UTC</option>
                        <option value="America/New_York">Eastern Time</option>
                        <option value="America/Chicago">Central Time</option>
                        <option value="America/Denver">Mountain Time</option>
                        <option value="America/Los_Angeles">Pacific Time</option>
                      </select>
                      <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-[var(--text-secondary)] mb-2">
                      Date Format *
                    </label>
                    <div class="relative">
                      <select
                        v-model="storeSettings.dateFormat"
                        class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                      >
                        <option value="MM/DD/YYYY">MM/DD/YYYY</option>
                        <option value="DD/MM/YYYY">DD/MM/YYYY</option>
                        <option value="YYYY-MM-DD">YYYY-MM-DD</option>
                      </select>
                      <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="pt-8 border-t border-[var(--glass-border)]">
              <h3 class="text-lg font-semibold text-[var(--text-primary)] mb-6">Store Logo & Branding</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div>
                  <label class="block text-sm font-medium text-[var(--text-secondary)] mb-3">
                    Store Logo
                  </label>
                  <div
                    class="border-2 border-dashed border-[var(--glass-border)] rounded-lg p-6 text-center hover:border-[var(--accent)] transition-colors duration-150 cursor-pointer"
                    @dragover.prevent
                    @drop="handleLogoDrop"
                    @click="triggerLogoUpload"
                  >
                    <div v-if="storeSettings.logo" class="mb-4">
                      <img
                        :src="storeSettings.logo"
                        alt="Store Logo"
                        class="h-32 mx-auto object-contain rounded-lg"
                      />
                    </div>
                    <div v-else class="mb-4">
                      <svg class="w-16 h-16 mx-auto text-[var(--text-secondary)]" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                    <div class="mb-4">
                      <p class="text-sm text-[var(--text-secondary)] mb-2">
                        Upload your store logo
                      </p>
                      <p class="text-xs text-[var(--text-muted)]">
                        PNG, JPG up to 2MB
                      </p>
                    </div>
                    <div>
                      <button
                        class="px-4 py-2 text-sm border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                      >
                        Choose Image
                      </button>
                    </div>
                    <input
                      ref="logoInput"
                      type="file"
                      accept="image/*"
                      class="hidden"
                      @change="handleLogoUpload"
                    />
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-[var(--text-secondary)] mb-3">
                    Favicon
                  </label>
                  <div
                    class="border-2 border-dashed border-[var(--glass-border)] rounded-lg p-6 text-center hover:border-[var(--accent)] transition-colors duration-150 cursor-pointer"
                    @dragover.prevent
                    @drop="handleFaviconDrop"
                    @click="triggerFaviconUpload"
                  >
                    <div v-if="storeSettings.favicon" class="mb-4">
                      <img
                        :src="storeSettings.favicon"
                        alt="Favicon"
                        class="h-16 mx-auto object-contain rounded"
                      />
                    </div>
                    <div v-else class="mb-4">
                      <svg class="w-12 h-12 mx-auto text-[var(--text-secondary)]" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                        <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                    <div class="mb-4">
                      <p class="text-sm text-[var(--text-secondary)] mb-2">
                        Upload your favicon
                      </p>
                      <p class="text-xs text-[var(--text-muted)]">
                        ICO, PNG up to 100KB
                      </p>
                    </div>
                    <div>
                      <button
                        class="px-4 py-2 text-sm border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                      >
                        Choose Image
                      </button>
                    </div>
                    <input
                      ref="faviconInput"
                      type="file"
                      accept="image/*"
                      class="hidden"
                      @change="handleFaviconUpload"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Shipping Settings -->
          <div v-else-if="activeTab === 'shipping'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-[var(--text-primary)] mb-6">Shipping Settings</h2>
              <div class="gap-6">
                <div class="flex items-center justify-between mb-4">
                  <div>
                    <h3 class="text-lg font-medium text-[var(--text-primary)]">Shipping Zones</h3>
                    <p class="text-[var(--text-secondary)] text-sm">Configure shipping rates for different regions</p>
                  </div>
                  <button
                    @click="addShippingZone"
                    class="btn-accent"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
                    </svg>
                    <span>Add Zone</span>
                  </button>
                </div>

                <div v-if="shippingZones.length === 0" class="text-center py-8 border-2 border-dashed border-[var(--glass-border)] rounded-lg">
                  <svg class="w-16 h-16 mx-auto text-[var(--text-secondary)] mb-3" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M8 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM15 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0z"/>
                    <path d="M3 4a1 1 0 00-1 1v10a1 1 0 001 1h1.05a2.5 2.5 0 014.9 0H10a1 1 0 001-1v-1h4a1 1 0 001-1v-1h2a1 1 0 001-1v-6a1 1 0 00-1-1H3zM14 7h-3v2h3V7z"/>
                  </svg>
                  <p class="text-[var(--text-secondary)] mb-4">No shipping zones configured</p>
                  <button
                    @click="addShippingZone"
                    class="px-4 py-2 border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                  >
                    Create Your First Shipping Zone
                  </button>
                </div>

                <div v-else class="gap-4">
                  <div
                    v-for="zone in shippingZones"
                    :key="zone.id"
                    class="border border-[var(--glass-border)] rounded-lg p-4 hover:border-[var(--accent)] transition-colors duration-150"
                  >
                    <div class="flex items-center justify-between mb-3">
                      <h4 class="font-medium text-[var(--text-primary)]">{{ zone.name }}</h4>
                      <div class="flex gap-2">
                        <button
                          @click="editShippingZone(zone)"
                          class="btn-glass-icon"
                        >
                          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                          </svg>
                        </button>
                        <button
                          @click="deleteShippingZone(zone)"
                          class="p-2 text-[var(--text-secondary)] hover:text-red-500 hover:bg-red-500/10 rounded-lg transition-all duration-150"
                        >
                          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"/>
                          </svg>
                        </button>
                      </div>
                    </div>
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                      <div>
                        <span class="text-[var(--text-secondary)]">Countries:</span>
                        <span class="ml-2 text-[var(--text-primary)]">{{ zone.countries.length }}</span>
                      </div>
                      <div>
                        <span class="text-[var(--text-secondary)]">Methods:</span>
                        <span class="ml-2 text-[var(--text-primary)]">{{ zone.methods.length }}</span>
                      </div>
                      <div>
                        <span class="text-[var(--text-secondary)]">Status:</span>
                        <span class="ml-2">
                          <span
                            class="px-2 py-0.5 text-xs rounded-full"
                            :class="zone.enabled ? 'bg-green-500/20 text-green-400' : 'bg-yellow-500/20 text-yellow-400'"
                          >
                            {{ zone.enabled ? 'Enabled' : 'Disabled' }}
                          </span>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="border-t border-[var(--glass-border)] pt-8">
              <h3 class="text-lg font-semibold text-[var(--text-primary)] mb-6">Shipping Options</h3>
              <div class="gap-4">
                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Free Shipping Threshold</label>
                    <p class="text-sm text-[var(--text-secondary)]">
                      Offer free shipping on orders above this amount
                    </p>
                  </div>
                  <div class="w-48">
                    <div class="relative">
                      <span class="absolute left-3 top-2 text-[var(--text-secondary)]">
                        {{ storeSettings.currencySymbol }}
                      </span>
                      <input
                        type="number"
                        v-model="shippingSettings.freeShippingThreshold"
                        min="0"
                        step="0.01"
                        class="w-full pl-8 pr-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent"
                      />
                    </div>
                  </div>
                </div>

                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Shipping Weight Unit</label>
                    <p class="text-sm text-[var(--text-secondary)]">Unit used for shipping weight calculations</p>
                  </div>
                  <div class="relative w-48">
                    <select
                      v-model="shippingSettings.weightUnit"
                      class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                    >
                      <option value="kg">Kilograms (kg)</option>
                      <option value="lb">Pounds (lb)</option>
                      <option value="oz">Ounces (oz)</option>
                    </select>
                    <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                    </svg>
                  </div>
                </div>

                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Enable Shipping Calculator</label>
                    <p class="text-sm text-[var(--text-secondary)]">Show real-time shipping rates at checkout</p>
                  </div>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      v-model="shippingSettings.enableCalculator"
                      class="sr-only peer"
                    />
                    <div class="w-11 h-6 bg-[var(--glass-bg)] peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[var(--accent)]"></div>
                  </label>
                </div>
              </div>
            </div>
          </div>

          <!-- Tax Settings -->
          <div v-else-if="activeTab === 'tax'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-[var(--text-primary)] mb-6">Tax Settings</h2>
              <div class="gap-6">
                <div class="flex items-center justify-between mb-4">
                  <div>
                    <h3 class="text-lg font-medium text-[var(--text-primary)]">Tax Rates</h3>
                    <p class="text-[var(--text-secondary)] text-sm">Configure tax rates for different regions</p>
                  </div>
                  <button
                    @click="addTaxRate"
                    class="btn-accent"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
                    </svg>
                    <span>Add Tax Rate</span>
                  </button>
                </div>

                <div v-if="taxRates.length === 0" class="text-center py-8 border-2 border-dashed border-[var(--glass-border)] rounded-lg">
                  <svg class="w-16 h-16 mx-auto text-[var(--text-secondary)] mb-3" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/>
                  </svg>
                  <p class="text-[var(--text-secondary)] mb-4">No tax rates configured</p>
                  <button
                    @click="addTaxRate"
                    class="px-4 py-2 border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                  >
                    Add Your First Tax Rate
                  </button>
                </div>

                <div v-else>
                  <div class="overflow-x-auto">
                    <table class="w-full">
                      <thead>
                        <tr class="bg-[var(--glass-bg)] text-[var(--text-secondary)]">
                          <th class="py-3 px-4 text-left font-semibold text-sm">Country/Region</th>
                          <th class="py-3 px-4 text-left font-semibold text-sm">Tax Name</th>
                          <th class="py-3 px-4 text-left font-semibold text-sm">Rate</th>
                          <th class="py-3 px-4 text-left font-semibold text-sm">Type</th>
                          <th class="py-3 px-4 text-left font-semibold text-sm">Status</th>
                          <th class="py-3 px-4 text-left font-semibold text-sm">Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr
                          v-for="rate in taxRates"
                          :key="rate.id"
                          class="hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150 border-b border-[var(--glass-border)] last:border-b-0"
                        >
                          <td class="py-3 px-4 text-[var(--text-primary)]">{{ rate.country }}</td>
                          <td class="py-3 px-4 text-[var(--text-primary)]">{{ rate.name }}</td>
                          <td class="py-3 px-4 text-[var(--text-primary)]">{{ rate.rate }}%</td>
                          <td class="py-3 px-4">
                            <span
                              class="px-2 py-1 text-xs rounded-full"
                              :class="rate.type === 'inclusive' ? 'bg-green-500/20 text-green-400' : 'bg-blue-500/20 text-[var(--text-accent)]'"
                            >
                              {{ rate.type }}
                            </span>
                          </td>
                          <td class="py-3 px-4">
                            <span
                              class="px-2 py-1 text-xs rounded-full"
                              :class="rate.enabled ? 'bg-green-500/20 text-green-400' : 'bg-yellow-500/20 text-yellow-400'"
                            >
                              {{ rate.enabled ? 'Active' : 'Inactive' }}
                            </span>
                          </td>
                          <td class="py-3 px-4">
                            <div class="flex gap-2">
                              <button
                                @click="editTaxRate(rate)"
                                class="btn-glass-icon"
                              >
                                <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                  <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                                </svg>
                              </button>
                              <button
                                @click="deleteTaxRate(rate)"
                                class="p-2 text-[var(--text-secondary)] hover:text-red-500 hover:bg-red-500/10 rounded-lg transition-all duration-150"
                              >
                                <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                  <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"/>
                                </svg>
                              </button>
                            </div>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>

            <div class="border-t border-[var(--glass-border)] pt-8">
              <h3 class="text-lg font-semibold text-[var(--text-primary)] mb-6">Tax Configuration</h3>
              <div class="gap-4">
                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Tax Calculation Method</label>
                    <p class="text-sm text-[var(--text-secondary)]">How taxes are calculated on orders</p>
                  </div>
                  <div class="relative w-64">
                    <select
                      v-model="taxSettings.calculationMethod"
                      class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                    >
                      <option value="exclusive">Exclusive (Add tax to price)</option>
                      <option value="inclusive">Inclusive (Tax included in price)</option>
                    </select>
                    <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                    </svg>
                  </div>
                </div>

                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Show Prices With Tax</label>
                    <p class="text-sm text-[var(--text-secondary)]">Display prices including tax to customers</p>
                  </div>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      v-model="taxSettings.showPricesWithTax"
                      class="sr-only peer"
                    />
                    <div class="w-11 h-6 bg-[var(--glass-bg)] peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[var(--accent)]"></div>
                  </label>
                </div>

                <div class="flex items-center justify-between py-3">
                  <div>
                    <label class="font-medium text-[var(--text-primary)]">Tax ID/Number</label>
                    <p class="text-sm text-[var(--text-secondary)]">Your business tax identification number</p>
                  </div>
                  <input
                    type="text"
                    v-model="taxSettings.taxId"
                    class="w-64 px-4 py-2.5 border border-[var(--glass-border)] rounded-lg bg-transparent text-[var(--text-primary)] focus:ring-2 focus:ring-accent focus:border-accent"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- Notifications -->
          <div v-else-if="activeTab === 'notifications'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-[var(--text-primary)] mb-6">Notification Settings</h2>
              <div class="gap-6">
                <div>
                  <h3 class="text-lg font-medium text-[var(--text-primary)] mb-4">Email Notifications</h3>
                  <div class="gap-4">
                    <div
                      v-for="notification in emailNotifications"
                      :key="notification.id"
                      class="flex items-center justify-between py-3 border-b border-[var(--glass-border)] last:border-b-0"
                    >
                      <div>
                        <label class="font-medium text-[var(--text-primary)]">{{ notification.name }}</label>
                        <p class="text-sm text-[var(--text-secondary)]">{{ notification.description }}</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          v-model="notification.enabled"
                          class="sr-only peer"
                        />
                        <div class="w-11 h-6 bg-[var(--glass-bg)] peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[var(--accent)]"></div>
                      </label>
                    </div>
                  </div>
                </div>

                <div class="border-t border-[var(--glass-border)] pt-8">
                  <h3 class="text-lg font-medium text-[var(--text-primary)] mb-4">Push Notifications</h3>
                  <div class="gap-4">
                    <div class="flex items-center justify-between py-3">
                      <div>
                        <label class="font-medium text-[var(--text-primary)]">Enable Browser Notifications</label>
                        <p class="text-sm text-[var(--text-secondary)]">Receive notifications in your browser</p>
                      </div>
                      <button
                        @click="requestNotificationPermission"
                        class="px-4 py-2 border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                      >
                        Request Permission
                      </button>
                    </div>
                  </div>
                </div>

                <div class="border-t border-[var(--glass-border)] pt-8">
                  <h3 class="text-lg font-medium text-[var(--text-primary)] mb-4">Notification Preferences</h3>
                  <div class="gap-4">
                    <div class="flex items-center justify-between py-3">
                      <div>
                        <label class="font-medium text-[var(--text-primary)]">Notification Frequency</label>
                        <p class="text-sm text-[var(--text-secondary)]">How often you receive notifications</p>
                      </div>
                      <div class="relative w-48">
                        <select
                          v-model="notificationSettings.frequency"
                          class="appearance-none text-[var(--text-primary)] border border-[var(--glass-border)] rounded-lg px-4 py-2.5 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-transparent w-full"
                        >
                          <option value="realtime">Real-time</option>
                          <option value="hourly">Hourly Digest</option>
                          <option value="daily">Daily Digest</option>
                        </select>
                        <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--text-secondary)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                          <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
                        </svg>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Save Button -->
          <div class="flex justify-end gap-4 pt-6 mt-6 border-t border-[var(--glass-border)]">
            <button
              type="button"
              @click="resetSettings"
              class="px-6 py-2.5 border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
            >
              Reset
            </button>
            <button
              type="button"
              @click="saveSettings"
              :disabled="saving"
              class="btn-accent disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg v-if="saving" class="animate-spin h-4 w-4 text-[var(--text-primary)]" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>{{ saving ? 'Saving...' : 'Save Changes' }}</span>
            </button>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import Card from '@/components/ui/Card.vue';
import Button from '@/components/ui/Button.vue';
import Badge from '@/components/ui/Badge.vue';
import Table from '@/components/ui/Table.vue';
import Modal from '@/components/ui/Modal.vue';

// Tabs configuration
const tabs = [
  { id: 'store', label: 'Store', icon: 'fas fa-store' },
  { id: 'shipping', label: 'Shipping', icon: 'fas fa-truck' },
  { id: 'tax', label: 'Tax', icon: 'fas fa-percentage' },
  { id: 'notifications', label: 'Notifications', icon: 'fas fa-bell' },
];

const activeTab = ref('store');
const saving = ref(false);

// Store Settings
const storeSettings = reactive({
  name: 'My Awesome Store',
  email: 'contact@myawesomestore.com',
  phone: '+1 (555) 123-4567',
  address: '123 Commerce Street\nSuite 456\nSan Francisco, CA 94107',
  currency: 'USD',
  currencySymbol: '$',
  timezone: 'America/New_York',
  dateFormat: 'MM/DD/YYYY',
  logo: 'https://via.placeholder.com/200x100?text=Store+Logo',
  favicon: 'https://via.placeholder.com/32x32?text=F',
});

// Shipping Settings
const shippingSettings = reactive({
  freeShippingThreshold: 50.00,
  weightUnit: 'kg',
  enableCalculator: true,
});

const shippingZones = ref([
  {
    id: 1,
    name: 'United States',
    countries: ['US'],
    methods: ['Standard', 'Express', 'Overnight'],
    enabled: true,
  },
  {
    id: 2,
    name: 'Europe',
    countries: ['GB', 'DE', 'FR', 'ES', 'IT'],
    methods: ['Standard', 'Express'],
    enabled: true,
  },
  {
    id: 3,
    name: 'Canada',
    countries: ['CA'],
    methods: ['Standard'],
    enabled: false,
  },
]);

const showShippingZoneModal = ref(false);
const editingShippingZone = ref<any>(null);
const editingShippingZoneData = reactive({
  name: '',
  countries: [] as string[],
  methods: [] as string[],
  enabled: true,
});

// Tax Settings
const taxSettings = reactive({
  calculationMethod: 'exclusive',
  showPricesWithTax: false,
  taxId: 'TAX-123456789',
});

const taxRates = ref([
  {
    id: 1,
    country: 'United States',
    name: 'Sales Tax',
    rate: 8.25,
    type: 'exclusive',
    enabled: true,
  },
  {
    id: 2,
    country: 'United Kingdom',
    name: 'VAT',
    rate: 20,
    type: 'inclusive',
    enabled: true,
  },
  {
    id: 3,
    country: 'Canada',
    name: 'GST/HST',
    rate: 13,
    type: 'exclusive',
    enabled: true,
  },
  {
    id: 4,
    country: 'Germany',
    name: 'VAT',
    rate: 19,
    type: 'inclusive',
    enabled: false,
  },
]);

const showTaxRateModal = ref(false);
const editingTaxRate = ref<any>(null);
const editingTaxRateData = reactive({
  country: '',
  name: '',
  rate: 0,
  type: 'exclusive',
  enabled: true,
});

// Notification Settings
const notificationSettings = reactive({
  frequency: 'realtime',
});

const emailNotifications = ref([
  {
    id: 1,
    name: 'New Order Notifications',
    description: 'Receive email when a new order is placed',
    enabled: true,
  },
  {
    id: 2,
    name: 'Order Status Updates',
    description: 'Get notified when order status changes',
    enabled: true,
  },
  {
    id: 3,
    name: 'Low Stock Alerts',
    description: 'Receive alerts when inventory is low',
    enabled: true,
  },
  {
    id: 4,
    name: 'New Customer Registrations',
    description: 'Get notified when new customers sign up',
    enabled: false,
  },
  {
    id: 5,
    name: 'Monthly Reports',
    description: 'Receive monthly sales and performance reports',
    enabled: true,
  },
  {
    id: 6,
    name: 'Marketing Campaigns',
    description: 'Updates about marketing campaigns and promotions',
    enabled: false,
  },
]);

// File upload refs
const logoInput = ref<HTMLInputElement | null>(null);
const faviconInput = ref<HTMLInputElement | null>(null);

// Methods
const triggerLogoUpload = () => {
  logoInput.value?.click();
};

const triggerFaviconUpload = () => {
  faviconInput.value?.click();
};

const handleLogoUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    // In a real app, you would upload to server and get URL
    // For mock, create a local URL
    const url = URL.createObjectURL(file);
    storeSettings.logo = url;
  }
};

const handleFaviconUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    const url = URL.createObjectURL(file);
    storeSettings.favicon = url;
  }
};

const handleLogoDrop = (event: DragEvent) => {
  event.preventDefault();
  if (event.dataTransfer?.files[0]) {
    const file = event.dataTransfer.files[0];
    const url = URL.createObjectURL(file);
    storeSettings.logo = url;
  }
};

const handleFaviconDrop = (event: DragEvent) => {
  event.preventDefault();
  if (event.dataTransfer?.files[0]) {
    const file = event.dataTransfer.files[0];
    const url = URL.createObjectURL(file);
    storeSettings.favicon = url;
  }
};

// Shipping Zone Methods
const addShippingZone = () => {
  editingShippingZone.value = null;
  editingShippingZoneData.name = '';
  editingShippingZoneData.countries = [];
  editingShippingZoneData.methods = [];
  editingShippingZoneData.enabled = true;
  showShippingZoneModal.value = true;
};

const editShippingZone = (zone: any) => {
  editingShippingZone.value = zone;
  Object.assign(editingShippingZoneData, zone);
  showShippingZoneModal.value = true;
};

const deleteShippingZone = (zone: any) => {
  if (confirm(`Are you sure you want to delete the shipping zone "${zone.name}"?`)) {
    const index = shippingZones.value.findIndex(z => z.id === zone.id);
    if (index !== -1) {
      shippingZones.value.splice(index, 1);
    }
  }
};

const closeShippingZoneModal = () => {
  showShippingZoneModal.value = false;
  editingShippingZone.value = null;
};

// Tax Rate Methods
const addTaxRate = () => {
  editingTaxRate.value = null;
  editingTaxRateData.country = '';
  editingTaxRateData.name = '';
  editingTaxRateData.rate = 0;
  editingTaxRateData.type = 'exclusive';
  editingTaxRateData.enabled = true;
  showTaxRateModal.value = true;
};

const editTaxRate = (rate: any) => {
  editingTaxRate.value = rate;
  Object.assign(editingTaxRateData, rate);
  showTaxRateModal.value = true;
};

const deleteTaxRate = (rate: any) => {
  if (confirm(`Are you sure you want to delete the tax rate for "${rate.country}"?`)) {
    const index = taxRates.value.findIndex(r => r.id === rate.id);
    if (index !== -1) {
      taxRates.value.splice(index, 1);
    }
  }
};

const closeTaxRateModal = () => {
  showTaxRateModal.value = false;
  editingTaxRate.value = null;
};

// Notification Methods
const requestNotificationPermission = () => {
  if ('Notification' in window) {
    Notification.requestPermission().then(permission => {
      if (permission === 'granted') {
        alert('Notification permission granted!');
      } else {
        alert('Notification permission denied.');
      }
    });
  } else {
    alert('This browser does not support notifications.');
  }
};

// Save and Reset Methods
const saveSettings = async () => {
  saving.value = true;

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 1000));

  console.log('Saving settings:', {
    storeSettings,
    shippingSettings,
    shippingZones: shippingZones.value,
    taxSettings,
    taxRates: taxRates.value,
    notificationSettings,
    emailNotifications: emailNotifications.value,
  });

  saving.value = false;
  alert('Settings saved successfully!');
};

const resetSettings = () => {
  if (confirm('Are you sure you want to reset all settings to their default values?')) {
    // Reset store settings
    Object.assign(storeSettings, {
      name: 'My Awesome Store',
      email: 'contact@myawesomestore.com',
      phone: '+1 (555) 123-4567',
      address: '123 Commerce Street\nSuite 456\nSan Francisco, CA 94107',
      currency: 'USD',
      currencySymbol: '$',
      timezone: 'America/New_York',
      dateFormat: 'MM/DD/YYYY',
      logo: 'https://via.placeholder.com/200x100?text=Store+Logo',
      favicon: 'https://via.placeholder.com/32x32?text=F',
    });

    // Reset shipping settings
    Object.assign(shippingSettings, {
      freeShippingThreshold: 50.00,
      weightUnit: 'kg',
      enableCalculator: true,
    });

    // Reset tax settings
    Object.assign(taxSettings, {
      calculationMethod: 'exclusive',
      showPricesWithTax: false,
      taxId: 'TAX-123456789',
    });

    // Reset notification settings
    Object.assign(notificationSettings, {
      frequency: 'realtime',
    });

    // Reset email notifications
    emailNotifications.value.forEach(notification => {
      if ([1, 2, 3, 5].includes(notification.id)) {
        notification.enabled = true;
      } else {
        notification.enabled = false;
      }
    });

    alert('Settings have been reset to default values.');
  }
};

onMounted(() => {
  console.log('GeneralSettings component mounted');
});
</script>

<style scoped>
/* Custom scrollbar */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: var(--glass-border) transparent;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: transparent;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: var(--glass-border);
  border-radius: 3px;
}

/* Input focus states */
input:focus, textarea:focus, select:focus {
  outline: none;
}

/* Placeholder color */
::placeholder {
  color: #6b7280;
}

/* File upload hover */
.border-dashed:hover {
  border-color: var(--accent);
}

/* Checkbox styling */
input[type="checkbox"]:checked {
  background-color: var(--accent);
}

/* Table row hover */
tr:hover {
  background-color: rgba(255,255,255,0.35);
}

/* Button transitions */
button {
  transition: all 0.15s ease-out;
}

button:focus {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

/* Smooth transitions */
* {
  transition: background-color 0.15s ease-out, border-color 0.15s ease-out;
}
</style>
