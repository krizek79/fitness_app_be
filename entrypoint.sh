#!/bin/sh

set -e

echo "[ENTRYPOINT] Decoding keystore..."
echo "$JWT_KEYSTORE_B64" | base64 -d > /app/keystore.p12

echo "[ENTRYPOINT] Starting application..."
exec java -jar app.jar
