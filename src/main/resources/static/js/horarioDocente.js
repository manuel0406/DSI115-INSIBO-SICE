document.addEventListener('DOMContentLoaded', function () {
    var horasDeClase = window.horasDeClase;

    function actualizarContenido(idHorarioBase) {
        var contenedor = document.querySelector(`td[data-idHorarioBase="${idHorarioBase}"]`);
        if (!contenedor) return;

        // Filtra los datos según idHorarioBase
        var datosFiltrados = horasDeClase.filter(horario => horario.idHorarioBase === String(idHorarioBase));

        // Crea el contenido HTML con la información filtrada
        var contenido = datosFiltrados.map(horario =>
            `<span>${horario.codMateria}<br>
                ${horario.grado + "º "} ${horario.codigo + "-"+horario.seccion}</span>`
        ).join('<br>');

        contenedor.innerHTML = contenido;
    }

    // Actualiza el contenido de todos los elementos td con data-idHorarioBase
    for (var i = 1; i <= 72; i++) {
        actualizarContenido(i);
    }
});