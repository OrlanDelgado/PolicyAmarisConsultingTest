# 🚀 Integración con GitHub - Polizas Service

## 📋 Prerrequisitos

### 1. Instalar Git
```bash
# Windows (con Chocolatey)
choco install git

# Windows (descarga directa)
# https://git-scm.com/download/win

# Verificar instalación
git --version
```

### 2. Configurar Git
```bash
# Configurar usuario global
git config --global user.name "Tu Nombre"
git config --global user.email "tu.email@ejemplo.com"

# Configurar editor preferido
git config --global core.editor "code --wait"

# Configurar rama por defecto
git config --global init.defaultBranch main

# Verificar configuración
git config --list
```

## 🔧 Configuración del Repositorio

### 1. Crear Repositorio en GitHub
1. Ve a [GitHub.com](https://github.com)
2. Haz clic en "New repository"
3. Nombre: `polizas-service`
4. Descripción: `Backend service for insurance policy management using Quarkus and DDD`
5. **NO** inicialices con README, .gitignore o licencia
6. Haz clic en "Create repository"

### 2. Inicializar Repositorio Local
```bash
# En el directorio del proyecto
git init

# Agregar archivos
git add .

# Primer commit
git commit -m "🎉 Initial commit: Polizas Service with Quarkus and DDD"

# Agregar remote
git remote add origin https://github.com/TU_USUARIO/polizas-service.git

# Cambiar a rama main
git branch -M main

# Push inicial
git push -u origin main
```

## 🔄 Flujo de Trabajo Git

### 1. Estructura de Ramas Recomendada
```bash
# Rama principal
main          # Código de producción
develop       # Código de desarrollo integrado
feature/*     # Nuevas funcionalidades
hotfix/*      # Correcciones urgentes
release/*     # Preparación de releases
```

### 2. Crear y Gestionar Ramas
```bash
# Crear rama de desarrollo
git checkout -b develop
git push -u origin develop

# Crear rama de feature
git checkout -b feature/implement-policy-validation
git push -u origin feature/implement-policy-validation

# Crear rama de hotfix
git checkout -b hotfix/fix-rut-validation
git push -u origin hotfix/fix-rut-validation
```

### 3. Flujo de Trabajo Diario
```bash
# 1. Actualizar rama base
git checkout develop
git pull origin develop

# 2. Crear rama de feature
git checkout -b feature/nueva-funcionalidad

# 3. Hacer cambios y commits
git add .
git commit -m "✨ Add new functionality"

# 4. Push de la rama
git push -u origin feature/nueva-funcionalidad

# 5. Crear Pull Request en GitHub
# 6. Code Review y Merge
# 7. Eliminar rama local
git checkout develop
git pull origin develop
git branch -d feature/nueva-funcionalidad
```

## 🚀 Configuración de CI/CD

### 1. GitHub Actions Workflow
El archivo `.github/workflows/ci-cd.yml` ya está configurado con:

- **Build y Test**: Compilación, tests y análisis de código
- **Code Quality**: Checkstyle, Spotless y JaCoCo
- **Security**: Escaneo de vulnerabilidades
- **Docker**: Construcción y push de imagen
- **Deployment**: Despliegue automático

### 2. Secrets Necesarios
Configura estos secrets en tu repositorio GitHub:

```bash
# En GitHub: Settings > Secrets and variables > Actions

# Docker Hub
DOCKER_USERNAME=tu_usuario_docker
DOCKER_PASSWORD=tu_password_docker

# SonarQube (opcional)
SONAR_TOKEN=tu_token_sonar

# Deployment (opcional)
DEPLOY_KEY=tu_clave_ssh
DEPLOY_HOST=tu_servidor
```

### 3. Configurar Protección de Ramas
```bash
# En GitHub: Settings > Branches > Add rule

# Rama main
- Require pull request reviews before merging
- Require status checks to pass before merging
- Require branches to be up to date before merging
- Include administrators
- Restrict pushes that create files
- Allow force pushes: Never
- Allow deletions: Never

# Rama develop
- Require pull request reviews before merging
- Require status checks to pass before merging
- Allow force pushes: Never
```

## 📊 Monitoreo y Calidad

### 1. SonarQube Integration
```yaml
# En .github/workflows/ci-cd.yml
- name: SonarQube Analysis
  uses: sonarqube-quality-gate-action@master
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
    SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
```

### 2. Code Coverage
```yaml
# Configuración JaCoCo en pom.xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 3. Badges para README
```markdown
# Agregar al README.md
[![Build Status](https://github.com/TU_USUARIO/polizas-service/workflows/CI%2FCD/badge.svg)](https://github.com/TU_USUARIO/polizas-service/actions)
[![Code Coverage](https://codecov.io/gh/TU_USUARIO/polizas-service/branch/main/graph/badge.svg)](https://codecov.io/gh/TU_USUARIO/polizas-service)
[![Quality Gate](https://sonarcloud.io/api/project_badges/quality_gate?project=TU_USUARIO_polizas-service)](https://sonarcloud.io/dashboard?id=TU_USUARIO_polizas-service)
```

## 🔒 Seguridad y Mejores Prácticas

### 1. Git Hooks
```bash
# El archivo .git/hooks/pre-commit ya está configurado
# Ejecuta automáticamente:
# - mvn validate
# - mvn test
```

### 2. Dependabot
```yaml
# Crear .github/dependabot.yml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 10
```

### 3. Security Scanning
```yaml
# En .github/workflows/ci-cd.yml
- name: Run Trivy vulnerability scanner
  uses: aquasecurity/trivy-action@master
  with:
    image-ref: 'polizas-service:latest'
    format: 'sarif'
    output: 'trivy-results.sarif'
```

## 📝 Templates y Configuración

### 1. Pull Request Template
```markdown
# Crear .github/pull_request_template.md
## 📋 Descripción
Breve descripción de los cambios realizados

## 🎯 Tipo de Cambio
- [ ] Bug fix
- [ ] Nueva funcionalidad
- [ ] Breaking change
- [ ] Documentación

## 🧪 Tests
- [ ] Tests unitarios pasan
- [ ] Tests de integración pasan
- [ ] Cobertura > 80%

## 📸 Screenshots (si aplica)

## ✅ Checklist
- [ ] Código sigue estándares del proyecto
- [ ] Documentación actualizada
- [ ] Tests agregados/actualizados
- [ ] No hay warnings de compilación
```

### 2. Issue Templates
```markdown
# Crear .github/ISSUE_TEMPLATE/bug_report.md
---
name: Bug report
about: Crear un reporte de bug
title: '[BUG] '
labels: 'bug'
assignees: ''

---

**Descripción del Bug**
Descripción clara y concisa del bug

**Pasos para Reproducir**
1. Ir a '...'
2. Hacer clic en '....'
3. Ver error

**Comportamiento Esperado**
Descripción de lo que debería pasar

**Screenshots**
Si aplica, agregar screenshots

**Entorno:**
 - OS: [e.g. Windows 10]
 - Java: [e.g. 21]
 - Quarkus: [e.g. 3.7.4]
```

## 🚀 Automatización Avanzada

### 1. Auto-release
```yaml
# En .github/workflows/ci-cd.yml
- name: Create Release
  uses: actions/create-release@v1
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
  with:
    tag_name: v${{ github.run_number }}
    release_name: Release v${{ github.run_number }}
    body: |
      Changes in this Release:
      ${{ github.event.head_commit.message }}
    draft: false
    prerelease: false
```

### 2. Auto-deploy
```yaml
# En .github/workflows/ci-cd.yml
- name: Deploy to Production
  if: github.ref == 'refs/heads/main'
  run: |
    # Comandos de despliegue
    ssh ${{ secrets.DEPLOY_USER }}@${{ secrets.DEPLOY_HOST }} << 'EOF'
      cd /opt/polizas-service
      git pull origin main
      docker-compose down
      docker-compose up -d
    EOF
```

## 📊 Métricas y Reportes

### 1. GitHub Insights
- **Pulse**: Actividad del repositorio
- **Contributors**: Quienes contribuyen
- **Traffic**: Visitas y clones
- **Commits**: Frecuencia de commits

### 2. Code Quality Metrics
```bash
# Generar reportes localmente
.\mvnw.cmd jacoco:report
.\mvnw.cmd checkstyle:check
.\mvnw.cmd spotless:check

# Ver reportes en target/
# - target/site/jacoco/index.html
# - target/checkstyle-result.xml
# - target/spotless/spotless-check.html
```

## 🆘 Troubleshooting

### 1. Problemas Comunes
```bash
# Error: Authentication failed
git config --global credential.helper store
git push origin main

# Error: Permission denied
# Verificar que tienes acceso al repositorio

# Error: Branch protection
# Crear Pull Request en lugar de push directo

# Error: CI/CD fails
# Verificar logs en Actions tab
```

### 2. Comandos de Recuperación
```bash
# Resetear último commit
git reset --soft HEAD~1

# Deshacer cambios no committeados
git checkout -- .

# Forzar push (solo en emergencias)
git push --force-with-lease origin main

# Ver historial de commits
git log --oneline --graph --all
```

## 📚 Recursos Adicionales

- **GitHub Guides**: https://guides.github.com/
- **Git Flow**: https://nvie.com/posts/a-successful-git-branching-model/
- **Conventional Commits**: https://www.conventionalcommits.org/
- **GitHub Actions**: https://docs.github.com/en/actions
- **SonarQube**: https://docs.sonarqube.org/
- **Dependabot**: https://dependabot.com/

## 🎯 Próximos Pasos

1. **Configurar repositorio en GitHub**
2. **Configurar secrets y variables**
3. **Configurar protección de ramas**
4. **Configurar SonarQube (opcional)**
5. **Configurar Dependabot**
6. **Configurar auto-deploy**
7. **Configurar monitoreo y alertas**

---

**¡Con esta configuración tendrás un pipeline de CI/CD completo y profesional! 🚀**
