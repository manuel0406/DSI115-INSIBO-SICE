$(document).ready(function () {
    $('#docente_celular').mask('0000-0000');
    $('#docente_telefono_fijo').mask('0000-0000');
    $('#docente_dui').mask('00000000-0');
    $('#docente_nup').mask('000000000000');
    $('#docente_nip').mask('0000000');

    // Variables para almacenar el último valor ingresado
    let lastConventionalValue = '';

    // Función para aplicar la máscara según el radio seleccionado
    function applyDefaultMask() {
        if ($('#homologado').is(':checked')) {
            $('#docente_nit').prop('readonly', true);
            $('#docente_nit').mask('00000000-0');
            var duiValue = $('#docente_dui').val();
            $('#docente_nit').val(duiValue);
        } else if ($('#convencional').is(':checked')) {
            $('#docente_nit').prop('readonly', false);
            $('#docente_nit').mask('0000-000000-000-0');
        }
    }

    // Llama a la función para aplicar la máscara al cargar la página
    applyDefaultMask();

    // Cambia la máscara al cambiar el radio
    $('input[type=radio][name=nitOption]').change(function () {
        if (this.value === 'homologado') {
            lastConventionalValue = $('#docente_nit').val();
            $('#docente_nit').prop('readonly', true);
            $('#docente_nit').mask('00000000-0');
            var duiValue = $('#docente_dui').val();
            $('#docente_nit').val(duiValue);
        } else if (this.value === 'convencional') {
            $('#docente_nit').prop('readonly', false);
            $('#docente_nit').mask('0000-000000-000-0');
            $('#docente_nit').val(lastConventionalValue);
        }
    });

    // Actualiza NIT en tiempo real según DUI cuando está en modo homologado
    $('#docente_dui').on('input', function () {
        if ($('#homologado').is(':checked')) {
            $('#docente_nit').val($(this).val());
        }
    });
});