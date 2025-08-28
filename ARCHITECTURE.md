# Arquitectura de la Aplicación - Gestión de Pólizas

## Diagrama de Arquitectura General

```mermaid
graph TB
    subgraph "API Layer (REST)"
        REST[PolicyResource]
        DTO[CreatePolicyRequest, CreatePolicyResponse, etc.]
        VALID[Validation]
    end
    
    subgraph "Application Layer"
        UC1[CreatePolicyUseCase]
        UC2[ListPoliciesUseCase]
        UC3[GetPolicyUseCase]
        UC4[UpdatePolicyStatusUseCase]
        MAPPER[PolicyMapper]
    end
    
    subgraph "Domain Layer"
        POLICY[Policy Aggregate]
        VO1[PolicyId]
        VO2[PolicyStatus]
        VO3[Money]
        VO4[Rut]
        REPO[PolicyRepository]
        EVENTS[Domain Events]
    end
    
    subgraph "Infrastructure Layer"
        PERSIST[PolicyRepositoryImpl]
        ENTITY[PolicyEntity]
        DB[(H2/SQLite Database)]
    end
    
    subgraph "Seedwork"
        ENT[Entity]
        AGG[AggregateRoot]
        VO[ValueObject]
        RESULT[Result]
        GUARD[Guard]
        SPEC[Specification]
        CLOCK[Clock]
    end
    
    REST --> UC1
    REST --> UC2
    REST --> UC3
    REST --> UC4
    UC1 --> MAPPER
    UC2 --> MAPPER
    UC3 --> MAPPER
    UC4 --> MAPPER
    UC1 --> POLICY
    UC2 --> POLICY
    UC3 --> POLICY
    UC4 --> POLICY
    UC1 --> REPO
    UC2 --> REPO
    UC3 --> REPO
    UC4 --> REPO
    POLICY --> VO1
    POLICY --> VO2
    POLICY --> VO3
    POLICY --> VO4
    POLICY --> EVENTS
    REPO --> PERSIST
    PERSIST --> ENTITY
    ENTITY --> DB
    POLICY --> AGG
    AGG --> ENT
    VO1 --> VO
    VO2 --> VO
    VO3 --> VO
    VO4 --> VO
    UC1 --> RESULT
    UC2 --> RESULT
    UC3 --> RESULT
    UC4 --> RESULT
    POLICY --> GUARD
    POLICY --> SPEC
    POLICY --> CLOCK
```

## Diagrama del Modelo de Dominio

```mermaid
classDiagram
    class Policy {
        -PolicyId id
        -String policyNumber
        -Rut clientRut
        -String clientName
        -String clientEmail
        -Money premium
        -Money coverage
        -PolicyStatus status
        -LocalDate startDate
        -LocalDate endDate
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -String description
        -String insuranceType
        +create(...) Policy
        +activate() void
        +cancel() void
        +updateStatus(PolicyStatus) void
        +validateBusinessRules() void
        +validateStatusTransition(PolicyStatus) boolean
    }
    
    class PolicyId {
        -UUID value
        +generate() PolicyId
        +from(String) PolicyId
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }
    
    class PolicyStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        CANCELLED
        PENDING
        EXPIRED
        +getDisplayName() String
        +canBeActivated() boolean
        +canBeCancelled() boolean
    }
    
    class Money {
        -BigDecimal amount
        -Currency currency
        +add(Money) Money
        +subtract(Money) Money
        +multiply(BigDecimal) Money
        +getAmount() BigDecimal
        +getCurrency() Currency
    }
    
    class Rut {
        -String value
        +from(String) Rut
        +validate() boolean
        +getVerificationDigit() char
        +toString() String
    }
    
    class PolicyCreatedEvent {
        -String policyId
        -String clientRut
        -String clientName
        -Instant occurredOn
        +getType() String
        +getAggregateId() String
        +getOccurredOn() Instant
    }
    
    class PolicyActivatedEvent {
        -String policyId
        -String status
        -Instant occurredOn
        +getType() String
        +getAggregateId() String
        +getOccurredOn() Instant
    }
    
    class PolicyCancelledEvent {
        -String policyId
        -String status
        -Instant occurredOn
        +getType() String
        +getAggregateId() String
        +getOccurredOn() Instant
    }
    
    class PolicyStatusUpdatedEvent {
        -String policyId
        -String oldStatus
        -String newStatus
        -Instant occurredOn
        +getType() String
        +getAggregateId() String
        +getOccurredOn() Instant
    }
    
    Policy --> PolicyId
    Policy --> PolicyStatus
    Policy --> Money
    Policy --> Rut
    Policy --> PolicyCreatedEvent
    Policy --> PolicyActivatedEvent
    Policy --> PolicyCancelledEvent
    Policy --> PolicyStatusUpdatedEvent
```

