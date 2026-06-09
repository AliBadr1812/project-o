<template>
  <div class="login-page">
    <div class="wallpaper fixed inset-0 -z-10"></div>

    <div class="login-card glass-card">
      <!-- Brand -->
      <div class="flex flex-col items-center gap-3 mb-8">
        <div class="login-logo">
          <i class="fas fa-store"></i>
        </div>
        <div class="text-center">
          <h1 class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">ShopAdmin</h1>
          <p class="text-sm mt-1" style="color: var(--text-muted);">Sign in to your dashboard</p>
        </div>
      </div>

      <!-- Error banner -->
      <Transition name="fade">
        <div v-if="errorMsg" class="login-error mb-5">
          <i class="fas fa-circle-exclamation text-xs mr-2"></i>
          {{ errorMsg }}
        </div>
      </Transition>

      <!-- Form -->
      <form @submit.prevent="handleLogin" class="flex flex-col gap-4">
        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold uppercase tracking-wide" style="color: var(--text-muted);">Email</label>
          <div class="relative">
            <i class="fas fa-envelope absolute left-3.5 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted);"></i>
            <input
              v-model="email"
              type="email"
              autocomplete="email"
              required
              placeholder="admin@example.com"
              class="glass-input pl-9 w-full"
              :disabled="loading"
            />
          </div>
        </div>

        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold uppercase tracking-wide" style="color: var(--text-muted);">Password</label>
          <div class="relative">
            <i class="fas fa-lock absolute left-3.5 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted);"></i>
            <input
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              autocomplete="current-password"
              required
              placeholder="••••••••"
              class="glass-input pl-9 pr-10 w-full"
              :disabled="loading"
            />
            <button
              type="button"
              @click="showPassword = !showPassword"
              class="absolute right-3 top-1/2 -translate-y-1/2 text-xs"
              style="color: var(--text-muted);"
              tabindex="-1"
            >
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </button>
          </div>
        </div>

        <button
          type="submit"
          class="btn-accent w-full mt-2 py-2.5 text-sm font-semibold"
          :disabled="loading"
        >
          <i v-if="loading" class="fas fa-spinner fa-spin text-xs mr-2"></i>
          {{ loading ? 'Signing in…' : 'Sign in' }}
        </button>
      </form>

      <!-- Default credentials hint -->
      <p class="text-center text-xs mt-5" style="color: var(--text-muted);">
        Default: admin@shopadmin.com / admin123
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuth } from '@/composables/useAuth';
import { useToast } from '@/composables/useToast';

const router = useRouter();
const route  = useRoute();
const { login } = useAuth();
const toast  = useToast();

const email        = ref('');
const password     = ref('');
const loading      = ref(false);
const errorMsg     = ref('');
const showPassword = ref(false);

async function handleLogin() {
  errorMsg.value = '';
  loading.value  = true;
  try {
    await login(email.value, password.value);
    toast.success('Welcome back!', 'Signed in');
    const redirect = (route.query.redirect as string) || '/dashboard';
    await router.replace(redirect);
  } catch (e: unknown) {
    errorMsg.value = e instanceof Error ? e.message : 'Sign-in failed. Please try again.';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px 36px;
  border-radius: 24px;
}

.login-logo {
  width: 56px;
  height: 56px;
  border-radius: 18px;
  background: linear-gradient(145deg, #b97fff, #7c5ef0, #5e5ce6);
  box-shadow: inset 0 1px 0 rgba(255,255,255,0.4), 0 6px 20px rgba(124,94,240,0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255,255,255,0.92);
  font-size: 22px;
}

.login-error {
  background: rgba(220, 38, 38, 0.08);
  border: 1px solid rgba(220, 38, 38, 0.2);
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 13px;
  color: #dc2626;
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
