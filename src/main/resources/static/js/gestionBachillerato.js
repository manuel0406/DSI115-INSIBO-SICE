var tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
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
    var currentHref = "";

    agregarButton.forEach(function (button) {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            //currentHref = button.getAttribute('href');
            confirmAgregarModal.show();
        });
    });
    confirmAgregarButton.addEventListener("click", function () {
        var form = document.getElementById("formBachillerato");
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
            //currentHref = button.getAttribute('href');
            confirmConsultarModal.show();
        });
    });

    confirmAgregarButton.addEventListener("click", function () {
        var form = document.getElementById("formBachillerato");
        if (!form.checkValidity()) {
            form.classList.add("was-validated");
        } else {
            form.submit();
        }
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
    .getElementById("cancelarSancion")
    .addEventListener("click", function () {
        var form = document.getElementById("formBachillerato");
        form.reset();
        form.classList.remove("was-validated"); // Eliminar la clase de validación
    });

    $(document).ready(function() {
        $('.editar-btn').on('click', function() {
            var idOrientador = $(this).data('id');
            var docente = $(this).data('doc');
            var bachillerato = $(this).data('bac');           
    
            $('#idOrientador').val(idOrientador);
            $('#EditDocente').val(docente);
            $('#EditBachillerato').val(bachillerato);
           
        });
    });


    document.addEventListener('DOMContentLoaded', function () {
        // Obtener todos los botones de consulta
        const consultarButtons = document.querySelectorAll('.btn-success.editar-btn');
    
        consultarButtons.forEach(button => {
            button.addEventListener('click', function () {
                // Obtener los datos del atributo data
                const tipo = this.getAttribute('data-tip');
                const descripcion = this.getAttribute('data-des');
                const accionCorrectiva = this.getAttribute('data-aco');
                const fecha = this.getAttribute('data-fec');
    
                // Asignar los datos al modal
                document.getElementById('consultaTipo').textContent = tipo;
                document.getElementById('consultaDescripcion').textContent = descripcion;
                document.getElementById('consultaAccionCorrectiva').textContent = accionCorrectiva;
                document.getElementById('consultaFechaCreacion').textContent = fecha;
            });
        });
    });
    
    