# Comandos de Compilación y Ejecución - Polizas Service

## 🚀 Comandos Principales de Maven

### Compilación
```bash
# Compilar el proyecto
.\mvnw.cmd clean compile

# Compilar sin limpiar
.\mvnw.cmd compile

# Compilar y ejecutar tests
.\mvnw.cmd clean compile test

# Compilar y generar JAR
.\mvnw.cmd clean package
```

### Testing
```bash
# Ejecutar tests unitarios
.\mvnw.cmd test

# Ejecutar tests con cobertura
.\mvnw.cmd clean test jacoco:report

# Ejecutar tests específicos
.\mvnw.cmd test -Dtest=PolicyResourceTest

# Ejecutar tests con debug
.\mvnw.cmd test -Dmaven.surefire.debug
```

### Ejecución
```bash
# Ejecutar en modo desarrollo (con hot-reload)
.\mvnw.cmd quarkus:dev

# Ejecutar en modo desarrollo con debug
.\mvnw.cmd quarkus:dev -Ddebug

# Ejecutar en modo desarrollo con puerto específico
.\mvnw.cmd quarkus:dev -Dquarkus.http.port=8081

# Ejecutar en modo producción
.\mvnw.cmd quarkus:prod

# Ejecutar JAR compilado
java -jar target/quarkus-app/quarkus-run.jar
```

### Análisis de Código
```bash
# Ejecutar Checkstyle
.\mvnw.cmd checkstyle:check

# Ejecutar Spotless (formateo)
.\mvnw.cmd spotless:check

# Aplicar formateo automático
.\mvnw.cmd spotless:apply

# Generar reporte de cobertura
.\mvnw.cmd jacoco:report

# Verificar calidad del código
.\mvnw.cmd clean compile checkstyle:check spotless:check test jacoco:report
```

## 🐳 Comandos Docker

### Construir Imagen
```bash
# Construir imagen de producción
docker build -t polizas-service:latest .

# Construir imagen de desarrollo
docker build -f Dockerfile.dev -t polizas-service:dev .

# Construir con tag específico
docker build -t polizas-service:v1.0.0 .
```

### Ejecutar Contenedores
```bash
# Ejecutar con docker-compose
docker-compose up -d

# Ejecutar solo el servicio
docker-compose up polizas-service

# Ejecutar en modo desarrollo
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

# Detener servicios
docker-compose down

# Ver logs
docker-compose logs -f polizas-service
```

### Gestión de Imágenes
```bash
# Listar imágenes
docker images | grep polizas-service

# Eliminar imagen
docker rmi polizas-service:latest

# Ejecutar contenedor directamente
docker run -p 8080:8080 polizas-service:latest

# Ejecutar con variables de entorno
docker run -p 8080:8080 -e QUARKUS_PROFILE=sqlite polizas-service:latest
```

## 🔧 Comandos de Desarrollo

### Limpieza
```bash
# Limpiar target
.\mvnw.cmd clean

# Limpiar dependencias de Maven
.\mvnw.cmd dependency:purge-local-repository

# Limpiar cache de Quarkus
rm -rf target/quarkus-app
```

### Dependencias
```bash
# Actualizar dependencias
.\mvnw.cmd dependency:resolve

# Mostrar dependencias
.\mvnw.cmd dependency:tree

# Descargar dependencias
.\mvnw.cmd dependency:resolve-plugins
```

### Generación de Código
```bash
# Generar código Quarkus
.\mvnw.cmd quarkus:generate-code

# Generar código nativo
.\mvnw.cmd quarkus:generate-code -Dquarkus.package.type=native

# Generar código con perfil específico
.\mvnw.cmd quarkus:generate-code -Dquarkus.profile=sqlite
```

## 📊 Comandos de Monitoreo

### Health Checks
```bash
# Verificar salud de la aplicación
curl http://localhost:8080/health

# Verificar salud de la base de datos
curl http://localhost:8080/health/ready

# Verificar métricas
curl http://localhost:8080/metrics
```

### Logs
```bash
# Ver logs en tiempo real
.\mvnw.cmd quarkus:dev | grep -E "(ERROR|WARN|INFO)"

# Ver logs con nivel específico
.\mvnw.cmd quarkus:dev -Dquarkus.log.level=DEBUG
```

## 🚀 Comandos de Despliegue

### Build para Producción
```bash
# Build completo para producción
.\mvnw.cmd clean package -Dquarkus.package.type=jar

# Build nativo (requiere GraalVM)
.\mvnw.cmd clean package -Dquarkus.package.type=native

# Build con perfil específico
.\mvnw.cmd clean package -Dquarkus.profile=prod
```

