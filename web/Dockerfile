# ==============================
# FRONTEND - Vue 3 + Vite
# ==============================

# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files first (better caching)
COPY package*.json ./
RUN npm ci --only=production

# Copy source and build
COPY . .
RUN npm run build

# ==============================
# Stage 2: Production server
# ==============================
FROM nginx:alpine

# Copy built assets from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]