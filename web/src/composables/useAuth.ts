import { ref, computed } from 'vue';
import api from '@/services/api';

export interface AuthUser {
  id:      number;
  name:    string;
  email:   string;
  role:    string;
  avatar?: string;
}

const TOKEN_KEY = 'auth_token';
const USER_KEY  = 'auth_user';

// ── Singleton reactive state (module-level) ──────────────────────────────
const user = ref<AuthUser | null>(
  (() => {
    try { return JSON.parse(localStorage.getItem(USER_KEY) ?? 'null') as AuthUser | null; }
    catch { return null; }
  })(),
);

const token = ref<string | null>(localStorage.getItem(TOKEN_KEY));

const isAuthenticated = computed(() => !!token.value && !!user.value);

/**
 * Authenticate against the real backend (POST /api/auth/login).
 * Stores the JWT + user profile in localStorage on success.
 * Throws an Error with the server's message on failure.
 */
async function login(email: string, password: string): Promise<void> {
  // api.ts unwraps ApiResponse<T>, so `data` here is already AuthResponse
  const { data } = await api.post<{ token: string; user: AuthUser }>(
    '/auth/login',
    { email, password },
  );

  user.value  = data.user;
  token.value = data.token;
  localStorage.setItem(TOKEN_KEY, data.token);
  localStorage.setItem(USER_KEY,  JSON.stringify(data.user));
}

/**
 * Clear local auth state.
 * Also calls the backend logout endpoint (fire-and-forget — purely informational
 * since JWTs are stateless, but good practice for audit logs).
 */
function logout() {
  api.post('/auth/logout').catch(() => { /* ignore network errors on logout */ });
  user.value  = null;
  token.value = null;
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}

export function useAuth() {
  return { user, token, isAuthenticated, login, logout };
}
