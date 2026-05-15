"use strict";

/* ─────────────────────────────────────────────────────────
   1. MENÚ HAMBURGUESA (mobile)
───────────────────────────────────────────────────────── */

var hamburger = document.getElementById("hamburger");
var navMenu   = document.getElementById("nav-menu");

if (hamburger && navMenu) {

  hamburger.addEventListener("click", function () {

    var estaAbierto =
      navMenu.classList.contains("abierto");

    navMenu.classList.toggle("abierto");
    hamburger.classList.toggle("abierto");

    hamburger.setAttribute(
      "aria-expanded",
      !estaAbierto
    );

  });

  var enlacesMenu =
    navMenu.querySelectorAll(".nav-link");

  enlacesMenu.forEach(function (enlace) {

    enlace.addEventListener("click", function () {

      navMenu.classList.remove("abierto");
      hamburger.classList.remove("abierto");

      hamburger.setAttribute(
        "aria-expanded",
        "false"
      );

    });

  });

}


/* ─────────────────────────────────────────────────────────
   2. ANIMACIONES REVEAL
───────────────────────────────────────────────────────── */

function activarReveal(seccion) {

  if (!seccion) return;

  var elementos =
    seccion.querySelectorAll(".reveal");

  elementos.forEach(function (el) {
    el.classList.add("visible");
  });

}


/* ─────────────────────────────────────────────────────────
   3. ACTUALIZAR NAV ACTIVA
───────────────────────────────────────────────────────── */

function actualizarNavActiva(ancla) {

  var enlaces =
    document.querySelectorAll(".nav-link");

  enlaces.forEach(function (enlace) {

    var anclaEnlace =
      enlace.getAttribute("data-menuanchor");

    if (anclaEnlace === ancla) {
      enlace.classList.add("active");
    }

    else {
      enlace.classList.remove("active");
    }

  });

}


/* ─────────────────────────────────────────────────────────
   4. INTERACTIVIDAD TABLERO
───────────────────────────────────────────────────────── */

function iniciarTablero() {

  var celdas =
    document.querySelectorAll(".celda.vacia");

  celdas.forEach(function (celda) {

    celda.addEventListener(
      "mouseenter",
      function () {
        celda.textContent = "?";
      }
    );

    celda.addEventListener(
      "mouseleave",
      function () {
        celda.textContent = "—";
      }
    );

  });

}


/* ─────────────────────────────────────────────────────────
   5. FULLPAGE.JS
   CONFIG ESTABLE Y SIN SALTOS
───────────────────────────────────────────────────────── */

var paginaCompleta = new fullpage("#fullpage", {

  /* ─────────────────────
     SCROLL
  ───────────────────── */

  autoScrolling: true,

  fitToSection: true,

  /* IMPORTANTE:
     Evita saltos entre secciones */
  fitToSectionDelay: 900,

  /* Scroll más estable */
  scrollingSpeed: 700,

  /* MUY IMPORTANTE:
     Desactivar CSS3 evita
     acumulación de eventos */
  css3: false,

  easingcss3:
    "cubic-bezier(0.77, 0, 0.175, 1)",

  /* Sensibilidad táctil */
  touchSensitivity: 15,

  /* Evita bugs con secciones grandes */
  bigSectionsDestination: null,

  /* Evita loops raros */
  loopBottom: false,
  loopTop: false,

  /* Mejor estabilidad wheel */
  scrollOverflow: false,

  /* IMPORTANTE:
     No saltar secciones */
  skipIntermediateItems: false,

  /* ─────────────────────
     ANCLAS
  ───────────────────── */

  anchors: [
    "inicio",
    "que-es",
    "mecanica",
    "galeria",
    "jugar"
  ],

  /* ─────────────────────
     NAVEGACIÓN
  ───────────────────── */

  navigation: true,

  navigationPosition: "right",

  navigationTooltips: [
    "Inicio",
    "El Juego",
    "Mecánica",
    "Galería",
    "Jugar"
  ],

  showActiveTooltip: true,

  /* ─────────────────────
     SECCIONES
  ───────────────────── */

  sectionsColor: [
    "#1E1E1E",
    "#1E1E1E",
    "#532214",
    "#3D505C",
    "#1E1E1E"
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
    // espacio preparado
  }

});


/* ─────────────────────────────────────────────────────────
   6. INICIALIZACIÓN
───────────────────────────────────────────────────────── */

document.addEventListener(
  "DOMContentLoaded",
  function () {

    var hero =
      document.getElementById("inicio");

    if (hero) {
      activarReveal(hero);
    }

    iniciarTablero();

  }
);


/* ─────────────────────────────────────────────────────────
   7. PANELES EXPANDIBLES
───────────────────────────────────────────────────────── */

var panels =
  document.querySelectorAll(".panel");

function removeActiveClasses() {

  panels.forEach(function (panel) {
    panel.classList.remove("active");
  });

}

panels.forEach(function (panel) {

  panel.addEventListener("click", function () {

    removeActiveClasses();

    panel.classList.add("active");

  });

});