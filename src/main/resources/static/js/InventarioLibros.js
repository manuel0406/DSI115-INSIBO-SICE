function showEditModal(idInventarioLibros) {
    fetch('/Biblioteca/InventarioLibros/edit/' + idInventarioLibros)
        .then(response => response.json())
        .then(data => {
            document.getElementById('idInventarioLibrosEdit').value = data.idInventarioLibros;
            document.getElementById('tituloLibroEdit').value = data.tituloLibro;
            document.getElementById('autorLibroEdit').value = data.autorLibro;
            document.getElementById('tipoLibroEdit').value = data.tipoLibro;
            document.getElementById('precioUnitarioEdit').value = data.precioUnitario;
            document.getElementById('existenciaLibroEdit').value = data.existenciaLibro;
            document.getElementById('fechaIngresoEdit').value = data.fechaIngreso;
            document.getElementById('cantidadMalEstadoEdit').value = data.cantidadMalEstado;

            $('#editarLibroModal').modal('show');
        })
        .catch(error => {
            console.error('Error al obtener los datos del libro:', error);
        });
}

function showDeleteModal(itemId) {
    // Establece el ID del artículo en el formulario
    document.getElementById('itemId').value = itemId;
    
    // Configura la acción del formulario de eliminación
    const deleteForm = document.getElementById('deleteForm');
    deleteForm.action = `/Biblioteca/InventarioLibros/delete/${itemId}`;
    
    // Muestra el modal
    var deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    deleteModal.show();
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formInventarioLibro");
    const cantidadField = document.getElementById("existenciaLibro");
    const cantidadMalEstadoField = document.getElementById("cantidadMalEstado");
    const precioField = document.getElementById("precioLibro");
    const fechaIngresoField = document.getElementById("fechaIngreso");

    // Obtener la fecha actual del sistema
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0'); // Los meses en JavaScript van de 0 a 11
    const year = today.getFullYear();

    // Formatear la fecha como yyyy-mm-dd
    const todayFormatted = `${year}-${month}-${day}`;

    // Establecer la fecha en el campo de fechaIngreso
    fechaIngresoField.value = todayFormatted;

    form.addEventListener("submit", function (event) {
        let isValid = true;

        // Validar que el precio sea mayor a 0
        const precio = parseFloat(precioField.value);
        if (isNaN(precio) || precio <= 0) {
            precioField.setCustomValidity("El precio debe ser mayor a 0 y puede tener decimales.");
            isValid = false;
        } else {
            precioField.setCustomValidity(""); // Limpiar el mensaje de error
        }

        // Validar que la cantidad sea mayor a 0
        const cantidad = parseInt(cantidadField.value, 10);
        if (isNaN(cantidad) || cantidad < 1) {
            cantidadField.setCustomValidity("La cantidad debe ser mayor a cero.");
            isValid = false;
        } else {
            cantidadField.setCustomValidity(""); // Limpiar el mensaje de error
        }

        // Validar que la cantidad en mal estado no sea mayor que la cantidad total
        const cantidadMalEstado = parseInt(cantidadMalEstadoField.value, 10);
        if (isNaN(cantidadMalEstado) || cantidadMalEstado > cantidad) {
            cantidadMalEstadoField.setCustomValidity("La cantidad en mal estado no puede ser mayor que la cantidad total.");
            isValid = false;
        } else {
            cantidadMalEstadoField.setCustomValidity(""); // Limpiar el mensaje de error
        }

        // Validar que la fecha de ingreso no sea futura
        const fechaIngreso = new Date(fechaIngresoField.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Quitar horas, minutos, segundos
        if (isNaN(fechaIngreso.getTime()) || fechaIngreso > today) {
            fechaIngresoField.setCustomValidity("La fecha de ingreso no puede ser futura.");
            isValid = false;
        } else {
            fechaIngresoField.setCustomValidity(""); // Limpiar el mensaje de error
        }

        // Si alguna validación no es válida, evitar el envío del formulario
        if (!form.checkValidity() || !isValid) {
            event.preventDefault();
            event.stopPropagation();
        }

        form.classList.add("was-validated");
    });
});

document.addEventListener("DOMContentLoaded", function () {
    // Establecer la fecha del sistema en el campo de fecha de ingreso del formulario de edición
    const fechaIngresoFieldEdit = document.getElementById("fechaIngresoEdit");

    if (fechaIngresoFieldEdit) {
        const today = new Date();
        const day = String(today.getDate()).padStart(2, '0');
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const year = today.getFullYear();
        const todayFormatted = `${year}-${month}-${day}`;
        fechaIngresoFieldEdit.value = todayFormatted;
    }

    // Función para validar el formulario
    function validateForm(form) {
        const precio = form.querySelector('#precioUnitarioEdit');
        const existencia = form.querySelector('#existenciaLibroEdit');
        const cantidadMalEstado = form.querySelector('#cantidadMalEstadoEdit');

        // Limpiar validaciones previas
        form.querySelectorAll('.form-control').forEach(input => {
            input.classList.remove('is-invalid');
        });

        let isValid = true;

        if (parseFloat(precio.value) <= 0 || isNaN(parseFloat(precio.value))) {
            precio.classList.add('is-invalid');
            isValid = false;
        }

        if (parseInt(existencia.value) <= 0 || isNaN(parseInt(existencia.value))) {
            existencia.classList.add('is-invalid');
            isValid = false;
        }

        if (parseInt(cantidadMalEstado.value) > parseInt(existencia.value) || isNaN(parseInt(cantidadMalEstado.value))) {
            cantidadMalEstado.classList.add('is-invalid');
            isValid = false;
        }

        return isValid;
    }

    const formEdit = document.getElementById('formEditarLibro');
    if (formEdit) {
        formEdit.addEventListener('submit', function (event) {
            if (!validateForm(formEdit)) {
                event.preventDefault();
            }
        });
    }
});