## Diagrama de Casos de Uso

```mermaid
graph LR
    subgraph "API Endpoints"
        POST[POST /policies]
        GET_ALL[GET /policies]
        GET_ONE[GET /policies/{id}]
        PUT_STATUS[PUT /policies/{id}/status]
    end
    
    subgraph "Use Cases"
        CREATE[CreatePolicyUseCase]
        LIST[ListPoliciesUseCase]
        GET[GetPolicyUseCase]
        UPDATE_STATUS[UpdatePolicyStatusUseCase]
    end
    
    subgraph "Domain Operations"
        VALIDATE[Validate Business Rules]
        PERSIST[Persist Policy]
        RETRIEVE[Retrieve Policy]
        UPDATE[Update Status]
        EVENTS[Raise Domain Events]
    end
    
    POST --> CREATE
    GET_ALL --> LIST
    GET_ONE --> GET
    PUT_STATUS --> UPDATE_STATUS
    
    CREATE --> VALIDATE
    CREATE --> PERSIST
    CREATE --> EVENTS
    
    LIST --> RETRIEVE
    GET --> RETRIEVE
    
    UPDATE_STATUS --> VALIDATE
    UPDATE_STATUS --> UPDATE
    UPDATE_STATUS --> EVENTS
```

## Diagrama de Capas de Arquitectura

```mermaid
graph TB
    subgraph "Presentation Layer"
        REST_API[REST API]
        OPENAPI[OpenAPI/Swagger]
        VALIDATION[Input Validation]
        ERROR_HANDLING[Error Handling RFC 7807]
    end
    
    subgraph "Application Layer"
        USE_CASES[Use Cases]
        DTOs[Data Transfer Objects]
        MAPPERS[Object Mappers]
        VALIDATION_LOGIC[Business Validation]
    end
    
    subgraph "Domain Layer"
        AGGREGATES[Aggregates]
        VALUE_OBJECTS[Value Objects]
        DOMAIN_SERVICES[Domain Services]
        DOMAIN_EVENTS[Domain Events]
        REPOSITORIES[Repository Interfaces]
    end
    
    subgraph "Infrastructure Layer"
        PERSISTENCE[Persistence Implementation]
        DATABASE[(Database)]
        EXTERNAL_SERVICES[External Services]
        LOGGING[Logging]
        METRICS[Metrics]
    end
    
    subgraph "Cross-Cutting Concerns"
        SEEDWORK[DDD Seedwork]
        CONFIGURATION[Configuration]
        HEALTH_CHECKS[Health Checks]
        SECURITY[Security]
    end
    
    REST_API --> USE_CASES
    OPENAPI --> REST_API
    VALIDATION --> REST_API
    ERROR_HANDLING --> REST_API
    
    USE_CASES --> AGGREGATES
    USE_CASES --> DTOs
    MAPPERS --> USE_CASES
    VALIDATION_LOGIC --> USE_CASES
    
    AGGREGATES --> VALUE_OBJECTS
    AGGREGATES --> DOMAIN_SERVICES
    AGGREGATES --> DOMAIN_EVENTS
    AGGREGATES --> REPOSITORIES
    
    PERSISTENCE --> AGGREGATES
    PERSISTENCE --> DATABASE
    EXTERNAL_SERVICES --> USE_CASES
    
    SEEDWORK --> AGGREGATES
    SEEDWORK --> VALUE_OBJECTS
    SEEDWORK --> REPOSITORIES
    
    CONFIGURATION --> PERSISTENCE
    CONFIGURATION --> EXTERNAL_SERVICES
    HEALTH_CHECKS --> INFRASTRUCTURE
    SECURITY --> PRESENTATION
```

