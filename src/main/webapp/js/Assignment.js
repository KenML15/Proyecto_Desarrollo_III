JavaScript

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const errorType = urlParams.get('error');
    const msg = urlParams.get('msg');
    const plate = urlParams.get('plate');
    const slot = urlParams.get('slot');

    // Mensajes de Alerta
    if (errorType === 'disability_required') {
        Swal.fire({
            icon: 'error',
            title: 'Espacio Reservado',
            html: `El espacio <b>${slot}</b> es para personas con discapacidad.<br>El vehículo <b>${plate}</b> no cumple el requisito.`,
            confirmButtonColor: '#d33'
        });
    } else if (msg === 'success') {
        Swal.fire({
            icon: 'success',
            title: 'Salida Registrada',
            text: 'El espacio se ha liberado correctamente.',
            timer: 2000,
            showConfirmButton: false
        });
    }
};

// ESTA ES LA FUNCIÓN QUE IMPORTA - UNA SOLA VEZ
function confirmarSalida(url, placa) {
    console.log("Intentando liberar placa: " + placa); // Para depuración
    
    Swal.fire({
        title: '¿Registrar salida?',
        text: `Se liberará el espacio del vehículo: ${placa}`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, liberar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = url; 
        }
    });
}

// Búsqueda en tiempo real
const inputBusqueda = document.getElementById("search");
if (inputBusqueda) {
    inputBusqueda.addEventListener("keyup", function () {
        const filter = this.value.toLowerCase();
        const rows = document.querySelectorAll("#assignmentTable tbody tr");
        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });
}

function cancelar() {
    window.location.href = document.body.dataset.menu || "menu_clerk.jsp";
}

document.addEventListener('DOMContentLoaded', () => {
    const parkingForm = document.getElementById('parkingForm');
    const confirmBox = document.getElementById('confirmBox');
    const btnCancelar = document.getElementById('btnCancelar');
    const btnConfirmYes = document.getElementById('confirmYes');
    const btnConfirmNo = document.getElementById('confirmNo');

    // 1. Manejo del Formulario vía AJAX (FETCH)
    if (parkingForm) {
        parkingForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = new FormData(this);
            const params = new URLSearchParams(formData);

            fetch('assignments', {
                method: 'POST',
                body: params,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    mostrarTicket(data);
                } else {
                    manejarErrores(data.error);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire('Error', 'Hubo un fallo en la comunicación con el servidor.', 'error');
            });
        });
    }

    // 2. Función para mostrar el Ticket Estilizado
    function mostrarTicket(data) {
        Swal.fire({
            title: '<strong>¡INGRESO EXITOSO!</strong>',
            icon: 'success',
            html: `
                <div style="text-align: center; border: 2px dashed #333; padding: 20px; background: #fff; color: #000; font-family: 'Courier New', monospace; margin-top: 10px;">
                    <h2 style="margin: 0; letter-spacing: 2px;">PARQUEO P</h2>
                    <p>*************************</p>
                    <p style="font-size: 1.1em;">PLACA: <strong>${data.plate}</strong></p>
                    <div style="border: 1px solid #000; margin: 15px 0; padding: 10px;">
                        <span style="display:block; font-size: 0.8em;">ESPACIO ASIGNADO</span>
                        <span style="font-size: 2.5em; font-weight: bold;">#${data.slot}</span>
                    </div>
                    <p>*************************</p>
                    <p style="font-size: 0.8em;">${new Date().toLocaleString()}</p>
                    <p style="font-size: 0.7em;">Conserve este comprobante</p>
                </div>
            `,
            showCloseButton: true,
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#4a5568'
        }).then(() => {
            parkingForm.reset();
        });
    }

    // 3. Manejo de Errores del Servidor
    function manejarErrores(errorType) {
        let msg = 'No se pudo procesar la solicitud.';
        if (errorType === 'already_parked') msg = 'Este vehículo ya tiene un ingreso activo.';
        if (errorType === 'no_slots_available') msg = 'No hay espacios disponibles (Lleno o restricción de Ley 7600).';
        if (errorType === 'db') msg = 'Error crítico en la base de datos.';

        Swal.fire({
            icon: 'error',
            title: 'Atención',
            text: msg,
            confirmButtonColor: '#d33'
        });
    }

    // 4. Lógica de Navegación y Confirmación
    if (btnCancelar) {
        btnCancelar.addEventListener('click', () => {
            confirmBox.style.display = 'flex';
        });
    }

    if (btnConfirmNo) {
        btnConfirmNo.addEventListener('click', () => {
            confirmBox.style.display = 'none';
        });
    }

    if (btnConfirmYes) {
        btnConfirmYes.addEventListener('click', () => {
            const menuPrincipal = document.body.getAttribute('data-menu');
            window.location.href = menuPrincipal;
        });
    }
});