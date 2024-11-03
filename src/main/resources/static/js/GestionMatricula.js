// Configuración de tooltips de Bootstrap
var tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-t="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});

// Restablecer el formulario y remover la validación cuando se cierra el modal
document
    .getElementById("cancelarMatricula")
    .addEventListener("click", function () {
        var form = document.getElementById("formMatricula");
        form.reset();
        form.classList.remove("was-validated"); // Eliminar la clase de validación
    });

$(document).ready(function () {
    // Evento al abrir el modal de matrícula
    $('.matricular-btn').on('click', function () {
        var idAlumno = $(this).data('id');
        var nombreAlumno = $(this).data('non');
        var apellidoAlumno = $(this).data('ape');
        var nie = $(this).data('nie');
        var carrera = $(this).data('ca');
        var seccion = $(this).data('sec');
        var grado = parseInt($(this).data('gra')); // Asegúrate de que el grado sea un número
        var codigo = $(this).data('cod');
        var estado = $(this).data('est'); // true si aprobado, false si no aprobado

        // Rellenar los campos del modal
        $('#idAlumno').val(idAlumno);
        $('#nombreAlumno').val(nombreAlumno);
        $('#apellidoAlumno').val(apellidoAlumno);
        $('#nieM').val(nie);
        $('#carrera').val(carrera);
        $('#seccion').val(seccion);
        $('#codigo').val(codigo);
        $('#estado').val(estado);

        // Control del campo grado
        var gradoSelect = $('#grado');
        gradoSelect.empty(); // Limpia las opciones anteriores

        if (!estado) {
            // Si el alumno no aprobó, agregar solo el grado actual al select
            gradoSelect.append(new Option(grado, grado, true, true));
            $('#alumnoMensaje').text(`El alumno ${nombreAlumno} ${apellidoAlumno} no aprobó el año y deberá repetir el mismo grado.`);
            $('#alumnoMensaje').removeClass('text-success').addClass('text-danger'); // Mensaje de reprobado
        } else {
            // Si el alumno aprobó, sumar 1 al grado actual
            var nuevoGrado = grado + 1;
            gradoSelect.append(new Option(nuevoGrado, nuevoGrado, true, true)); // Agregar el nuevo grado
            $('#alumnoMensaje').text(`El alumno ${nombreAlumno} ${apellidoAlumno} puede matricularse en el siguiente año.`);
            $('#alumnoMensaje').removeClass('text-danger').addClass('text-success'); // Mensaje de aprobado
        }
    });

    // Restablecer el formulario cuando el modal se cierra
    $('#MatricularModal').on('hidden.bs.modal', function () {
        $('#formMatricula')[0].reset();
    });
});

// Lógica para actualizar el campo de secciones basado en carrera y grado seleccionados
document.addEventListener('DOMContentLoaded', function () {
    const carreraSelect = document.getElementById('carrera');
    const gradoSelect = document.getElementById('grado');
    const seccionSelect = document.getElementById('seccion');
    const matricula = document.getElementById('matricula');

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
                .catch(error => console.error('Error al actualizar secciones: ', error));
        } else {
            seccionSelect.innerHTML = '<option value="" selected>Seleccione</option>';
        }
    }

    carreraSelect.addEventListener('change', updateSecciones);
    gradoSelect.addEventListener('change', updateSecciones);
});