### Verificación
```bash
# Verificar JAR generado
java -jar target/quarkus-app/quarkus-run.jar --version

# Verificar dependencias
.\mvnw.cmd dependency:analyze

# Verificar configuración
.\mvnw.cmd quarkus:config
```

## 🧪 Comandos de Testing Avanzado

### Tests de Integración
```bash
# Ejecutar tests de integración
.\mvnw.cmd verify

# Ejecutar tests con perfil específico
.\mvnw.cmd test -Dquarkus.profile=test

# Ejecutar tests con timeout extendido
.\mvnw.cmd test -Dsurefire.timeout=300
```

### Cobertura de Código
```bash
# Generar reporte HTML de cobertura
.\mvnw.cmd jacoco:report

# Verificar cobertura mínima
.\mvnw.cmd jacoco:check

# Generar reporte XML para CI/CD
.\mvnw.cmd jacoco:report -Djacoco.outputFormat=xml
```

## 🔍 Comandos de Debug

### Debug de Maven
```bash
# Debug de Maven
.\mvnw.cmd clean compile -X

# Debug de tests
.\mvnw.cmd test -X

# Debug de dependencias
.\mvnw.cmd dependency:tree -X
```

### Debug de Quarkus
```bash
# Debug de Quarkus
.\mvnw.cmd quarkus:dev -Ddebug

# Debug con puerto específico
.\mvnw.cmd quarkus:dev -Ddebug=5005

# Debug con variables de entorno
.\mvnw.cmd quarkus:dev -Dquarkus.log.level=DEBUG -Ddebug
```

## 📝 Comandos de Documentación

### Generar Documentación
```bash
# Generar JavaDoc
.\mvnw.cmd javadoc:javadoc

# Generar reporte de sitio
.\mvnw.cmd site:site

# Generar reporte de dependencias
.\mvnw.cmd dependency:analyze-report
```

## 🚨 Comandos de Emergencia

### Reset Completo
```bash
# Reset completo del proyecto
.\mvnw.cmd clean
rm -rf target/
rm -rf .quarkus/
.\mvnw.cmd clean compile
```

### Verificar Estado
```bash
# Verificar estado de Maven
.\mvnw.cmd validate

# Verificar configuración
.\mvnw.cmd help:effective-pom

# Verificar plugins
.\mvnw.cmd help:describe -Dplugin=quarkus
```

## 📋 Comandos Rápidos (Aliases Recomendados)

### Windows PowerShell
```powershell
# Crear alias para comandos comunes
Set-Alias -Name mvnw -Value ".\mvnw.cmd"
Set-Alias -Name dev -Value ".\mvnw.cmd quarkus:dev"
Set-Alias -Name build -Value ".\mvnw.cmd clean compile"
Set-Alias -Name test -Value ".\mvnw.cmd test"
Set-Alias -Name clean -Value ".\mvnw.cmd clean"
```

### Linux/Mac
```bash
# Crear alias para comandos comunes
alias mvnw='./mvnw'
alias dev='./mvnw quarkus:dev'
alias build='./mvnw clean compile'
alias test='./mvnw test'
alias clean='./mvnw clean'
```

## 🎯 Flujo de Trabajo Recomendado

### Desarrollo Diario
```bash
# 1. Limpiar y compilar
.\mvnw.cmd clean compile

# 2. Ejecutar tests
.\mvnw.cmd test

# 3. Iniciar modo desarrollo
.\mvnw.cmd quarkus:dev
```

### Antes de Commit
```bash
# 1. Verificar calidad del código
.\mvnw.cmd clean compile checkstyle:check spotless:check

# 2. Ejecutar tests
.\mvnw.cmd test

# 3. Verificar cobertura
.\mvnw.cmd jacoco:report
```

### Antes de Despliegue
```bash
# 1. Build completo
.\mvnw.cmd clean package

# 2. Verificar JAR
java -jar target/quarkus-app/quarkus-run.jar --version

# 3. Tests de integración
.\mvnw.cmd verify
```

## 📚 Recursos Adicionales

- **Documentación Quarkus**: https://quarkus.io/guides/
- **Maven Wrapper**: https://maven.apache.org/wrapper/
- **Docker Compose**: https://docs.docker.com/compose/
- **JaCoCo**: https://www.jacoco.org/jacoco/trunk/doc/
- **Checkstyle**: https://checkstyle.org/
- **Spotless**: https://github.com/diffplug/spotless
