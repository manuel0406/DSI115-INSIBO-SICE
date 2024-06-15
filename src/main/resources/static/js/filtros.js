document.addEventListener('DOMContentLoaded', function() {
    let selectedCarrera = null;
    let selectedGrado = null;
    let selectedSeccion = null;

    function filtrarPorCarrera(carrera) {
        selectedCarrera = carrera;
        cargarAlumnos();
    }

    function filtrarPorGrado(grado) {
        selectedGrado = grado;
        cargarAlumnos();
    }

    function filtrarPorSeccion(seccion) {
        selectedSeccion = seccion;
        cargarAlumnos();
    }

    function cargarAlumnos() {
        if (!selectedCarrera || !selectedGrado || !selectedSeccion) {
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/ExpedienteAlumno/filtrarAlumnos?carrera=' + (selectedCarrera || '') + '&grado=' + (selectedGrado || '') + '&seccion=' + (selectedSeccion || ''));
        xhr.onload = function () {
            if (xhr.status === 200) {
                document.getElementById('alumnosContainer').innerHTML = xhr.responseText;
            } else {
                console.log('Error al cargar los alumnos');
            }
        };
        xhr.send();
    }
});
