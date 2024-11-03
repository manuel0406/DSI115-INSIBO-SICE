var tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-t="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});
document.addEventListener("DOMContentLoaded", function () {
    var agregarButton = document.querySelectorAll(".Agregar-btn");
    var confirmAgregarModal = new bootstrap.Modal(
        document.getElementById("agregarModal")
    );
    var confirmAgregarButton = document.getElementById("agregarModalButton");

    agregarButton.forEach(function (button) {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            confirmAgregarModal.show();
        });
    });

    
    confirmAgregarButton.addEventListener("click", function () {
        var form = document.getElementById("formAnio");
        var yearField = document.getElementById("anio");
        var yearValue = parseInt(yearField.value, 10);

        // Limpia los mensajes de error anteriores
        yearField.setCustomValidity("");

        // Verifica si el año es mayor a 2020
        if (yearField.value === "") {
            yearField.setCustomValidity("Este campo no puede quedar vacío.");
        } else if (yearValue < 2020) {
            yearField.setCustomValidity("El año debe ser mayor a 2020.");
        }

        // Valida el formulario
        if (!form.checkValidity()) {
            form.classList.add("was-validated");
        } else {
            form.submit();
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    var consultarButton = document.querySelectorAll(".consultar-btn");
    var confirmConsultarModal = new bootstrap.Modal(document.getElementById("consultarModal"));
    var confirmConsultarModalButton = document.getElementById("consultarModalButton");

    consultarButton.forEach(function (button) {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            confirmConsultarModal.show();
        });
    });
});

(() => {
    "use strict";

    // Selecciona todos los formularios que tienen la clase "needs-validation"
    const forms = document.querySelectorAll(".needs-validation");
    Array.from(forms).forEach((form) => {
        // Añade un listener para el evento "submit" en cada formulario
        form.addEventListener(
            "submit",
            (event) => {
                // Verifica si el formulario es válido
                if (!form.checkValidity()) {
                    // Si no es válido, previene el envío del formulario
                    event.preventDefault();
                    event.stopPropagation();

                    // Encuentra el primer elemento inválido en el formulario
                    const firstInvalidElement = form.querySelector(":invalid");
                    if (firstInvalidElement) {
                        // Desplaza la vista al primer elemento inválido
                        firstInvalidElement.scrollIntoView({
                            behavior: "smooth",
                            block: "center",
                        });
                        // Opcionalmente, enfoca el primer elemento inválido
                        firstInvalidElement.focus();
                    }
                }

                // Añade la clase "was-validated" al formulario
                form.classList.add("was-validated");
            },
            false
        );
    });
})();

document
    .getElementById("cancelarAnio")
    .addEventListener("click", function () {
        var form = document.getElementById("formAnio");
        form.reset();
        form.classList.remove("was-validated"); // Eliminar la clase de validación
    });

$(document).ready(function () {
    $('.editar-btn').on('click', function () {
        var idAnio = $(this).data('id');
        var estado = $(this).data('est');
        var anio = $(this).data('an');
        var matricula = $(this).data('ma');
        var cerrado = $(this).data('ce')

        $('#editIdAnio').val(idAnio);
        $('#editEstado').prop('checked', estado);
        $('#editAnio').val(anio);
        $('#editMatricula').prop('checked', matricula);
        $('#editcerrado').prop('checked', cerrado);

    });
});

document.addEventListener("DOMContentLoaded", function () {
    var deleteButtons = document.querySelectorAll('.cerrar-btn');
    var confirmDeleteModal = new bootstrap.Modal(document.getElementById('confirmCerrarModal'));
    var confirmDeleteButton = document.getElementById('confirmCerrarButton');
    var currentHref = '';

    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            currentHref = button.getAttribute('href');
            confirmDeleteModal.show();
        });
    });

    confirmDeleteButton.addEventListener('click', function () {
        window.location.href = currentHref;
    });
});
