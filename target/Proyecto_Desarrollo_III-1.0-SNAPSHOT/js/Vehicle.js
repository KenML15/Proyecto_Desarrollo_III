/**
 * Vehicle.js
 * Funciones JavaScript para las vistas de Vehículos.
 * Metodología: modal de confirmación + búsqueda en tiempo real (keyup).
 */

// ── Modal de cancelación ──────────────────────────────────────────────────────

function cancelar() {
    document.getElementById("confirmBox").style.display = "flex";
}

function confirmYes() {
    var menu = document.body.dataset.menu || "menu_admin.jsp";
    window.location.href = menu;
}

function confirmNo() {
    document.getElementById("confirmBox").style.display = "none";
}

// ── Modal de eliminación ──────────────────────────────────────────────────────

var deleteUrl = "";

function confirmarEliminar(url, placa) {
    deleteUrl = url;
    document.getElementById("deleteNombre").textContent = placa;
    document.getElementById("deleteBox").style.display = "flex";
}

function deleteYes() {
    window.location.href = deleteUrl;
}

function deleteNo() {
    document.getElementById("deleteBox").style.display = "none";
}

// ── Búsqueda en tiempo real (keyup) ──────────────────────────────────────────

var searchInput = document.getElementById("search");
if (searchInput) {
    searchInput.addEventListener("keyup", function () {
        var filter = this.value.toLowerCase();
        var rows = document.querySelectorAll("#vehicleTable tbody tr");

        rows.forEach(function (row) {
            var text = row.textContent.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });
}
