"use strict";

/* ─────────────────────────────────────────────────────────
   1. MENÚ HAMBURGUESA (mobile)
───────────────────────────────────────────────────────── */

var hamburger = document.getElementById("hamburger");
var navMenu   = document.getElementById("nav-menu");

if (hamburger && navMenu) {

  hamburger.addEventListener("click", function () {
    var estaAbierto = navMenu.classList.contains("abierto");
    navMenu.classList.toggle("abierto");
    hamburger.classList.toggle("abierto");
    hamburger.setAttribute("aria-expanded", !estaAbierto);
  });

  var enlacesMenu = navMenu.querySelectorAll(".nav-link");
  enlacesMenu.forEach(function (enlace) {
    enlace.addEventListener("click", function () {
      navMenu.classList.remove("abierto");
      hamburger.classList.remove("abierto");
      hamburger.setAttribute("aria-expanded", "false");
    });
  });
}


/* ─────────────────────────────────────────────────────────
   2. ANIMACIONES REVEAL
───────────────────────────────────────────────────────── */

function activarReveal(seccion) {
  if (!seccion) return;
  var elementos = seccion.querySelectorAll(".reveal");
  elementos.forEach(function (el) {
    el.classList.add("visible");
  });
}


/* ─────────────────────────────────────────────────────────
   3. ACTUALIZAR NAV ACTIVA
───────────────────────────────────────────────────────── */

function actualizarNavActiva(ancla) {
  var enlaces = document.querySelectorAll(".nav-link");
  enlaces.forEach(function (enlace) {
    var anclaEnlace = enlace.getAttribute("data-menuanchor");
    if (anclaEnlace === ancla) {
      enlace.classList.add("active");
    } else {
      enlace.classList.remove("active");
    }
  });
}


/* ─────────────────────────────────────────────────────────
   4. INTERACTIVIDAD TABLERO
───────────────────────────────────────────────────────── */

function iniciarTablero() {
  var celdas = document.querySelectorAll(".celda.vacia");
  celdas.forEach(function (celda) {
    celda.addEventListener("mouseenter", function () {
      celda.textContent = "?";
    });
    celda.addEventListener("mouseleave", function () {
      celda.textContent = "—";
    });
  });
}


/* ─────────────────────────────────────────────────────────
   5. BARRA DE URGENCIA — contador regresivo
   Genera sensación de escasez temporal
───────────────────────────────────────────────────────── */

function iniciarBarraUrgencia() {

  var barra   = document.getElementById("barra-urgencia");
  var cerrar  = document.getElementById("cerrar-urgencia");
  var contador = document.getElementById("contador-urgencia");

  /* Cierre de la barra */
  if (cerrar && barra) {
    cerrar.addEventListener("click", function () {
      barra.style.transition = "opacity 0.3s ease, max-height 0.4s ease";
      barra.style.opacity    = "0";
      barra.style.maxHeight  = "0";
      barra.style.overflow   = "hidden";
      barra.setAttribute("aria-hidden", "true");
    });
  }

  /* Contador: calcula días restantes hasta el próximo domingo */
  if (contador) {
    var ahora   = new Date();
    var diasSem = ahora.getDay();              /* 0=dom … 6=sab */
    var restantes = (7 - diasSem) % 7 || 7;   /* días hasta el próximo domingo */
    var horas   = 23 - ahora.getHours();
    var texto   = restantes === 1
      ? " menos de 24h"
      : " " + restantes + " d\xEDas";
    contador.textContent = texto;
  }

  /* Plazas restantes: número pseudodinámico basado en hora del día
     para dar sensación de que el stock va bajando */
  var plazas = document.getElementById("plazas-restantes");
  if (plazas) {
    var hora = new Date().getHours();
    /* Entre 9 y 22h las plazas "bajan" progresivamente */
    var base = 63;
    var consumidas = Math.max(0, hora - 8) * 2;
    var restantesPlazas = Math.max(12, base - consumidas);
    plazas.textContent = restantesPlazas;
  }
}


