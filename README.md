# mi-proyecto-fullstack

> **Aplicación completa (Fullstack)** para gestionar Productos y Categorías usando **Spring Boot** en el backend y **React + TypeScript + Vite** en el frontend.

---

## 📂 Estructura del repositorio

```
mi-proyecto-fullstack
├── backend/               ← Proyecto Java + Spring Boot (Hibernate/JPA)
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/tuempresa/aplicacion
│   │   │   │   ├── controller       ← Controladores REST
│   │   │   │   ├── entity           ← Entidades JPA (modelos)
│   │   │   │   ├── repository       ← Interfaces JPA (repositorios)
│   │   │   │   └── service          ← Lógica de negocio
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test                   ← Tests unitarios (opcional)
│   └── pom.xml                   ← Configuración Maven
│
├── frontend/              ← Proyecto React + TypeScript + Vite
│   ├── public
│   │   └── index.html       ← Entrada HTML principal
│   ├── src
│   │   ├── api
│   │   │   └── api.ts       ← Lógica de llamadas HTTP con Axios
│   │   ├── components
│   │   │   ├── CategorySelect.tsx
│   │   │   ├── FilterBar.tsx
│   │   │   ├── ProductForm.tsx
│   │   │   └── ProductList.tsx
│   │   ├── types
│   │   │   └── types.ts     ← Interfaces TypeScript (Producto, Categoría, etc.)
│   │   ├── App.tsx          ← Componente raíz de la aplicación
│   │   ├── App.css          ← Estilos globales básicos
│   │   └── main.tsx         ← Punto de entrada React
│   ├── tsconfig.json        ← Configuración TypeScript
│   ├── vite.config.ts       ← Configuración de Vite (proxy a /api)
│   └── package.json         ← Dependencias y scripts npm
│
├── .gitignore               ← Archivos y carpetas a ignorar en Git
└── README.md                ← Esta documentación
```

## 🔧 Prerrequisitos

Antes de comenzar, asegúrate de tener instalado en tu máquina:

1. **Java 17** (OpenJDK u otra distribución)  
2. **Maven 3.6+**  
3. **Node.js 16+** (y npm, versión recomendada igual o superior a 8.x)  
4. **SQL Server** (o alguna instancia compatible)  
5. Un editor/IDE (NetBeans, IntelliJ IDEA, VSCode, WebStorm, etc.)

---

## 🖥️ 1) Configurar y ejecutar el Backend (Spring Boot)

El backend está escrito en Java con Spring Boot, Hibernate/JPA y usa SQL Server como base de datos.

1. **Abrir terminal / PowerShell** y navegar a la carpeta backend/:
   ```bash
   cd backend
   ```