## Diagrama de Flujo de Creación de Póliza

```mermaid
sequenceDiagram
    participant Client
    participant PolicyResource
    participant CreatePolicyUseCase
    participant PolicyMapper
    participant Policy
    participant PolicyRepository
    participant Database
    
    Client->>PolicyResource: POST /policies
    PolicyResource->>PolicyResource: Validate input
    PolicyResource->>CreatePolicyUseCase: handle(request)
    CreatePolicyUseCase->>PolicyMapper: map(request)
    CreatePolicyUseCase->>Policy: create(...)
    Policy->>Policy: validateBusinessRules()
    Policy->>Policy: raise(PolicyCreatedEvent)
    CreatePolicyUseCase->>PolicyRepository: save(policy)
    PolicyRepository->>Database: persist
    Database-->>PolicyRepository: success
    PolicyRepository-->>CreatePolicyUseCase: policy
    CreatePolicyUseCase->>PolicyMapper: toCreateResponse(policy)
    CreatePolicyUseCase-->>PolicyResource: response
    PolicyResource-->>Client: 201 Created
```

## Diagrama de Estados de Póliza

```mermaid
stateDiagram-v2
    [*] --> PENDING: create()
    PENDING --> ACTIVE: activate()
    PENDING --> CANCELLED: cancel()
    ACTIVE --> INACTIVE: updateStatus(INACTIVE)
    ACTIVE --> CANCELLED: cancel()
    ACTIVE --> EXPIRED: endDate reached
    INACTIVE --> ACTIVE: updateStatus(ACTIVE)
    INACTIVE --> CANCELLED: cancel()
    CANCELLED --> [*]
    EXPIRED --> [*]
    
    note right of PENDING
        Estado inicial al crear
        la póliza
    end note
    
    note right of ACTIVE
        Póliza vigente y
        operativa
    end note
    
    note right of CANCELLED
        Póliza cancelada
        permanentemente
    end note
```

## Tecnologías y Stack

```mermaid
graph LR
    subgraph "Runtime"
        JAVA[Java 21]
        QUARKUS[Quarkus 3.7.4]
        JVM[JVM]
    end
    
    subgraph "Persistence"
        H2[H2 Database]
        SQLITE[SQLite]
        JPA[JPA/Hibernate]
        PANACHE[Panache]
    end
    
    subgraph "API"
        REST[RESTEasy Reactive]
        JSON[Jackson]
        OPENAPI[OpenAPI]
        VALIDATION[Bean Validation]
    end
    
    subgraph "Observability"
        LOGGING[Logging JSON]
        METRICS[Micrometer]
        HEALTH[Health Checks]
        TRACING[Distributed Tracing]
    end
    
    subgraph "Development"
        MAVEN[Maven]
        SPOTLESS[Spotless]
        CHECKSTYLE[Checkstyle]
        JACOCO[JaCoCo]
    end
    
    subgraph "DevOps"
        DOCKER[Docker]
        GITHUB[GitHub Actions]
        CI_CD[CI/CD Pipeline]
    end
    
    JAVA --> JVM
    JVM --> QUARKUS
    QUARKUS --> REST
    QUARKUS --> JPA
    QUARKUS --> LOGGING
    QUARKUS --> METRICS
    QUARKUS --> HEALTH
    
    JPA --> H2
    JPA --> SQLITE
    PANACHE --> JPA
    
    MAVEN --> SPOTLESS
    MAVEN --> CHECKSTYLE
    MAVEN --> JACOCO
    
    DOCKER --> QUARKUS
    GITHUB --> CI_CD
    CI_CD --> DOCKER
```

