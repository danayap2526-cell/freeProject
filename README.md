# Bright Falls - Videojuego 2D Narrativo

## 📝 Descripción del Proyecto
**Bright Falls** es una propuesta innovadora que transforma el clásico juego del "tres en raya" en una experiencia narrativa evolutiva. [cite_start]Desarrollado en **Java**, el juego utiliza el tablero como un motor para avanzar en una historia: conforme el jugador gana partidas, desbloquea fragmentos de una trama oculta[cite: 36]. 

El objetivo es demostrar cómo una mecánica simple puede ganar profundidad mediante una narrativa inmersiva y un sistema de recompensas dinámico.

## 🚀 Funcionalidades Principales
* **Lógica de Juego 2D**: Motor de juego sólido basado en las reglas del tres en raya.
* **Sistema de Progresión**: Desbloqueo de fragmentos de historia condicionado a las victorias del usuario.
* [cite_start]**Persistencia con MySQL**: Guardado de puntuaciones, nombres de jugadores y, lo más importante, el progreso de la historia[cite: 17].
* **Interfaz Gráfica (GUI)**: Menús, tableros y ventanas interactivas creadas con Java Swing.

## 🛠️ Tecnologías Utilizadas
* [cite_start]**Lenguaje**: Java[cite: 18].
* **Interfaz**: Java Swing (Gestión de eventos de teclado y ratón).
* [cite_start]**Base de Datos**: MySQL (Persistencia y gestión de datos)[cite: 17].
* **Conectividad**: JDBC para la integración Java-MySQL.

## 👤 Mi Contribución Personal
[cite_start]En este proyecto intermodular he desempeñado un rol integral como **Diseñador y Programador**[cite: 2, 23]:
1.  **Arquitectura y Lógica**: Programación del motor del juego y las condiciones de victoria en Java.
2.  [cite_start]**Diseño UI/UX**: Creación de la estética visual y la narrativa del juego[cite: 23].
3.  **Backend**: Diseño del esquema de la base de datos en MySQL y conexión con el frontend.

## 🧩 Problemas Encontrados y Soluciones
* **Desafío**: Mantener el progreso del usuario tras cerrar la sesión.
    * **Solución**: Implementé una lógica de consulta en MySQL que verifica el último fragmento de historia desbloqueado al iniciar la aplicación.
* **Desafío**: Sincronización de la interfaz Swing con la lógica del juego.
    * **Solución**: Utilicé una estructura modular para asegurar que la interfaz se refresque automáticamente ante cada evento de usuario (clic o movimiento).

## 🎓 Aprendizajes
Este desarrollo me ha permitido consolidar competencias clave:
* **Modularización**: Organización de código profesional para proyectos escalables.
* **Gestión de Datos**: Experiencia real conectando aplicaciones de escritorio con bases de datos relacionales.
* **Visión de Producto**: Capacidad para diseñar una experiencia de usuario completa, desde la idea hasta el despliegue técnico.
