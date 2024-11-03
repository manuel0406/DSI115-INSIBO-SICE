function showEditModal(idArticulo) {
    fetch('/Biblioteca/Papeleria/InventarioPapeleria/edit/' + idArticulo)
        .then(response => response.json())
        .then(data => {
            document.getElementById('idArticuloEdit').value = data.idArticulo;
            document.getElementById('nombreArticuloEdit').value = data.nombreArticulo;
            document.getElementById('descripcionArticuloEdit').value = data.descripcionArticulo;
            document.getElementById('existenciaArticuloEdit').value = data.existenciaArticulo;

            $('#editarArticuloModal').modal('show');
        })
        .catch(error => {
            console.error('Error al obtener los datos del artículo:', error);
        });
}

function showDeleteModal(itemId) {
    // Establece el ID del artículo en el formulario
    document.getElementById('itemId').value = itemId;
    
    // Configura la acción del formulario de eliminación
    const deleteForm = document.getElementById('deleteForm');
    deleteForm.action = `/Biblioteca/Papeleria/InventarioPapeleria/delete/${itemId}`;
    
    // Muestra el modal
    var deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    deleteModal.show();
}

document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("formNuevoProducto");

    form.addEventListener("submit", function (event) {
        var isValid = true;

        // Validar nombre de producto
        var nombre = form.querySelector("#nombreArticulo");
        if (!nombre.value.trim()) {
            nombre.classList.add("is-invalid");
            isValid = false;
        } else {
            nombre.classList.remove("is-invalid");
        }

        // Validar detalle del producto
        var detalle = form.querySelector("#descripcionArticulo");
        if (!detalle.value.trim()) {
            detalle.classList.add("is-invalid");
            isValid = false;
        } else {
            detalle.classList.remove("is-invalid");
        }

        // Validar cantidad
        var cantidad = form.querySelector("#existenciaArticulo");
        if (cantidad.value <= 0) {
            cantidad.classList.add("is-invalid");
            isValid = false;
        } else {
            cantidad.classList.remove("is-invalid");
        }

        if (!isValid) {
            event.preventDefault();
            event.stopPropagation();
            form.classList.add("was-validated");
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("formEditarArticulo");

    form.addEventListener("submit", function (event) {
        var isValid = true;

        // Validar nombre del artículo
        var nombre = form.querySelector("#nombreArticuloEdit");
        if (!nombre.value.trim()) {
            nombre.classList.add("is-invalid");
            isValid = false;
        } else {
            nombre.classList.remove("is-invalid");
        }

        // Validar descripción del artículo
        var descripcion = form.querySelector("#descripcionArticuloEdit");
        if (!descripcion.value.trim()) {
            descripcion.classList.add("is-invalid");
            isValid = false;
        } else {
            descripcion.classList.remove("is-invalid");
        }

        // Validar existencia del artículo
        var existencia = form.querySelector("#existenciaArticuloEdit");
        if (existencia.value <= 0) {
            existencia.classList.add("is-invalid");
            isValid = false;
        } else {
            existencia.classList.remove("is-invalid");
        }

        if (!isValid) {
            event.preventDefault();
            event.stopPropagation();
            form.classList.add("was-validated");
        }
    });
});