## Estructura de Directorios

```mermaid
graph TD
    ROOT[polizas-service/]
    
    subgraph "Source Code"
        MAIN[src/main/java/cl/demo/polizas/]
        MAIN --> SEEDWORK[seedwork/]
        MAIN --> DOMAIN[domain/]
        MAIN --> APPLICATION[application/]
        MAIN --> APP[app/]
        
        SEEDWORK --> SEED_DOMAIN[domain/]
        SEEDWORK --> SEED_APP[application/]
        SEEDWORK --> SEED_INFRA[infrastructure/]
        
        DOMAIN --> DOMAIN_MODEL[model/]
        DOMAIN --> DOMAIN_VO[valueobject/]
        DOMAIN --> DOMAIN_REPO[repository/]
        DOMAIN --> DOMAIN_SERVICE[service/]
        DOMAIN --> DOMAIN_EVENTS[events/]
        
        APPLICATION --> APP_USECASE[usecase/]
        APPLICATION --> APP_DTO[dto/]
        
        APP --> APP_CONFIG[config/]
        APP --> APP_REST[rest/]
        APP --> APP_PERSIST[persistence/]
        APP --> APP_MAPPERS[mappers/]
        APP --> APP_VALID[validation/]
    end
    
    subgraph "Resources"
        RESOURCES[src/main/resources/]
        RESOURCES --> APP_PROPS[application.properties]
        RESOURCES --> APP_SQLITE[application-sqlite.properties]
    end
    
    subgraph "Tests"
        TEST[src/test/java/cl/demo/polizas/]
        TEST --> TEST_DOMAIN[domain/]
        TEST --> TEST_APPLICATION[application/]
        TEST --> TEST_APP[app/rest/]
    end
    
    subgraph "Configuration"
        CONFIG[pom.xml]
        CONFIG --> SPOTLESS_CONFIG[spotless.xml]
        CONFIG --> CHECKSTYLE_CONFIG[checkstyle.xml]
        CONFIG --> DOCKERFILE[Dockerfile]
        CONFIG --> DOCKER_COMPOSE[docker-compose.yml]
    end
    
    ROOT --> MAIN
    ROOT --> RESOURCES
    ROOT --> TEST
    ROOT --> CONFIG
```

## Patrones de Diseño Implementados

```mermaid
graph LR
    subgraph "Domain Patterns"
        AGGREGATE[Aggregate Root]
        VALUE_OBJECT[Value Object]
        DOMAIN_EVENT[Domain Event]
        REPOSITORY[Repository Pattern]
        SPECIFICATION[Specification Pattern]
    end
    
    subgraph "Application Patterns"
        USE_CASE[Use Case Pattern]
        COMMAND[Command Pattern]
        QUERY[Query Pattern]
        MAPPER[Mapper Pattern]
        RESULT[Result Pattern]
    end
    
    subgraph "Infrastructure Patterns"
        JPA_ENTITY[JPA Entity]
        PANACHE_REPO[Panache Repository]
        TRANSACTION[Transaction Management]
    end
    
    subgraph "Cross-Cutting"
        GUARD[Guard Pattern]
        CLOCK[Clock Pattern]
        FACTORY[Factory Pattern]
    end
    
    AGGREGATE --> VALUE_OBJECT
    AGGREGATE --> DOMAIN_EVENT
    AGGREGATE --> REPOSITORY
    
    USE_CASE --> COMMAND
    USE_CASE --> QUERY
    USE_CASE --> MAPPER
    USE_CASE --> RESULT
    
    JPA_ENTITY --> AGGREGATE
    PANACHE_REPO --> REPOSITORY
    
    GUARD --> USE_CASE
    CLOCK --> DOMAIN_EVENT
    FACTORY --> VALUE_OBJECT
```

## Métricas y Observabilidad

