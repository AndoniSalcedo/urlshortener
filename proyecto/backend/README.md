Para ejecutar keycloak ejecutar:

docker run -p 8000:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -e DB_VENDOR=h2 quay.io/keycloak/keycloak:16.1.0

acontinuación importar el realm que se encuentra en la carpeta src/main/resources/realm-export.json