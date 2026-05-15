/* ============================================================
   BRIGHT FALLS — galeria.js
   Carrusel estable optimizado para FullPage.js
   Funciones:
     - Navegación manual
     - Indicadores sincronizados
     - Swipe táctil estable
     - Compatible con FullPage.js
     - Responsive robusto
   ============================================================ */

"use strict";

document.addEventListener("DOMContentLoaded", function () {

  /* ─────────────────────────────────────────
     1. ELEMENTOS DOM
  ───────────────────────────────────────── */

  var track =
    document.getElementById("galeria-track");

  var btnPrev =
    document.getElementById("btn-prev");

  var btnNext =
    document.getElementById("btn-next");

  var contenedor =
    document.getElementById("galeria-puntos");

  if (
    !track ||
    !btnPrev ||
    !btnNext ||
    !contenedor
  ) {
    return;
  }

  var tarjetas =
    track.querySelectorAll(".galeria-tarjeta");

  var total = tarjetas.length;

  /* ─────────────────────────────────────────
     2. ESTADO
  ───────────────────────────────────────── */

  var indiceActual = 0;

  var inicioToque = 0;

  var moviendo = false;

  /* ─────────────────────────────────────────
     3. CREAR PUNTOS
  ───────────────────────────────────────── */

  function crearPuntos() {

    contenedor.innerHTML = "";

    for (var i = 0; i < total; i++) {

      var punto =
        document.createElement("button");

      punto.classList.add("punto");

      punto.setAttribute("role", "tab");

      punto.setAttribute(
        "aria-label",
        "Ir a imagen " + (i + 1)
      );

      punto.setAttribute(
        "aria-selected",
        i === 0 ? "true" : "false"
      );

      punto.dataset.indice = i;

      punto.addEventListener(
        "click",
        function (e) {

          var idx = parseInt(
            e.currentTarget.dataset.indice
          );

          irA(idx);

        }
      );

      contenedor.appendChild(punto);

    }

    actualizarPuntos();

  }

  /* ─────────────────────────────────────────
     4. ACTUALIZAR PUNTOS
  ───────────────────────────────────────── */

  function actualizarPuntos() {

    var puntos =
      contenedor.querySelectorAll(".punto");

    puntos.forEach(function (p, i) {

      if (i === indiceActual) {

        p.classList.add("activo");

        p.setAttribute(
          "aria-selected",
          "true"
        );

      }

      else {

        p.classList.remove("activo");

        p.setAttribute(
          "aria-selected",
          "false"
        );

      }

    });

  }

  /* ─────────────────────────────────────────
     5. MOVER CARRUSEL
     SOLUCIÓN RESPONSIVE REAL
  ───────────────────────────────────────── */

  function mover() {

    if (!tarjetas[indiceActual]) return;

    moviendo = true;

    var offset =
      tarjetas[indiceActual].offsetLeft;

    track.style.transform =
      "translateX(-" + offset + "px)";

    tarjetas.forEach(function (t, i) {

      t.setAttribute(
        "aria-hidden",
        i !== indiceActual ? "true" : "false"
      );

    });

    actualizarPuntos();

    setTimeout(function () {
      moviendo = false;
    }, 500);

  }

  /* ─────────────────────────────────────────
     6. NAVEGACIÓN
  ───────────────────────────────────────── */

  function irA(indice) {

    if (moviendo) return;

    indiceActual = Math.max(
      0,
      Math.min(indice, total - 1)
    );

    mover();

  }

  function siguiente() {

    if (indiceActual < total - 1) {
      irA(indiceActual + 1);
    }

  }

  function anterior() {

    if (indiceActual > 0) {
      irA(indiceActual - 1);
    }

  }

  /* ─────────────────────────────────────────
     7. BOTONES
  ───────────────────────────────────────── */

  btnNext.addEventListener(
    "click",
    function (e) {

      e.preventDefault();
      e.stopPropagation();

      siguiente();

    }
  );

  btnPrev.addEventListener(
    "click",
    function (e) {

      e.preventDefault();
      e.stopPropagation();

      anterior();

    }
  );

  /* ─────────────────────────────────────────
     8. TECLADO
     SOLO CUANDO EL TRACK TIENE FOCUS
  ───────────────────────────────────────── */

  track.setAttribute("tabindex", "0");

  track.addEventListener(
    "keydown",
    function (e) {

      if (e.key === "ArrowRight") {

        e.preventDefault();

        siguiente();

      }

      if (e.key === "ArrowLeft") {

        e.preventDefault();

        anterior();

      }

    }
  );

  /* ─────────────────────────────────────────
     9. SWIPE TÁCTIL
     COMPATIBLE CON FULLPAGE
  ───────────────────────────────────────── */

  track.addEventListener(
    "touchstart",
    function (e) {

      inicioToque =
        e.touches[0].clientX;

    },
    { passive: true }
  );

  track.addEventListener(
    "touchmove",
    function (e) {

      /* MUY IMPORTANTE:
         evita conflictos con FullPage */

      e.stopPropagation();

    },
    { passive: true }
  );

  track.addEventListener(
    "touchend",
    function (e) {

      var finToque =
        e.changedTouches[0].clientX;

      var diferencia =
        inicioToque - finToque;

      /* Umbral swipe */
      if (diferencia > 60) {

        siguiente();

      }

      else if (diferencia < -60) {

        anterior();

      }

    },
    { passive: true }
  );

  /* ─────────────────────────────────────────
     10. RESIZE
  ───────────────────────────────────────── */

  var resizeTimeout;

  window.addEventListener(
    "resize",
    function () {

      clearTimeout(resizeTimeout);

      resizeTimeout = setTimeout(
        function () {

          mover();

        },
        120
      );

    }
  );

  /* ─────────────────────────────────────────
     11. INICIALIZAR
  ───────────────────────────────────────── */

  crearPuntos();

  mover();

});