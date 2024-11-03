document.addEventListener('DOMContentLoaded', (event) => {
    // Toma la informacion relacionada al checkbox marcado y la pasa al form
    const checkboxes = document.querySelectorAll('.checkbox-clase');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            if (this.checked) {
                const hora = this.getAttribute('data-hora');
                const dia = this.getAttribute('data-dia');
                const horaBase = this.getAttribute('data-horaBase')
                const value = this.value;

                document.getElementById('horaSeleccionada').value = hora;
                document.getElementById('dia').value = dia;
                document.getElementById('horaBase').value = horaBase;
                document.getElementById('horaSeleccionada').dataset.value = value;
            }
        });
    });

    // Añade los parámetros necesarios al enviar el form, estos datos son necesario en el controller 'guardarHora'
    const form = document.getElementById('formNuevaHora');
    form.addEventListener('submit', function (event) {

        // Obtiene el valor del atributo 'data-value' del elemento con ID 'horaSeleccionada' y lo asigna al campo de entrada 'horaInput'
        const horaInput = document.getElementById('horaSeleccionada');
        horaInput.value = horaInput.dataset.value;

        // Crea un campo oculto para enviar 'carrera' 'grado' y 'seccion'
        const carreraInput = document.createElement('input');
        carreraInput.type = 'hidden';
        carreraInput.name = 'carrera';
        carreraInput.value = document.getElementById('carrera').value;
 
        const gradoInput = document.createElement('input');
        gradoInput.type = 'hidden';
        gradoInput.name = 'grado';
        gradoInput.value = document.getElementById('grado').value;

        const seccionInputHidden = document.createElement('input');
        seccionInputHidden.type = 'hidden';
        seccionInputHidden.name = 'seccion';
        seccionInputHidden.value = document.getElementById('seccion').value;

        form.appendChild(carreraInput);
        form.appendChild(gradoInput);
        form.appendChild(seccionInputHidden);

        // Obtiene el 'select' de ID 'asignacionSeleccionada' y el valor 'data-docente' de la opción seleccionada
        const asignacionSelect = document.getElementById('asignacionSeleccionada');
        const selectedOption = asignacionSelect.options[asignacionSelect.selectedIndex];
        const duiDocente = selectedOption.getAttribute('data-docente');

        // Crea un campo oculto para enviar 'duiDocente'
        const duiDocenteInput = document.createElement('input');
        duiDocenteInput.type = 'hidden';
        duiDocenteInput.name = 'duiDocente';
        duiDocenteInput.value = duiDocente;

        form.appendChild(duiDocenteInput);
    });


    // Desmarcar checkboxes al hacer clic en Cancelar
    const cancelarBtn = document.getElementById('cancelarNuevaHora');
    cancelarBtn.addEventListener('click', function () {
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
    });

    // Desmarcar checkboxes cuando el modal se cierra
    const modal = document.getElementById('nuevaHoradeClase');
    modal.addEventListener('hidden.bs.modal', function () {
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
    });

    /* ----------------------------------------------------------------------------------------- */
    /*      Lo siguiente muestra 'disponible' o la asignacion relacionada al checkbox            */
    /* ----------------------------------------------------------------------------------------- */

    // Convierte la lista de horarios de clase JSON
    var horasDeClase = JSON.parse(horasDeClaseJson);

    // Itera sobre cada checkbox encontrado
    checkboxes.forEach(function (checkbox) {
        // Obtiene el valor del checkbox que corresponde a 'idHorarioBase'
        var idHorarioBase = checkbox.value;

        // Busca una asignación en la lista de horarios de clase que coincida con el 'idHorarioBase' del checkbox
        var asignacion = horasDeClase.find(function (item) {
            return item.idHorarioBase == idHorarioBase;
        });

        // Obtiene el label asociado al checkbox
        var label = checkbox.nextElementSibling;
        if (label) {
            if (asignacion) {
                // Si se encuentra una asignación, deshabilita el checkbox
                checkbox.disabled = true;
                checkbox.hidden = true;

                // Crea un contenedor para organizar el texto original del label y el nuevo texto
                var container = document.createElement("div");
                container.style.display = "inline";

                // Clona el texto original del label y le aplica tachado para indicar que no está disponible
                var originalText = document.createElement("span");
                originalText.innerHTML = label.innerHTML;
                originalText.style.textDecoration = "line-through";
                originalText.style.color = "#6c757d"; // Color gris

                // Limpia el contenido del label original y agrega el texto tachado
                label.innerHTML = "";
                container.appendChild(originalText);

                // Crea un nuevo span que contiene la información del docente y materia asignada
                var infoDocenteMateria = document.createElement("span");
                infoDocenteMateria.innerHTML = `&nbsp;<em> (<em><strong>${asignacion.nombreDocente}</strong> - <em>${asignacion.codMateria})</em>`;
                infoDocenteMateria.style.textDecoration = "none";
                infoDocenteMateria.style.color = "#495057";
                infoDocenteMateria.classList.add("ml-2");

                container.appendChild(infoDocenteMateria);
                label.appendChild(container);
            } else {
                // Si no se encuentra una asignación, indica que está disponible
                var emptySpace = document.createElement("span");
                emptySpace.innerHTML = `&nbsp;<em> (Disponible)<em>`;
                emptySpace.style.textDecoration = "none";
                emptySpace.style.color = "#878686";
                emptySpace.classList.add("text-start", "mb-2");

                label.appendChild(emptySpace);
            }
        }
    });
});
