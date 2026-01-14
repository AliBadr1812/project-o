<template>
  <div class="general-settings">
    <div class="mb-8">
      <h1 class="text-2xl font-bold text-gray-800">Settings</h1>
      <p class="text-gray-600 mt-1">Manage your store settings and preferences</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
      <!-- Settings Sidebar -->
      <div class="lg:col-span-1">
        <Card class="sticky top-6">
          <nav class="gap-1">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="activeTab = tab.id"
              class="w-full text-left px-4 py-3 rounded-lg transition-colors flex items-center"
              :class="activeTab === tab.id
                ? 'bg-blue-50 text-blue-700 border-r-2 border-blue-500'
                : 'text-gray-700 hover:bg-gray-50'"
            >
              <i :class="tab.icon" class="mr-3"></i>
              {{ tab.label }}
            </button>
          </nav>
        </Card>
      </div>

      <!-- Settings Content -->
      <div class="lg:col-span-3">
        <Card>
          <!-- Store Settings -->
          <div v-if="activeTab === 'store'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-gray-800 mb-6">Store Information</h2>
              <div class="gap-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Store Name *
                    </label>
                    <input
                      type="text"
                      v-model="storeSettings.name"
                      required
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Store Email *
                    </label>
                    <input
                      type="email"
                      v-model="storeSettings.email"
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
                      v-model="storeSettings.phone"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Store Currency *
                    </label>
                    <select
                      v-model="storeSettings.currency"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                      <option value="USD">USD ($)</option>
                      <option value="EUR">EUR (€)</option>
                      <option value="GBP">GBP (£)</option>
                      <option value="JPY">JPY (¥)</option>
                    </select>
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Store Address
                  </label>
                  <textarea
                    v-model="storeSettings.address"
                    rows="3"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  ></textarea>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Timezone *
                    </label>
                    <select
                      v-model="storeSettings.timezone"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                      <option value="UTC">UTC</option>
                      <option value="America/New_York">Eastern Time</option>
                      <option value="America/Chicago">Central Time</option>
                      <option value="America/Denver">Mountain Time</option>
                      <option value="America/Los_Angeles">Pacific Time</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      Date Format *
                    </label>
                    <select
                      v-model="storeSettings.dateFormat"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                      <option value="MM/DD/YYYY">MM/DD/YYYY</option>
                      <option value="DD/MM/YYYY">DD/MM/YYYY</option>
                      <option value="YYYY-MM-DD">YYYY-MM-DD</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <div class="border-t border-gray-200 pt-8">
              <h3 class="text-lg font-semibold text-gray-800 mb-6">Store Logo & Branding</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-3">
                    Store Logo
                  </label>
                  <div
                    class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center"
                    @dragover.prevent
                    @drop="handleLogoDrop"
                  >
                    <div v-if="storeSettings.logo" class="mb-4">
                      <img
                        :src="storeSettings.logo"
                        alt="Store Logo"
                        class="h-32 mx-auto object-contain"
                      />
                    </div>
                    <div v-else class="mb-4">
                      <i class="fas fa-image text-4xl text-gray-400"></i>
                    </div>
                    <div class="mb-4">
                      <p class="text-sm text-gray-600 mb-2">
                        Upload your store logo
                      </p>
                      <p class="text-xs text-gray-500">
                        PNG, JPG up to 2MB
                      </p>
                    </div>
                    <div>
                      <Button
                        variant="outline"
                        size="sm"
                        @click="triggerLogoUpload"
                      >
                        Choose Image
                      </Button>
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
                  <label class="block text-sm font-medium text-gray-700 mb-3">
                    Favicon
                  </label>
                  <div
                    class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center"
                    @dragover.prevent
                    @drop="handleFaviconDrop"
                  >
                    <div v-if="storeSettings.favicon" class="mb-4">
                      <img
                        :src="storeSettings.favicon"
                        alt="Favicon"
                        class="h-16 mx-auto object-contain"
                      />
                    </div>
                    <div v-else class="mb-4">
                      <i class="fas fa-flag text-3xl text-gray-400"></i>
                    </div>
                    <div class="mb-4">
                      <p class="text-sm text-gray-600 mb-2">
                        Upload your favicon
                      </p>
                      <p class="text-xs text-gray-500">
                        ICO, PNG up to 100KB
                      </p>
                    </div>
                    <div>
                      <Button
                        variant="outline"
                        size="sm"
                        @click="triggerFaviconUpload"
                      >
                        Choose Image
                      </Button>
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
              <h2 class="text-xl font-semibold text-gray-800 mb-6">Shipping Settings</h2>
              <div class="gap-6">
                <div class="flex items-center justify-between">
                  <div>
                    <h3 class="text-lg font-medium text-gray-800">Shipping Zones</h3>
                    <p class="text-gray-600">Configure shipping rates for different regions</p>
                  </div>
                  <Button variant="primary" @click="addShippingZone">
                    <i class="fas fa-plus mr-2"></i> Add Zone
                  </Button>
                </div>

                <div v-if="shippingZones.length === 0" class="text-center py-8">
                  <i class="fas fa-truck text-4xl text-gray-400 mb-3"></i>
                  <p class="text-gray-600 mb-4">No shipping zones configured</p>
                  <Button variant="outline" @click="addShippingZone">
                    Create Your First Shipping Zone
                  </Button>
                </div>

                <div v-else class="gap-4">
                  <div
                    v-for="zone in shippingZones"
                    :key="zone.id"
                    class="border border-gray-200 rounded-lg p-4"
                  >
                    <div class="flex items-center justify-between mb-3">
                      <h4 class="font-medium text-gray-800">{{ zone.name }}</h4>
                      <div class="flex gap-2">
                        <Button variant="ghost" size="sm" @click="editShippingZone(zone)">
                          <i class="fas fa-edit"></i>
                        </Button>
                        <Button variant="ghost" size="sm" @click="deleteShippingZone(zone)">
                          <i class="fas fa-trash text-red-600"></i>
                        </Button>
                      </div>
                    </div>
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                      <div>
                        <span class="text-gray-600">Countries:</span>
                        <span class="ml-2">{{ zone.countries.length }}</span>
                      </div>
                      <div>
                        <span class="text-gray-600">Methods:</span>
                        <span class="ml-2">{{ zone.methods.length }}</span>
                      </div>
                      <div>
                        <span class="text-gray-600">Status:</span>
                        <Badge :variant="zone.enabled ? 'success' : 'warning'" class="ml-2">
                          {{ zone.enabled ? 'Enabled' : 'Disabled' }}
                        </Badge>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="border-t border-gray-200 pt-8">
              <h3 class="text-lg font-semibold text-gray-800 mb-6">Shipping Options</h3>
              <div class="gap-4">
                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Free Shipping Threshold</label>
                    <p class="text-sm text-gray-600">
                      Offer free shipping on orders above this amount
                    </p>
                  </div>
                  <div class="w-48">
                    <div class="relative">
                      <span class="absolute left-3 top-2 text-gray-500">
                        {{ storeSettings.currencySymbol }}
                      </span>
                      <input
                        type="number"
                        v-model="shippingSettings.freeShippingThreshold"
                        min="0"
                        step="0.01"
                        class="w-full pl-8 pr-4 py-2 border border-gray-300 rounded-lg"
                      />
                    </div>
                  </div>
                </div>

                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Shipping Weight Unit</label>
                    <p class="text-sm text-gray-600">Unit used for shipping weight calculations</p>
                  </div>
                  <select
                    v-model="shippingSettings.weightUnit"
                    class="w-48 px-4 py-2 border border-gray-300 rounded-lg"
                  >
                    <option value="kg">Kilograms (kg)</option>
                    <option value="lb">Pounds (lb)</option>
                    <option value="oz">Ounces (oz)</option>
                  </select>
                </div>

                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Enable Shipping Calculator</label>
                    <p class="text-sm text-gray-600">Show real-time shipping rates at checkout</p>
                  </div>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      v-model="shippingSettings.enableCalculator"
                      class="sr-only peer"
                    />
                    <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                  </label>
                </div>
              </div>
            </div>
          </div>

          <!-- Tax Settings -->
          <div v-else-if="activeTab === 'tax'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-gray-800 mb-6">Tax Settings</h2>
              <div class="gap-6">
                <div class="flex items-center justify-between">
                  <div>
                    <h3 class="text-lg font-medium text-gray-800">Tax Rates</h3>
                    <p class="text-gray-600">Configure tax rates for different regions</p>
                  </div>
                  <Button variant="primary" @click="addTaxRate">
                    <i class="fas fa-plus mr-2"></i> Add Tax Rate
                  </Button>
                </div>

                <div v-if="taxRates.length === 0" class="text-center py-8">
                  <i class="fas fa-percentage text-4xl text-gray-400 mb-3"></i>
                  <p class="text-gray-600 mb-4">No tax rates configured</p>
                  <Button variant="outline" @click="addTaxRate">
                    Add Your First Tax Rate
                  </Button>
                </div>

                <div v-else>
                  <Table>
                    <thead>
                      <tr>
                        <th>Country/Region</th>
                        <th>Tax Name</th>
                        <th>Rate</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th class="text-right">Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="rate in taxRates" :key="rate.id">
                        <td>{{ rate.country }}</td>
                        <td>{{ rate.name }}</td>
                        <td>{{ rate.rate }}%</td>
                        <td>
                          <Badge :variant="rate.type === 'inclusive' ? 'success' : 'info'">
                            {{ rate.type }}
                          </Badge>
                        </td>
                        <td>
                          <Badge :variant="rate.enabled ? 'success' : 'warning'">
                            {{ rate.enabled ? 'Active' : 'Inactive' }}
                          </Badge>
                        </td>
                        <td class="text-right">
                          <div class="flex justify-end gap-2">
                            <Button variant="ghost" size="sm" @click="editTaxRate(rate)">
                              <i class="fas fa-edit"></i>
                            </Button>
                            <Button variant="ghost" size="sm" @click="deleteTaxRate(rate)">
                              <i class="fas fa-trash text-red-600"></i>
                            </Button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </Table>
                </div>
              </div>
            </div>

            <div class="border-t border-gray-200 pt-8">
              <h3 class="text-lg font-semibold text-gray-800 mb-6">Tax Configuration</h3>
              <div class="gap-4">
                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Tax Calculation Method</label>
                    <p class="text-sm text-gray-600">How taxes are calculated on orders</p>
                  </div>
                  <select
                    v-model="taxSettings.calculationMethod"
                    class="w-48 px-4 py-2 border border-gray-300 rounded-lg"
                  >
                    <option value="exclusive">Exclusive (Add tax to price)</option>
                    <option value="inclusive">Inclusive (Tax included in price)</option>
                  </select>
                </div>

                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Show Prices With Tax</label>
                    <p class="text-sm text-gray-600">Display prices including tax to customers</p>
                  </div>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      v-model="taxSettings.showPricesWithTax"
                      class="sr-only peer"
                    />
                    <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                  </label>
                </div>

                <div class="flex items-center justify-between">
                  <div>
                    <label class="font-medium text-gray-800">Tax ID/Number</label>
                    <p class="text-sm text-gray-600">Your business tax identification number</p>
                  </div>
                  <input
                    type="text"
                    v-model="taxSettings.taxId"
                    class="w-48 px-4 py-2 border border-gray-300 rounded-lg"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- Notifications -->
          <div v-else-if="activeTab === 'notifications'" class="gap-8">
            <div>
              <h2 class="text-xl font-semibold text-gray-800 mb-6">Notification Settings</h2>
              <div class="gap-6">
                <div>
                  <h3 class="text-lg font-medium text-gray-800 mb-4">Email Notifications</h3>
                  <div class="gap-4">
                    <div v-for="notification in emailNotifications" :key="notification.id" class="flex items-center justify-between">
                      <div>
                        <label class="font-medium text-gray-800">{{ notification.name }}</label>
                        <p class="text-sm text-gray-600">{{ notification.description }}</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          v-model="notification.enabled"
                          class="sr-only peer"
                        />
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                      </label>
                    </div>
                  </div>
                </div>

                <div class="border-t border-gray-200 pt-8">
                  <h3 class="text-lg font-medium text-gray-800 mb-4">Push Notifications</h3>
                  <div class="gap-4">
                    <div class="flex items-center justify-between">
                      <div>
                        <label class="font-medium text-gray-800">Enable Browser Notifications</label>
                        <p class="text-sm text-gray-600">Receive notifications in your browser</p>
                      </div>
                      <Button
                        variant="outline"
                        size="sm"
                        @click="requestNotificationPermission"
                      >
                        Request Permission
                      </Button>
                    </div>
                  </div>
                </div>

                <div class="border-t border-gray-200 pt-8">
                  <h3 class="text-lg font-medium text-gray-800 mb-4">Notification Preferences</h3>
                  <div class="gap-4">
                    <div class="flex items-center justify-between">
                      <div>
                        <label class="font-medium text-gray-800">Notification Frequency</label>
                        <p class="text-sm text-gray-600">How often you receive notifications</p>
                      </div>
                      <select
                        v-model="notificationSettings.frequency"
                        class="w-48 px-4 py-2 border border-gray-300 rounded-lg"
                      >
                        <option value="realtime">Real-time</option>
                        <option value="hourly">Hourly Digest</option>
                        <option value="daily">Daily Digest</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Save Button -->
          <div class="flex justify-end gap-4 pt-6 mt-6 border-t border-gray-200">
            <Button
              type="button"
              variant="outline"
              @click="resetSettings"
            >
              Reset
            </Button>
            <Button
              type="button"
              variant="primary"
              :loading="saving"
              @click="saveSettings"
            >
              Save Changes
            </Button>
          </div>
        </Card>
      </div>
    </div>

    <!-- Modals -->
    <Modal
      v-if="showShippingZoneModal"
      :title="editingShippingZone ? 'Edit Shipping Zone' : 'Add Shipping Zone'"
      size="lg"
      @close="closeShippingZoneModal"
    >
      <!-- Shipping zone form content -->
    </Modal>

    <Modal
      v-if="showTaxRateModal"
      :title="editingTaxRate ? 'Edit Tax Rate' : 'Add Tax Rate'"
      @close="closeTaxRateModal"
    >
      <!-- Tax rate form content -->
    </Modal>
  </div>
</template>

<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
import Button from '@/components/ui/Button.vue';
import Badge from '@/components/ui/Badge.vue';
import Table from '@/components/ui/Table.vue';
import Modal from '@/components/ui/Modal.vue';
</script>
