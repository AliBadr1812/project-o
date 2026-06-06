# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

# VITE_API_URL is baked into the bundle at build time
ARG VITE_API_URL=http://localhost:8081/api
ENV VITE_API_URL=${VITE_API_URL}

COPY . .
RUN npm run build

# ── Stage 2: Serve ───────────────────────────────────────────────────────────
FROM node:20-alpine
WORKDIR /app
COPY --from=build /app/dist ./dist
RUN npm install -g http-server
EXPOSE 5173
CMD ["sh", "-c", "http-server -p ${PORT:-5173} ./dist"]
