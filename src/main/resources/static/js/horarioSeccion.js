document.addEventListener('DOMContentLoaded', function () {
    var horasDeClase = window.horasDeClase;

    function actualizarContenido(idHorarioBase) {
        var contenedor = document.querySelector(`td[data-idHorarioBase="${idHorarioBase}"]`);
        if (!contenedor) return;

        // Filtrar los datos según idHorarioBase
        var datosFiltrados = horasDeClase.filter(horario => horario.idHorarioBase === String(idHorarioBase));

        // Crear el contenido HTML con la información filtrada
        var contenido = datosFiltrados.map(horario =>
            `<span>${horario.codMateria}<br>
                ${horario.nombreDocente} ${horario.apellidoDocente}</span>`
        ).join('<br>');
        contenedor.innerHTML = contenido;
    }

    // Actualizar el contenido de todos los elementos td con data-idHorarioBase
    for (var i = 1; i <= 72; i++) {
        actualizarContenido(i);
    }
});