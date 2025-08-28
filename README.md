# Servicio de Gestión de Pólizas

> **Prueba Técnica Backend** - Sistema de gestión de pólizas implementado con **Java 21**, **Quarkus 3.x** y **Clean Architecture + DDD con Seedwork**.

## 🎯 Objetivo

Desarrollar un servicio backend para la gestión de pólizas de salud que implemente:
- **Clean Architecture** con separación clara de capas
- **Domain-Driven Design (DDD)** con un Seedwork reutilizable
- **API REST** siguiendo estándares HTTP y RFC 7807
- **Validaciones** robustas incluyendo RUT chileno
- **Tests** con cobertura 30%
- **Documentación** OpenAPI/Swagger

## 🏗️ Arquitectura

### Clean Architecture + DDD Seedwork

```
src/main/java/cl/demo/polizas/
├── seedwork/                    # DDD Seedwork reutilizable
│   ├── domain/                  # Entity, AggregateRoot, ValueObject, etc.
│   ├── application/             # UseCase, Query, Command, Mapper
│   └── infrastructure/          # Ports y adaptadores base
├── domain/                      # Lógica de negocio pura
│   ├── model/                   # Aggregates (Policy)
│   ├── valueobject/            # Rut, PolicyId, Money, PolicyStatus
│   ├── repository/              # Interfaces de repositorio
│   ├── service/                 # Servicios de dominio
│   └── events/                  # Eventos de dominio
├── application/                 # Casos de uso y orquestación
│   ├── usecase/                 # CreatePolicy, ListPolicies, etc.
│   └── dto/                     # DTOs de entrada/salida
└── app/                         # Infraestructura y adaptadores
    ├── config/                  # Configuración y beans
    ├── rest/                    # Recursos REST (JAX-RS)
    ├── persistence/             # Entidades JPA y repositorios
    ├── mappers/                 # Mapeadores DTO ↔ Domain
    └── validation/              # Validadores personalizados
```

### Principios DDD

- **Policy** como Aggregate Root
- **Value Objects** inmutables (Rut, Money, PolicyId)
- **Domain Events** para cambios de estado
- **Repository Pattern** para persistencia
- **Specification Pattern** para filtros complejos

## 🚀 Tecnologías

- **Java 21** (LTS)
- **Quarkus 3.7.4** (Framework nativo de Java)
- **Hibernate ORM + Panache** (Persistencia)
- **H2 Database** (In-memory por defecto)
- **Hibernate Validator** (Validaciones)
- **JAX-RS** (API REST)
- **Jackson** (Serialización JSON)
- **OpenAPI/Swagger** (Documentación)
- **JUnit 5 + REST Assured** (Testing)
- **JaCoCo** (Cobertura de código)

## 📋 Requisitos

- **Java 21** o superior
- **Maven 3.8+**
- **Git**

## 🏃‍♂️ Arranque Rápido

### 1. Clonar y configurar

```bash
git clone <repository-url>
cd polizas-service
```

### 2. Ejecutar en modo desarrollo

```bash
./mvnw quarkus:dev
```

La aplicación estará disponible en:
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI**: http://localhost:8080/openapi
- **Health Check**: http://localhost:8080/health

### 3. Ejecutar tests

```bash
./mvnw test
```

### 4. Ver cobertura de código

```bash
./mvnw jacoco:report
```

El reporte estará en: `target/site/jacoco/index.html`

## 📚 API Endpoints

### Crear Póliza

```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "rutTitular": "12.345.678-5",
    "fechaEmision": "2025-01-10",
    "planSalud": "Plan Oro",
    "prima": 34990.00
  }'
```

**Respuesta**: `201 Created` con header `Location: /api/policies/{id}`

### Listar Pólizas

```bash
# Todas las pólizas
curl http://localhost:8080/api/policies

# Por estado
curl "http://localhost:8080/api/policies?estado=activa"

# Por rango de fechas
curl "http://localhost:8080/api/policies?from=2025-01-01&to=2025-12-31"

# Combinando filtros
curl "http://localhost:8080/api/policies?estado=activa&from=2025-01-01&to=2025-12-31"
```

### Obtener Póliza por ID

```bash
curl http://localhost:8080/api/policies/{id}
```

### Actualizar Estado

```bash
curl -X PUT http://localhost:8080/api/policies/{id}/status \
  -H "Content-Type: application/json" \
  -d '{ "estado": "activa" }'
```

**Transiciones válidas**:
- `emitida` → `activa`
- `activa` → `anulada`
- **No reversibles**

## 🔍 Validaciones

### RUT Chileno
- Formato: `XXXXXXXX-X` (con o sin puntos)
- Validación de dígito verificador (módulo 11)
- Normalización automática

### Fecha de Emisión
- Formato ISO-8601 (YYYY-MM-DD)
- No puede ser futura

### Plan de Salud
- Requerido
- 1-100 caracteres

