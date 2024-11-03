// BOTON DE FILTRACION
document.getElementById('filtrarBtn').addEventListener('click', function() {
    // Obtener el valor seleccionado en el <select>
    var tipo = document.getElementById('filtroTipoMateria').value;

    // Construir la URL con el parámetro tipo
    if (tipo == "") {
        var url = '/GestionMaterias'
    } else {
        var url = '/GestionMaterias?tipo=' + encodeURIComponent(tipo);
    }
    // Redirigir a la nueva URL
    window.location.href = url;
});

// Se actualizan la variables
var codMateria;
var nomMateria;
var tipoMateria;

// LLENADO DE MODAL DE ACTUALIZAR/EDITAS
$(document).ready(function() {
    $('.editar-btn').on('click', function() {
        var idMateria = $(this).data('id');
        codMateria = $(this).data('cod');
        nomMateria = $(this).data('nom');
        tipoMateria = $(this).data('tipo');

        $('#idMateria').val(idMateria);
        $('#editCodMateria').val(codMateria);
        $('#editNomMateria').val(nomMateria);
        $('#editTipoMateria').val(tipoMateria);
    });
});

// MOSTRAR MODAL DE ELIMINAR / MENSAJE DE CONFIRMACION
document.addEventListener("DOMContentLoaded", function() {
    var deleteButton = document.querySelectorAll('.delete-btn');
    var modalDelete = new bootstrap.Modal(document.getElementById('eliminarMateriaModal'));
    var confirmDeleteButton = document.getElementById('confirmarEliminarMateria');
    var modalBody = document.querySelector('.modal-body-delete');
    var currentHref = '';
  
    deleteButton.forEach(function(button) {
        button.addEventListener('click', function(event) {
            event.preventDefault(); // Evitar el comportamiento por defecto del botón
        
            // Obtener el atributo href del botón actual (si es necesario)
            currentHref = button.getAttribute('href');
        
            // Obtener la fila (tr) padre del botón actual
            var row = button.closest('tr');
            
            // Obtener el contenido de la columna 2 (índice 1 en base 0)
            var materia = row.cells[1].textContent.trim();
            
            // Modificar el texto del modal con el correo obtenido
            modalBody.textContent = "¿Deseas eliminar la materia de " + materia + "?";
        
            // Mostrar el modal de confirmación
            modalDelete.show();
        });
    });
  
    confirmDeleteButton.addEventListener('click', function() {
        window.location.href = currentHref;
    });
});


// Obtener Materias en BD
var materias = JSON.parse(materiasJson);

