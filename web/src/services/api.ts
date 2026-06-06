import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8081/api',
    headers: { 'Content-Type': 'application/json' },
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Network error — server unreachable or CORS preflight failed
            return Promise.reject(new Error('Cannot reach the server. Is the API running?'));
        }
        const message =
            error.response.data?.message ||
            `HTTP error! status: ${error.response.status}`;
        return Promise.reject(new Error(message));
    }
);

export default api;
