#!/bin/bash
VAULT_RETRIES=5
echo "Vault is starting..."
until vault status > /dev/null 2>&1 || [ "$VAULT_RETRIES" -eq 0 ]; do
        echo "Waiting for vault to start...: $((VAULT_RETRIES--))"
        sleep 1
done
echo "Authenticating to vault..."
vault login token=vault-plaintext-root-token
echo "Initializing vault..."

vault secrets enable -version=2 -path=secrets kv

echo "Adding entries..."

vault kv put secrets/sensitive-words-service \
    spring.datasource.username=test_user \
    spring.datasource.password=test_password \
    app.config.auth.token=vault-plaintext-root-token \
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=sensitive_words;trustServerCertificate=true

echo "Complete..."