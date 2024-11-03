$(document).ready(function() {
    function updateMasterCheckbox(groupClass, masterCheckboxId) {
        var allChecked = $(groupClass).length === $(groupClass + ':checked').length;
        $(masterCheckboxId).prop('checked', allChecked);
    }

    function validateCheckboxes() {
        var valid = $('.primeros-anos:checked').length > 0 || 
                    $('.segundos-anos:checked').length > 0 || 
                    $('.terceros-anos:checked').length > 0;
        
        // Aplicar estilos según la validez de los checkboxes
        if (!valid) {
            $('#error-message').show(); // Mostrar el mensaje de error si no hay selección
            $('.checkbox-group').addClass('border-danger').removeClass('border-success');
            $('.label-checkbox-group').addClass('text-danger').removeClass('text-success');
        } else {
            $('#error-message').hide(); // Ocultar el mensaje de error si hay al menos una selección
            $('.checkbox-group').addClass('border-success').removeClass('border-danger');
            $('.label-checkbox-group').addClass('text-success').removeClass('text-danger');
        }
        return valid;
    }

    function validateProfessorSelection() {
        var valid = $('#profesor').val() !== "";
        if (!valid) {
            $('#profesor').addClass('is-invalid');
        } else {
            $('#profesor').removeClass('is-invalid').addClass('is-valid');
        }
        return valid;
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    // Usar eventos delegados para manejar elementos dinámicos
    $(document).on('change', '.marcar-todos', function() {
        var groupClass = '.' + $(this).data('group');
        $(groupClass).prop('checked', $(this).is(':checked'));
        validateCheckboxes(); // Validar después de cambiar el estado de los checkboxes
    });

    $(document).on('change', '.form-check-input:not(.marcar-todos)', function() {
        var groupClass = '.' + $(this).attr('class').split(' ')[1];
        var masterCheckboxId = '#marcarTodos' + capitalizeFirstLetter(groupClass);
        updateMasterCheckbox(groupClass, masterCheckboxId);
        validateCheckboxes(); // Validar después de cambiar el estado de los checkboxes
    });

    $(document).on('change', '.form-check-input.primeros-anos', function() {
        updateMasterCheckbox('.primeros-anos', '#marcarTodosPrimerosAnos');
        validateCheckboxes(); // Validar después de cambiar el estado de los checkboxes
    });

    $(document).on('change', '.form-check-input.segundos-anos', function() {
        updateMasterCheckbox('.segundos-anos', '#marcarTodosSegundosAnos');
        validateCheckboxes(); // Validar después de cambiar el estado de los checkboxes
    });

    $(document).on('change', '.form-check-input.terceros-anos', function() {
        updateMasterCheckbox('.terceros-anos', '#marcarTodosTercerosAnos');
        validateCheckboxes(); // Validar después de cambiar el estado de los checkboxes
    });

    // Validación adicional para al menos un checkbox seleccionado
    $(document).on('submit', 'form.needs-validation', function(event) {
        var checkboxesValid = validateCheckboxes();
        var professorValid = validateProfessorSelection();

        if (!checkboxesValid || !professorValid) {
            event.preventDefault();
            event.stopPropagation();
            if (!checkboxesValid) {
                $('#error-message').show(); // Mostrar el mensaje de error si no hay selección
            }
        } else {
            $('#error-message').hide(); // Ocultar el mensaje de error si hay al menos una selección
        }
        $(this).addClass('was-validated');
    });

    // Inicializar el mensaje de error como oculto
    $('#error-message').hide();
    // Inicializar los bordes como no válidos
    $('.checkbox-group').removeClass('border-danger');
    $('.label-checkbox-group').removeClass('text-danger');

    // Validar campo de selección de profesor al cambiar la selección
    $('#profesor').on('change', function() {
        validateProfessorSelection();
    });
});
