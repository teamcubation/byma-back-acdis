# byma-back-acdis

## Descripción General
Esta es una API basada en Spring Boot que permite gestionar ACDIs(Agente de Colocación y Distribución Integral). Puedes crear, obtener, actualizar(el requerimiento actual solo permite modificar los campos mail y liquidaEnByma), dar de baja(baja logica) y eliminar ACDIs utilizando esta API. La API está documentada con Swagger y puedes interactuar con ella a través de la interfaz de usuario de Swagger.

## Tecnologías

- **Spring Boot** - Framework para el backend
- **Java** - Lenguaje de programación
- **Maven** - Gestión de dependencias
- **Lombok** - Para reducir código repetitivo
- **Jakarta Validation** - Validación de DTOs
- **Swagger/OpenAPI** - Documentación de la API
- **Base de Datos H2 (Opcional)** - Base de datos en memoria para desarrollo/pruebas
- **Postman** - Testing de la API
- **JUnit 5** - Framework de pruebas unitarias
- **Mockito** - Framework de mocking para pruebas unitarias
- **Docker (Opcional)** - Contenerización
- **Spring Data JPA** - Capa de acceso a datos

## Comenzando

### 1. Clonar el Repositorio

```bash
git clone https://github.com/teamcubation/byma-back-acdis
cd byma-back-acdis
```

### 2. Construir el Proyecto
Usa Maven para instalar las dependencias y construir el proyecto:
```bash
mvn clean install
```

### 3. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

Por defecto, la aplicación se ejecuta en `http://localhost:8080`. Puedes acceder a la API a través de esta URL.

### 4. Acceder a Swagger UI
Una vez que la aplicación esté en ejecución, puedes ver la documentación de la API e interactuar con ella a través de Swagger UI:
```bash
http://localhost:8080/swagger-ui/index.html
```

### 5. Ejemplos de Solicitudes a la API
A continuación, algunos ejemplos para interactuar con la API utilizando Postman:

1. Crear un ACDI (POST /api/v1/acdis)

**POST** `http://localhost:8080/api/v1/acdis`
```json
{
  "idOrganizacion": "ORG003",
  "denominacion": "Denominación 3",
  "liquidaEnByma": true,
  "habilitado": true,
  "billeteras": false,
  "observaciones": "Observaciones",
  "mail": "pauDeJoseCPaz@email.com"
}
```

2. Obtener todos los ACDIs (GET /api/v1/acdis)

**GET** `http://localhost:8080/api/v1/acdis`

3. Obtener ACDI por ID (GET /api/v1/acdis/{id})

**GET** `http://localhost:8080/api/v1/acdis/{id}`

4. Actualizar un ACDI (PUT /api/v1/acdi/{id})

**PUT** `http://localhost:8080/api/v1/acdis/{id}`
```json
{
  "mail": "gustavoDeBalvnera@email.com",
  "liquidaEnByma": true
}
```

5. Dar de baja un ACDI - baja logica (PUT /api/v1/acdis/{id}/baja)

**PUT** `http://localhost:8080/api/v1/acdis/{id}/baja`

6. Eliminar un ACDI (DELETE /api/v1/acdis/{id})

**DELETE** `http://localhost:8080/api/v1/acdis/{id}`