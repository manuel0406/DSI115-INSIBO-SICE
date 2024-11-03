var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});

document.addEventListener("DOMContentLoaded", function() {
    var deleteButtons = document.querySelectorAll('.delete-btn');
    var confirmDeleteModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
    var confirmDeleteButton = document.getElementById('confirmDeleteButton');
    var currentHref = '';

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            currentHref = button.getAttribute('href');
            confirmDeleteModal.show();
        });
    });

    confirmDeleteButton.addEventListener('click', function() {
        window.location.href = currentHref;
    });
});

(() => {
    'use strict'

    // Selecciona todos los formularios que tienen la clase "needs-validation"
    const forms = document.querySelectorAll('.needs-validation')
    Array.from(forms).forEach(form => {
        // Añade un listener para el evento "submit" en cada formulario
        form.addEventListener('submit', event => {
            // Verifica si el formulario es válido
            if (!form.checkValidity()) {
                // Si no es válido, previene el envío del formulario
                event.preventDefault()
                event.stopPropagation()

                // Encuentra el primer elemento inválido en el formulario
                const firstInvalidElement = form.querySelector(':invalid')
                if (firstInvalidElement) {
                    // Desplaza la vista al primer elemento inválido
                    firstInvalidElement.scrollIntoView({ behavior: 'smooth', block: 'center' })
                    // Opcionalmente, enfoca el primer elemento inválido
                    firstInvalidElement.focus()
                }
            }

            // Añade la clase "was-validated" al formulario
            form.classList.add('was-validated')
        }, false)
    })
})()