2. **Editar application.properties** en src/main/resources/ para ajustar tu conexión a SQL Server. Por ejemplo:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MiBaseDatos
   spring.datasource.username=TU_USUARIO_SQLSERVER
   spring.datasource.password=TU_CONTRASEÑA_SQLSERVER

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

   server.port=8080
   ```
   - Reemplaza MiBaseDatos, TU_USUARIO_SQLSERVER y TU_CONTRASEÑA_SQLSERVER según tu entorno.  
   - spring.jpa.hibernate.ddl-auto=update permitirá que Hibernate cree/actualice las tablas automáticamente.

3. **Construir el proyecto con Maven**:
   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

5. **Verificar que el backend está funcionando**:  
   - Abre tu navegador o Postman y prueba los endpoints:  
     - GET http://localhost:8080/api/categorias  
     - GET http://localhost:8080/api/productos  
   - Deberías recibir respuestas en formato JSON (inicialmente vacías si no hay datos).

6. *(Opcional)* Si deseas precargar datos de ejemplo, crea un archivo src/main/resources/data.sql con INSERTs para categorias y productos. Al iniciar Spring Boot, Hibernate ejecutará ese script y verás datos iniciales.

---

## 🌐 2) Configurar y ejecutar el Frontend (React + TypeScript + Vite)

El frontend consume la API REST expuesta por el backend para mostrar la lista de productos, permitir filtros y realizar CRUD.

1. **Abrir otra terminal** y navegar a la carpeta frontend/:
   ```bash
   cd frontend
   ```

2. **Instalar dependencias de Node**:
   ```bash
   npm install
   ```

3. **Iniciar Vite en modo desarrollo**:
   ```bash
   npm run dev
   ```
   - Vite levantará un servidor de desarrollo en http://localhost:3000.  
   - La configuración de vite.config.ts incluye un proxy de /api hacia http://localhost:8080, por lo que las llamadas a /api/* se redirigirán al backend sin problemas de CORS.

4. **Probar la interfaz**:  
   - Abre tu navegador en http://localhost:3000.  
   - Verás la aplicación con:  
     - Barra de filtros (buscar por nombre, rango de precio y categoría).  
     - Formulario de producto (para crear o editar).  
     - Tabla con lista de productos (cada fila muestra ID, nombre, precio, stock, categoría y botones de “Editar” / “Eliminar”).

---

## 🚀 3) Flujo de trabajo con Git Flow

Para mantener un desarrollo organizado, se utiliza Git Flow. A continuación se detallan las ramas principales y cómo trabajar con ellas:

### Ramas Principales
- main: Contiene siempre la versión estable (producción).  
- develop: Rama de desarrollo donde se integran todas las funcionalidades antes de publicar.

### Crear una nueva funcionalidad (feature)
1. Partir de develop:
   ```bash
   git checkout develop
   git pull origin develop
   ```
2. Crear la rama de feature (nombres cortos y descriptivos):
   ```bash
   git checkout -b feature/nombre-descriptivo
   ```
3. Trabajar en esa rama:
   - Agrega o modifica archivos correspondientes.  
   - Haz commits parciales y descriptivos:
     ```bash
     git add .
     git commit -m "feat(producto): agregar búsqueda por rango de precio"
     ```

### Enviar (push) la rama de feature a remoto
```bash
git push -u origin feature/nombre-descriptivo
```
- Al hacer esto, GitHub detectará que existe una nueva rama y sugerirá abrir un Pull Request.

### Abrir un Pull Request (PR)
1. En GitHub, ve a tu repositorio → pestaña Pull requests → New pull request.  
2. Asegúrate de que:  
   - base: develop  
   - compare: feature/nombre-descriptivo  
3. Describe brevemente qué hace este feature y solicita la revisión de al menos un compañero o tú mismo.  
4. Cuando esté aprobado, haz Merge Pull Request.  
5. Borra la rama de feature local y remota:
   ```bash
   git checkout develop
   git pull origin develop
   git branch -d feature/nombre-descriptivo
   git push origin --delete feature/nombre-descriptivo
   ```

### Publicar una versión estable (release) en main
1. Cuando develop contenga todas las funcionalidades que quieras publicar, crea una rama de release:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b release/v1.0.0
   ```
2. Actualiza las versiones en:
   - backend/pom.xml (por ejemplo, 0.0.1-SNAPSHOT → 1.0.0)  
   - frontend/package.json (por ejemplo, "version": "0.0.1" → "version": "1.0.0")  
3. Haz commit de esos cambios:
   ```bash
   git add backend/pom.xml frontend/package.json
   git commit -m "chore(release): bump version a 1.0.0"
   ```
4. Push de la rama de release:
   ```bash
   git push -u origin release/v1.0.0
   ```
5. Abrir un PR desde release/v1.0.0 hacia main en GitHub.  
6. Una vez aprobado, mergear en main. Después:
   ```bash
   git checkout main
   git pull origin main
   git tag -a v1.0.0 -m "Release v1.0.0"
   git push origin main --tags
   ```
7. Fusionar los cambios de vuelta a develop:
   ```bash
   git checkout develop
   git merge --no-ff release/v1.0.0
   git push origin develop
   ```
8. Borrar la rama de release:
   ```bash
   git branch -d release/v1.0.0
   git push origin --delete release/v1.0.0
   ```

### Hotfix (corrección urgente en producción)
Si detectas un bug en producción que no puede esperar hasta el siguiente release, crea una rama hotfix/x.y.z a partir de main, corrige, PR → main, taggea la versión parche, luego fusiona de regreso a develop.

---

## 📋 4) Mensajes de commit claros y descriptivos
Para facilitar el historial y que cualquiera entienda rápidamente el objetivo de cada cambio, sigue esta convención:

- **feat(ámbito):** Nueva funcionalidad.  
  > Ejemplo: feat(producto): implementar búsqueda por rango de precio
- **fix(ámbito):** Corrección de bug.  
  > Ejemplo: fix(categoria): corregir validación de nombre duplicado
- **refactor(ámbito):** Refactorización sin cambiar comportamiento.  
  > Ejemplo: refactor(service): extraer lógica de validación a utilitario común
- **chore(ámbito):** Tareas de mantenimiento (actualizar dependencias, cambiar estructura).  
  > Ejemplo: chore(restructure): mover backend/ y frontend/ a la raíz del repo

> **Nota:** ámbito puede ser un módulo, entidad o carpeta como producto, categoria, api, etc.