```mermaid
graph LR
    subgraph "Application Metrics"
        HTTP_METRICS[HTTP Metrics]
        BUSINESS_METRICS[Business Metrics]
        PERFORMANCE_METRICS[Performance Metrics]
    end
    
    subgraph "Health Checks"
        DB_HEALTH[Database Health]
        APP_HEALTH[Application Health]
        CUSTOM_HEALTH[Custom Health Checks]
    end
    
    subgraph "Logging"
        STRUCTURED_LOGS[Structured Logs]
        LOG_LEVELS[Log Levels]
        LOG_FORMAT[Log Format]
    end
    
    subgraph "Monitoring"
        PROMETHEUS[Prometheus]
        GRAFANA[Grafana]
        ALERTS[Alerts]
    end
    
    HTTP_METRICS --> PROMETHEUS
    BUSINESS_METRICS --> PROMETHEUS
    PERFORMANCE_METRICS --> PROMETHEUS
    
    DB_HEALTH --> APP_HEALTH
    APP_HEALTH --> CUSTOM_HEALTH
    
    STRUCTURED_LOGS --> LOG_FORMAT
    LOG_LEVELS --> LOG_FORMAT
    
    PROMETHEUS --> GRAFANA
    GRAFANA --> ALERTS
```

## Pipeline de CI/CD

```mermaid
graph LR
    subgraph "Source Control"
        GIT[Git Repository]
        BRANCHES[Main/Develop]
        PULL_REQUESTS[Pull Requests]
    end
    
    subgraph "CI Pipeline"
        BUILD[Build Project]
        TESTS[Run Tests]
        CODE_QUALITY[Code Quality Checks]
        SECURITY[Security Scan]
    end
    
    subgraph "CD Pipeline"
        DOCKER_BUILD[Build Docker Image]
        DOCKER_PUSH[Push to Registry]
        DEPLOY[Deploy to Environment]
        NOTIFY[Notify Team]
    end
    
    subgraph "Quality Gates"
        UNIT_TESTS[Unit Tests Pass]
        INTEGRATION_TESTS[Integration Tests Pass]
        COVERAGE[Code Coverage > 80%]
        SECURITY_PASS[Security Scan Pass]
    end
    
    GIT --> BUILD
    BUILD --> TESTS
    TESTS --> CODE_QUALITY
    CODE_QUALITY --> SECURITY
    
    SECURITY --> DOCKER_BUILD
    DOCKER_BUILD --> DOCKER_PUSH
    DOCKER_PUSH --> DEPLOY
    DEPLOY --> NOTIFY
    
    UNIT_TESTS --> TESTS
    INTEGRATION_TESTS --> TESTS
    COVERAGE --> CODE_QUALITY
    SECURITY_PASS --> SECURITY
```

## Resumen de Arquitectura

La aplicación sigue los principios de **Clean Architecture** y **Domain-Driven Design (DDD)**:

### Capas Principales:
1. **Domain Layer**: Contiene la lógica de negocio, entidades, value objects y reglas de dominio
2. **Application Layer**: Orquesta los casos de uso y coordina entre capas
3. **Infrastructure Layer**: Implementa la persistencia y servicios externos
4. **Presentation Layer**: Expone la API REST y maneja la validación de entrada

### Patrones Clave:
- **Aggregate Root**: `Policy` como raíz de agregado
- **Value Objects**: `PolicyId`, `PolicyStatus`, `Money`, `Rut`
- **Repository Pattern**: Abstracción de persistencia
- **Use Case Pattern**: Separación de responsabilidades por caso de uso
- **Domain Events**: Eventos de dominio para desacoplamiento

### Tecnologías:
- **Java 21** con **Quarkus 3.7.4**
- **H2/SQLite** para persistencia
- **JPA/Hibernate** con **Panache**
- **RESTEasy Reactive** para API REST
- **Maven** para build y gestión de dependencias

### Calidad de Código:
- **Spotless** para formateo
- **Checkstyle** para estándares de código
- **JaCoCo** para cobertura de pruebas
- **GitHub Actions** para CI/CD
