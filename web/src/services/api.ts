import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8081/api',
    headers: { 'Content-Type': 'application/json' },
});

// Unwrap ApiResponse<T> wrapper ({ success, data, message }) automatically.
// Every service can then do `const { data } = await api.get(...)` and get T directly.
api.interceptors.response.use(
    (response) => {
        if (response.data && typeof response.data === 'object' && 'success' in response.data) {
            response.data = response.data.data;
        }
        return response;
    },
    (error) => {
        if (!error.response) {
            return Promise.reject(new Error('Cannot reach the server. Is the API running?'));
        }
        const message =
            error.response.data?.message ||
            `HTTP error! status: ${error.response.status}`;
        return Promise.reject(new Error(message));
    }
);

export default api;
