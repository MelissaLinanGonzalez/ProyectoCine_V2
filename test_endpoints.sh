#!/bin/bash

# 1. Register user
echo "=== Registering User ==="
REGISTER_RESP=$(curl -s -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"testuser3@test.com", "password":"password"}')

echo "Register Response:"
echo $REGISTER_RESP

TOKEN=$(echo $REGISTER_RESP | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. Get Public Functions
echo -e "\n=== Get Public Funciones ==="
curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X GET http://localhost:8080/api/v1/funciones

# 3. Post Function without auth (should be 401)
echo -e "\n=== Post Function (Unauth) ==="
curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X POST http://localhost:8080/api/v1/funciones \
  -H "Content-Type: application/json" \
  -d '{"peliculaId":1, "salaId":1, "fecha":"2026-05-16T20:00:00", "precio":10.0}'

# 4. Post Function as USER (should be 403)
echo -e "\n=== Post Function (USER Auth) ==="
curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X POST http://localhost:8080/api/v1/funciones \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"peliculaId":1, "salaId":1, "fecha":"2026-05-16T20:00:00", "precio":10.0}'

# 5. Get mis-entradas as USER
echo -e "\n=== Get mis-entradas (USER Auth) ==="
curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X GET http://localhost:8080/api/v1/entradas/mis-entradas \
  -H "Authorization: Bearer $TOKEN"

