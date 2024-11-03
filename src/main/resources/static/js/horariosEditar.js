document.addEventListener("DOMContentLoaded", function () {
    // Función para agregar un campo oculto al formulario
    function agregarCampoOculto(form, nombre, valor) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = nombre;
        input.value = valor;
        form.appendChild(input);
    }

    // Añade los parámetros necesarios al enviar el form 'guardarHora'
    const formNuevaHora = document.getElementById('formNuevaHora');
    formNuevaHora.addEventListener('submit', function () {
        const carrera = document.getElementById('carrera').value;
        const grado = document.getElementById('grado').value;
        const seccion = document.getElementById('seccion').value;
        const asignacionSelect = document.getElementById('asignacionSeleccionada');
        const selectedOption = asignacionSelect.options[asignacionSelect.selectedIndex];
        const duiDocente = selectedOption.getAttribute('data-docente');

        // Agrega los campos ocultos al formulario
        agregarCampoOculto(formNuevaHora, 'carrera', carrera);
        agregarCampoOculto(formNuevaHora, 'grado', grado);
        agregarCampoOculto(formNuevaHora, 'seccion', seccion);
        agregarCampoOculto(formNuevaHora, 'duiDocente', duiDocente);
    });

    // Maneja el filtro de días para la tabla
    const filtroDia = document.getElementById('filtroDia');
    const filas = document.querySelectorAll('tbody tr');

    filtroDia.addEventListener('change', function () {
        const diaSeleccionado = this.value;

        filas.forEach(fila => {
            const columnaDia = fila.querySelector('td:nth-child(3)');
            const dia = columnaDia.textContent.trim();

            fila.style.display = (diaSeleccionado === "Todos" || dia === diaSeleccionado) ? "" : "none";
        });

        // Actualiza la selección de checkboxes en función del filtro activo
        actualizarSelectAllCheckbox();
        toggleDeleteButton();
    });

    // Toma la información relacionada al registro seleccionado de la tabla y la pasa al form
    const editButtons = document.querySelectorAll(".editar-btn");

    editButtons.forEach(button => {
        button.addEventListener("click", function () {
            const campos = {
                "idAsignacionHorario": this.getAttribute("data-id"),
                "horaSeleccionada": this.getAttribute("data-horario"),
                "intervaloHora": this.getAttribute("data-intervalo"),
                "asignacionSeleccionada": this.getAttribute("data-docente"),
                "horaBase": this.getAttribute("data-hora"),
                "dia": this.getAttribute("data-dia")
            };

            // Asigna los valores a los campos del formulario
            for (const [campoId, valor] of Object.entries(campos)) {
                document.getElementById(campoId).value = valor;
            }
        });
    });

    // Manejo de la selección múltiple y eliminación
    const selectAllCheckbox = document.getElementById('selectAll');
    const itemCheckboxes = document.querySelectorAll('.select-item');
    const deleteButton = document.getElementById('deleteSelected');
    const formEliminarHoras = document.getElementById('eliminarHorasForm');
    const confirmSelectedDeleteButton = document.getElementById('confirmSelectedDeleteButton');

    // Función para alternar el botón de eliminar
    function toggleDeleteButton() {
        const anyChecked = Array.from(itemCheckboxes).some(checkbox => checkbox.checked);
        deleteButton.disabled = !anyChecked;
    }

    // Función para actualizar la selección de "Seleccionar todo" en función del filtro
    function actualizarSelectAllCheckbox() {
        const visibleCheckboxes = Array.from(itemCheckboxes).filter(checkbox => checkbox.closest('tr').style.display !== 'none');
        const allVisibleChecked = visibleCheckboxes.every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allVisibleChecked;
    }

    // Maneja la selección de "Seleccionar todo"
    selectAllCheckbox.addEventListener('change', function () {
        const visibleCheckboxes = Array.from(itemCheckboxes).filter(checkbox => checkbox.closest('tr').style.display !== 'none');
        visibleCheckboxes.forEach(checkbox => checkbox.checked = selectAllCheckbox.checked);
        toggleDeleteButton();
    });

    // Maneja los cambios en los checkboxes individuales
    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            actualizarSelectAllCheckbox();
            toggleDeleteButton();
        });
    });

    // Contiene la lógica para agregar los IDs seleccionados al formulario cuando se confirma la eliminación
    confirmSelectedDeleteButton.addEventListener('click', function () {
        // Primero, eliminar todos los inputs ocultos existentes
        formEliminarHoras.querySelectorAll('input[name="ids"]').forEach(input => input.remove());

        // Luego, agregar los IDs seleccionados como inputs ocultos
        itemCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                agregarCampoOculto(formEliminarHoras, 'ids', checkbox.value);
            }
        });

        // Finalmente, envía el formulario
        formEliminarHoras.submit();
    });

    // Inicializa el estado del botón de eliminar
    toggleDeleteButton();
    actualizarSelectAllCheckbox();
});
