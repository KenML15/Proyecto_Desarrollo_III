function cancelar() {
    document.getElementById("confirmBox").style.display = "flex";
}

function confirmYes() {
    window.location.href = "menu_admin.jsp";
}

function confirmNo() {
    document.getElementById("confirmBox").style.display = "none";
}

var deleteUrl = "";

function confirmarEliminar(url, nombre) {
    deleteUrl = url;
    document.getElementById("deleteNombre").textContent = nombre;
    document.getElementById("deleteBox").style.display = "flex";
}

function deleteYes() {
    window.location.href = deleteUrl;
}

function deleteNo() {
    document.getElementById("deleteBox").style.display = "none";
}

var searchInput = document.getElementById("search");
if (searchInput) {
    searchInput.addEventListener("keyup", function () {
        var filter = this.value.toLowerCase();
        var rows = document.querySelectorAll("#parkingTable tbody tr");

        rows.forEach(function (row) {
            var text = row.textContent.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });
}