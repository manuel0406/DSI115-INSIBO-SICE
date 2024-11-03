// Selecciona todos los elementos con la clase "restricted-input"
const restrictedInputs = document.querySelectorAll('.restricted-input');
restrictedInputs.forEach(function (input) {
    // Añade un listener para el evento "input" en cada elemento
    input.addEventListener('input', function (event) {
        // Obtiene el valor actual del input
        let value = this.value;
        // Reemplaza todos los caracteres que no sean dígitos o el signo '-' con una cadena vacía
        value = value.replace(/[^\d-]/g, '');
        // Asigna el nuevo valor filtrado al input
        this.value = value;
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

// Añade un listener para el evento "submit" en el formulario con id "formulario"
document.getElementById('formulario').addEventListener('submit', function (event) {
    var showError = false;
    var errorMessage = '';

    // Valida el campo NIE
    var nieInput = document.getElementById('nie');
    var nieValue = nieInput.value;
    if (nieValue.length < 7) {
        errorMessage = 'El NIE debe tener 7 caracteres.';
        showError = true;
    }

    // Valida el campo DUI del encargado
    var duiInput = document.getElementById('duiEncargado');
    var duiValue = duiInput.value;
    if (duiValue.length < 10) {
        errorMessage = 'El DUI del responsable debe tener 10 caracteres.';
        showError = true;
    }

    // Valida el campo Teléfono del alumno
    var telefonoInput = document.getElementById('telefono');
    var telefonoValue = telefonoInput.value;
    if (telefonoValue.length < 9) {
        errorMessage = 'El Teléfono del alumno debe tener 9 caracteres.';
        showError = true;
    }

    // Valida el campo Teléfono del encargado
    var telefonoEncargadoInput = document.getElementById('telefonoEncargado');
    var telefonoEncargadoValue = telefonoEncargadoInput.value;
    if (telefonoEncargadoValue.length < 9) {
        errorMessage = 'El Teléfono del Encargado debe tener 9 caracteres.';
        showError = true;
    }

    // Si hay algún error, muestra el mensaje de error y previene el envío del formulario
    if (showError) {
        showErrorModal(errorMessage);
        event.preventDefault();
    }
});

// Función para mostrar el modal de error con el mensaje correspondiente
function showErrorModal(message) {
    document.getElementById('errorMessage').innerText = message;
    var errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
    errorModal.show();
}

document.addEventListener('DOMContentLoaded', function () {
    const carreraSelect = document.getElementById('carrera');
    const gradoSelect = document.getElementById('grado');
    const seccionSelect = document.getElementById('seccion');
    const matricula = document.getElementById('matricula')
  console.log(matricula.value)
    function updateSecciones() {
        const carrera = carreraSelect.value;
        const grado = gradoSelect.value;

        if (carrera && grado) {
            fetch(`/ExpedienteAlumno/secciones?carrera=${encodeURIComponent(carrera)}&grado=${encodeURIComponent(grado)}&matricula=${encodeURIComponent(matricula.value)}`)
                .then(response => response.json())
                .then(data => {
                    seccionSelect.innerHTML = '<option value="" selected>Seleccione</option>';
                    data.forEach(seccion => {
                        const option = document.createElement('option');
                        option.value = seccion;
                        option.text = seccion;
                        seccionSelect.add(option);
                    });
                })
                .catch(error => console.error('Error:', error));
        } else {
            seccionSelect.innerHTML = '<option value="" selected>Seleccione</option>';
        }
    }

    carreraSelect.addEventListener('change', updateSecciones);
    gradoSelect.addEventListener('change', updateSecciones);
});