### Prima
- Debe ser > 0
- Máximo 2 decimales

## 🗄️ Base de Datos

### H2 (Por defecto)
- **URL**: `jdbc:h2:mem:polizasdb`
- **Usuario**: `sa`
- **Contraseña**: `password`
- **DDL**: `drop-and-create` (desarrollo)

### SQLite (Opcional)
```bash
./mvnw quarkus:dev -Dquarkus.profile=sqlite
```

## 🧪 Testing

### Estructura de Tests
```
src/test/java/cl/demo/polizas/
├── domain/                      # Tests de dominio
├── application/                 # Tests de casos de uso
└── app/rest/                    # Tests de API REST
```

### Ejecutar Tests
```bash
# Todos los tests
./mvnw test

# Tests específicos
./mvnw test -Dtest=PolicyResourceTest

# Con cobertura
./mvnw clean test jacoco:report
```

### Cobertura Mínima
- **Dominio**: 100%
- **Aplicación**: ≥ 90%
- **API**: ≥ 80%
- **Total**: ≥ 80%

## 🐳 Docker

### Construir imagen
```bash
./mvnw clean package -Dquarkus.package.type=jar
docker build -t polizas-service .
```

### Ejecutar contenedor
```bash
docker run -p 8080:8080 polizas-service
```

## 🔧 Configuración

### Perfiles disponibles
- **dev** (por defecto): H2 in-memory, logging DEBUG
- **test**: H2 in-memory, logging WARN
- **sqlite**: SQLite persistente
- **prod**: Configuración de producción

### Variables de entorno
```bash
# Base de datos
QUARKUS_DATASOURCE_JDBC_URL=jdbc:h2:mem:polizasdb
QUARKUS_DATASOURCE_USERNAME=sa
QUARKUS_DATASOURCE_PASSWORD=password

# Logging
QUARKUS_LOG_LEVEL=INFO
QUARKUS_LOG_CATEGORY__CL_DEMO_POLIZAS_LEVEL=DEBUG
```

## 📊 Observabilidad

### Health Checks
- **Liveness**: Estado general de la aplicación
- **Readiness**: Disponibilidad para recibir requests
- **Database**: Estado de la conexión a BD

### Métricas
- **Micrometer**: Métricas de JVM, HTTP, base de datos
- **Endpoint**: `/metrics` (formato Prometheus)

### Logging
- **Formato**: JSON estructurado
- **Nivel**: INFO (producción), DEBUG (desarrollo)
- **Correlación**: `traceId` y `spanId` por request

## 🚨 Manejo de Errores

### RFC 7807 (Problem Details)
```json
{
  "type": "https://polizas-service.com/errors/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Los datos de entrada no son válidos",
  "instance": "/api/policies",
  "errors": {
    "rutTitular": "RUT inválido",
    "prima": "Debe ser mayor a 0"
  }
}
```

### Códigos de Estado HTTP
- **200**: OK (operación exitosa)
- **201**: Created (recurso creado)
- **400**: Bad Request (validación fallida)
- **404**: Not Found (recurso no encontrado)
- **409**: Conflict (transición de estado inválida)
- **500**: Internal Server Error (error interno)

## 🏆 Decisiones Técnicas

### Clean Architecture
- **Independencia del framework** en dominio y aplicación
- **Inversión de dependencias** con interfaces
- **Separación clara** de responsabilidades

### DDD Seedwork
- **Clases base reutilizables** para entidades y value objects
- **Patrones comunes** (Repository, Specification, Result)
- **Facilita testing** con abstracciones como Clock

### Quarkus
- **Framework nativo** optimizado para Java
- **Startup rápido** y bajo consumo de memoria
- **Extensiones** para validación, persistencia y documentación

### Validaciones
- **Bean Validation** para validaciones básicas
- **Validadores personalizados** para RUT chileno
- **Manejo consistente** de errores con RFC 7807

## 📈 Roadmap

### Fase 1 (Actual)
- ✅ API REST básica
- ✅ Validaciones de dominio
- ✅ Persistencia H2
- ✅ Tests unitarios

### Fase 2 (Próxima)
- 🔄 Paginación y ordenamiento
- 🔄 Filtros avanzados con Specification
- 🔄 Event sourcing básico
- 🔄 Cache con Redis

### Fase 3 (Futura)
- 🔄 Outbox pattern para eventos
- 🔄 Testcontainers para tests de integración
- 🔄 CI/CD con GitHub Actions
- 🔄 Monitoreo con Prometheus + Grafana

## 🤝 Contribución

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/amazing-feature`)
3. Commit tus cambios (`git commit -m 'Add amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Desarrollador Backend** - [Tu Nombre]
- **Email**: tu.email@ejemplo.com
- **LinkedIn**: [Tu Perfil](https://linkedin.com/in/tu-perfil)

---

**¿Preguntas o sugerencias?** Abre un issue en el repositorio o contacta al equipo de desarrollo.

