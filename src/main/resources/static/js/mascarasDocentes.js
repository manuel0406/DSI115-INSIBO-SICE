$(document).ready(function () {
    $('#docente_celular').mask('0000-0000');
    $('#docente_telefono_fijo').mask('0000-0000');
    $('#docente_dui').mask('00000000-0');
    $('#docente_nup').mask('000000000000');
    $('#docente_nip').mask('0000000');
    $('#docente_nit').mask('0000-000000-000-0');

    // Variable para almacenar el último valor ingresado
    let lastConventionalValue = '';

    $('input[type=radio][name=nitOption]').change(function () {
        if (this.value === 'homologado') {
            // Guarda el valor actual antes de cambiar a homologado
            lastConventionalValue = $('#docente_nit').val();
            $('#docente_nit').prop('readonly', true);
            $('#docente_nit').mask('00000000-0');
            var duiValue = $('#docente_dui').val();
            $('#docente_nit').val(duiValue);
        } else if (this.value === 'convencional') {
            // Restaura el último valor ingresado cuando se vuelve a convencional
            $('#docente_nit').prop('readonly', false);
            $('#docente_nit').mask('0000-000000-000-0');
            $('#docente_nit').val(lastConventionalValue);
        }
    });
});