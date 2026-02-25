# 🎭 ImpostGame API

API REST desarrollada con Spring Boot para gestionar el juego presencial "Impostor".  
El sistema se encarga de administrar salas, jugadores, roles, votaciones y rondas.

---

# 📌 Objetivo del Proyecto

Proveer un backend estructurado que permita:

- Crear salas de juego
- Unir jugadores a una sala
- Iniciar partidas
- Asignar roles (Impostor / Civil)
- Registrar votaciones
- Expulsar jugadores
- Determinar ganador

Este proyecto está diseñado bajo arquitectura en capas siguiendo buenas prácticas de desarrollo backend.

---

# 🏗 Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:
controller → service → repository → database


Cada capa tiene una responsabilidad específica.

---

# 📂 Estructura de Carpetas
src/main/java/com/impostgame
│
├── controller
├── service
├── repository
├── model
├── dto
└── ImpostGameApplication.java


---

## 📌 controller

Contiene los controladores REST.

Responsabilidad:
- Exponer los endpoints HTTP
- Recibir solicitudes del cliente
- Validar datos de entrada
- Delegar la lógica al service
- Retornar respuestas HTTP

Ejemplo:
- `RoomController`
- `GameController`
- `VoteController`

Los controladores **NO deben contener lógica de negocio**, solo coordinación.

---

## 📌 service

Contiene la lógica de negocio del sistema.

Responsabilidad:
- Implementar reglas del juego
- Validar estados
- Coordinar operaciones entre entidades
- Controlar flujo de rondas

Ejemplo:
- Crear sala
- Asignar roles aleatorios
- Verificar si hay ganador
- Procesar votaciones

Es la capa más importante del backend.

---

## 📌 repository

Contiene interfaces que extienden `JpaRepository`.

Responsabilidad:
- Comunicación directa con la base de datos
- CRUD automático
- Consultas personalizadas

Ejemplo:

```java
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByCode(String code);
}
