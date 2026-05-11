/**
 * Assignment.js
 * Funciones JavaScript para las vistas de Asignaciones y Ocupación.
 * Metodología: modal de confirmación de salida + búsqueda en tiempo real (keyup).
 */

// ── Modal de cancelación de formulario ────────────────────────────────────────

function cancelar() {
    document.getElementById("confirmBox").style.display = "flex";
}

function confirmYes() {
    var menu = document.body.dataset.menu || "menu_clerk.jsp";
    window.location.href = menu;
}

function confirmNo() {
    document.getElementById("confirmBox").style.display = "none";
}

// ── Modal de confirmación de salida de vehículo ───────────────────────────────

var releaseUrl = "";

function confirmarSalida(url, placa) {
    releaseUrl = url;
    document.getElementById("releasePlaca").textContent = placa;
    document.getElementById("releaseBox").style.display = "flex";
}

function releaseYes() {
    window.location.href = releaseUrl;
}

function releaseNo() {
    document.getElementById("releaseBox").style.display = "none";
}

// ── Búsqueda en tiempo real (keyup) ──────────────────────────────────────────

var searchInput = document.getElementById("search");
if (searchInput) {
    searchInput.addEventListener("keyup", function () {
        var filter = this.value.toLowerCase();
        var rows = document.querySelectorAll("#assignmentTable tbody tr");

        rows.forEach(function (row) {
            var text = row.textContent.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });
}