document.addEventListener('DOMContentLoaded', function() {
    // Obtener campos de entrada "Nueva Materia"
    var inputCod = document.getElementById('codMateria');
    var inputNom = document.getElementById('nomMateria');
    var inputTipo = document.getElementById('tipoMateria');

    // Obtener campos de entrada "Actualizar Materia"
    var editInputCod = document.getElementById('editCodMateria');
    var editInputNom = document.getElementById('editNomMateria');
    var editInputTipo = document.getElementById('editTipoMateria');

    // Obtener mensajes de texto "Nueva Materia"
    var errorMessageContainerC = document.getElementById('errorCodMateria');
    var errorMessageContainerN = document.getElementById('errorNomMateria');
    var errorMessageContainerT = document.getElementById('errorTipoMateria');

    // Obtener mensajes de texto "Actualizar Materia"
    var editErrorMessageContainerC = document.getElementById('editErrorCodMateria');
    var editErrorMessageContainerN = document.getElementById('editErrorNomMateria');
    var editErrorMessageContainerT = document.getElementById('editErrorTipoMateria');

    var saveButton = document.getElementById('saveButton'); // Botón de guardar
    var updateButton = document.getElementById('upadateButton'); // Botón de actualizar

    function validateCodMateria(input, errorMessageContainer) {
        var query = input.value.trim().toLowerCase();
        if (query !== "") {
            var results = materias.filter(function(materia) {
                return materia.codMateria.toLowerCase() === query;
            });
            if (results.length !== 0) {
                errorMessageContainer.textContent = "El código de la materia ya existe.";
                input.className = 'form-control is-invalid';
            } else {
                errorMessageContainer.textContent = "";
                input.className = 'form-control is-valid';
            }
        } else {
            errorMessageContainer.textContent = "Por favor, ingrese el código de la materia.";
            input.className = 'form-control is-invalid';
        }
    }

    function validateNomMateria(input, errorMessageContainer) {
        var query = input.value.trim().toLowerCase();
        if (query !== "") {
            var results = materias.filter(function(materia) {
                return materia.nomMateria.toLowerCase() === query;
            });
            if (results.length !== 0) {
                errorMessageContainer.textContent = "El nombre de la materia ya existe.";
                input.className = 'form-control is-invalid';
            } else {
                errorMessageContainer.textContent = "";
                input.className = 'form-control is-valid';
            }
        } else {
            errorMessageContainer.textContent = "Por favor, ingrese el nombre de la materia.";
            input.className = 'form-control is-invalid';
        }
    }

    function validateTipoMateria(input, errorMessageContainer) {
        var selectedValue = input.value;
        if (selectedValue === "") {
            errorMessageContainer.textContent = "Por favor, seleccione un tipo de materia.";
            input.className = 'form-select is-invalid';
        } else {
            errorMessageContainer.textContent = "";
            input.className = 'form-select is-valid';
        }
    }

    function updateSaveButtonState() {
        var isTipoValid = inputTipo.value !== "";
        var isCodValid = inputCod.value.trim().toLowerCase() !== "";
        var isNomValid = inputNom.value.trim().toLowerCase() !== "";
        var hasNoErrors = errorMessageContainerC.textContent === "" && errorMessageContainerN.textContent === "" && errorMessageContainerT.textContent === "";
        saveButton.disabled = !(isTipoValid && isCodValid && isNomValid && hasNoErrors);
    }

    function updateUpdateButtonState() {
        var isTipoValid = editInputTipo.value !== "";
        var isCodValid = editInputCod.value.trim().toLowerCase() !== "";
        var isNomValid = editInputNom.value.trim().toLowerCase() !== "";
        var isValidNom = editInputNom.classList.contains("is-valid");
        var isValidCod = editInputCod.classList.contains("is-valid");
        var hasNoErrors = (isValidNom || isValidCod) && editErrorMessageContainerC.textContent === "" && editErrorMessageContainerN.textContent === "" && editErrorMessageContainerT.textContent === "";
        updateButton.disabled = !(isTipoValid && isCodValid && isNomValid && hasNoErrors);
    }

    // NUEVA MATERIA
    inputCod.addEventListener('input', function() {
        validateCodMateria(inputCod, errorMessageContainerC);
        updateSaveButtonState();
    });

    inputNom.addEventListener('input', function() {
        validateNomMateria(inputNom, errorMessageContainerN);
        updateSaveButtonState();
    });

    inputTipo.addEventListener('change', function() {
        validateTipoMateria(inputTipo, errorMessageContainerT);
        updateSaveButtonState();
    });

    // ACTUALIZAR MATERIA
    editInputCod.addEventListener('input', function() {
        var query = editInputCod.value.trim().toLowerCase();
        if(query !== codMateria.toLowerCase()) {
            validateCodMateria(editInputCod, editErrorMessageContainerC);
        }
        else{
            editInputCod.className = 'form-control';
            editErrorMessageContainerC.textContent = "";
        }
        updateUpdateButtonState();
    });

    editInputNom.addEventListener('input', function() {
        var query = editInputNom.value.trim().toLowerCase();
        if(query !== nomMateria.toLowerCase()) {
            validateNomMateria(editInputNom, editErrorMessageContainerN);
        }
        else{
            editInputNom.className = 'form-control';
            editErrorMessageContainerN.textContent = "";
        }
        updateUpdateButtonState();
    });

    editInputTipo.addEventListener('change', function() {
        var selectedValue = editInputTipo.value;
        if(selectedValue !== tipoMateria) {
            validateTipoMateria(editInputTipo, editErrorMessageContainerT);
        }
        else{
            editInputTipo.className = 'form-select';
            editErrorMessageContainerT.textContent = "";
        }
        updateUpdateButtonState();
    });

    function cerrarModalN() {
        var form = document.getElementById('formNuevaMateria');
        form.reset();
        form.classList.remove('was-validated');  // Eliminar la clase de validación

        // Limpieza de campos
        inputCod.className = 'form-control';
        inputNom.className = 'form-control';
        inputTipo.className = 'form-select';

        // Obtener mensajes de texto
        errorMessageContainerC.textContent = "";
        errorMessageContainerN.textContent = "";
        errorMessageContainerT.textContent = "";
    }

    function cerrarModalA() {
        var form = document.getElementById('formActualizarMateria');
        form.reset();
        form.classList.remove('was-validated');  // Eliminar la clase de validación

        // Limpieza de campos
        editInputCod.className = 'form-control';
        editInputNom.className = 'form-control';
        editInputTipo.className = 'form-select';

        // Obtener mensajes de texto
        editErrorMessageContainerC.textContent = "";
        editErrorMessageContainerN.textContent = "";
        editErrorMessageContainerT.textContent = "";
    }

    var iconoCerrarN = document.getElementById('cancelarNuevaMateriaIco');
    var btnCerrarModalN = document.getElementById('cancelarNuevaMateria');
    var iconoCerrarA = document.getElementById('cancelarActualizarMateriaIco');
    var btnCerrarModalA = document.getElementById('cancelarActualizarMateria');

    iconoCerrarN.addEventListener('click', cerrarModalN);
    btnCerrarModalN.addEventListener('click', cerrarModalN);
    iconoCerrarA.addEventListener('click', cerrarModalA);
    btnCerrarModalA.addEventListener('click', cerrarModalA);
});