/* ─────────────────────────────────────────────────────────
   6. FULLPAGE.JS
   CONFIG ESTABLE Y SIN SALTOS
   — Nueva sección "testimonios" añadida entre mecanica y galeria
───────────────────────────────────────────────────────── */

/* Esperamos a que el DOM esté listo para iniciar fullPage */
document.addEventListener("DOMContentLoaded", function () {

  /* Primero inicializamos los módulos no-fullpage */
  iniciarTablero();
  iniciarBarraUrgencia();

  /* Activar reveal en la sección hero inmediatamente */
  var hero = document.getElementById("inicio");
  if (hero) {
    activarReveal(hero);
  }

  /* Iniciar fullPage solo si la librería está disponible */
  if (typeof fullpage === "undefined") {
    console.warn("[BF] fullPage.js no cargado aún. Reintentando en 500ms...");
    setTimeout(function () {
      iniciarFullPage();
    }, 500);
  } else {
    iniciarFullPage();
  }

});

function iniciarFullPage() {

  if (typeof fullpage === "undefined") {
    console.error("[BF] fullPage.js no disponible. Comprueba vendor/fullpage.min.js");
    return;
  }

  new fullpage("#fullpage", {

    /* ─────────────────────
       SCROLL
    ───────────────────── */
    licenseKey: "Desarrollado por Bright Falls",
    autoScrolling:       true,
    fitToSection:        true,
    fitToSectionDelay:   900,
    scrollingSpeed:      700,

    /* Desactivar CSS3 evita acumulación de eventos */
    css3:                false,
    easingcss3:          "cubic-bezier(0.77, 0, 0.175, 1)",

    touchSensitivity:    15,
    bigSectionsDestination: null,
    loopBottom:          false,
    loopTop:             false,
    scrollOverflow:      false,
    skipIntermediateItems: false,

    /* ─────────────────────
       ANCLAS — incluye la nueva sección testimonios
    ───────────────────── */
    anchors: [
      "inicio",
      "que-es",
      "mecanica",
      "testimonios",
      "galeria",
      "jugar"
    ],

    /* ─────────────────────
       NAVEGACIÓN LATERAL
    ───────────────────── */
    navigation:          true,
    navigationPosition:  "right",
    navigationTooltips: [
      "Inicio",
      "El Juego",
      "Mec\u00E1nica",
      "Opiniones",
      "Galer\u00EDa",
      "Jugar"
    ],
    showActiveTooltip:   true,

    /* ─────────────────────
       COLORES POR SECCIÓN
    ───────────────────── */
    sectionsColor: [
      "#1E1E1E",  /* inicio      */
      "#1E1E1E",  /* que-es      */
      "#532214",  /* mecanica    */
      "#0F2D1A",  /* testimonios — verde oscuro */
      "#3D505C",  /* galeria     */
      "#1E1E1E"   /* jugar       */
    ],

    verticalCentered: true,

    /* ─────────────────────
       CALLBACKS
    ───────────────────── */
    afterLoad: function (origen, destino) {
      var seccion = destino.item;
      var ancla   = destino.anchor;
      activarReveal(seccion);
      actualizarNavActiva(ancla);
    },

    onLeave: function () {
      /* espacio preparado para futuros efectos */
    }

  });
}


/* ─────────────────────────────────────────────────────────
   7. PANELES EXPANDIBLES (Galería)
───────────────────────────────────────────────────────── */

var panels = document.querySelectorAll(".panel");

function removeActiveClasses() {
  panels.forEach(function (panel) {
    panel.classList.remove("active");
  });
}

panels.forEach(function (panel) {
  /* Click */
  panel.addEventListener("click", function () {
    removeActiveClasses();
    panel.classList.add("active");
  });

  /* Teclado: Enter/Espacio para accesibilidad */
  panel.setAttribute("tabindex", "0");
  panel.addEventListener("keydown", function (e) {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      removeActiveClasses();
      panel.classList.add("active");
    }
  });
});
