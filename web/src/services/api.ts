import axios from 'axios';

const TOKEN_KEY = 'auth_token';
const USER_KEY  = 'auth_user';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8081/api',
    headers: { 'Content-Type': 'application/json' },
});

// ── Request interceptor — inject Bearer token ────────────────────────────
api.interceptors.request.use((config) => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// ── Response interceptor ─────────────────────────────────────────────────
api.interceptors.response.use(
    (response) => {
        // Unwrap ApiResponse<T> wrapper ({ success, data, message }) automatically.
        // Every service can then do `const { data } = await api.get(...)` and get T directly.
        if (response.data && typeof response.data === 'object' && 'success' in response.data) {
            response.data = response.data.data;
        }
        return response;
    },
    (error) => {
        if (!error.response) {
            return Promise.reject(new Error('Cannot reach the server. Is the API running?'));
        }

        // 401 = no token / token expired — redirect to login.
        // Do NOT handle 403 here: once authenticated, 403 means "forbidden for this
        // resource" (not "you need to log in") and should be surfaced as an error.
        if (error.response.status === 401) {
            const wasOnLogin = window.location.pathname.includes('/login');
            localStorage.removeItem(TOKEN_KEY);
            localStorage.removeItem(USER_KEY);
            if (!wasOnLogin) {
                const redirect = encodeURIComponent(window.location.pathname + window.location.search);
                window.location.href = `/login?redirect=${redirect}`;
            }
        }

        const message =
            error.response.data?.message ||
            `HTTP error! status: ${error.response.status}`;
        return Promise.reject(new Error(message));
    },
);

export default api;
