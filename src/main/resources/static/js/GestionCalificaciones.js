var tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-t="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
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
    .getElementById("cancelarSancion")
    .addEventListener("click", function () {
        var form = document.getElementById("formNota");
        form.reset();
        form.classList.remove("was-validated"); // Eliminar la clase de validación
    });

$(document).ready(function () {
    $('.editar-btn').on('click', function () {
        var idNota = $(this).data('id');
        var idAlumno = $(this).data('al');
        var idActividad = $(this).data('ac');
        var notaObtenida = $(this).data('not');
       


        $('#idNota').val(idNota);
        $('#idAlumno').val(idAlumno);
        $('#idActividad').val(idActividad);
        $('#notaObtenida').val(notaObtenida);

    });
});