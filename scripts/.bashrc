cat > .env << 'EOF'
# =========================
# Database 환경 변수
# =========================
DB_URL=jdbc:mariadb://localhost:3306/ddasum
DB_USERNAME=root
DB_PASSWORD=1234

# =========================
# JWT 환경 변수
# =========================
JWT_SECRET=abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=~[]{}|;:,.<>?/abcdefgh
JWT_ACCESS_TOKEN_EXPIRATION=1800000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# =========================
# 파일 업로드 환경 변수
# =========================
UPLOAD_DIR=uploads

# =========================
# 이메일 환경 변수
# =========================
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=dhxogns920@gmail.com
MAIL_PASSWORD=ssql soan kvim hnuz

# =========================
# 서버 환경 변수
# =========================
SERVER_PORT=8080

# =========================
# Redis 환경 변수
# =========================
REDIS_HOST=localhost
REDIS_PORT=6379

# =========================
# Iamport 환경 변수
# =========================
IAMPORT_API_KEY=6241148542384302
IAMPORT_API_SECRET=cH4W5x3LCve4w9lXxXxxz95oRnajI4f0prhw9KD68rg2vevXqUmIlNA0Ilq5685Q2rNqF1DPt8G0E8ki
EOF