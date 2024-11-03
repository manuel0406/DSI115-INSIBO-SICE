$(document).ready(function () {
    $('#administrativo_celular').mask('0000-0000');
    $('#administrativo_telefono_fijo').mask('0000-0000');
    $('#administrativo_dui').mask('00000000-0');
    $('#administrativo_nup').mask('000000000000');

    // Variable para almacenar el último valor ingresado en convencional
    let lastConventionalValue = '';

    // Función para aplicar la máscara según el radio seleccionado al cargar la página
    function applyDefaultMask() {
        if ($('#homologado').is(':checked')) {
            $('#administrativo_nit').prop('readonly', true);
            $('#administrativo_nit').mask('00000000-0');
            var duiValue = $('#administrativo_dui').val();
            $('#administrativo_nit').val(duiValue);
        } else if ($('#convencional').is(':checked')) {
            $('#administrativo_nit').prop('readonly', false);
            $('#administrativo_nit').mask('0000-000000-000-0');
        }
    }

    // Llama a la función para aplicar la máscara al cargar la página
    applyDefaultMask();

    // Cambia la máscara cuando se selecciona un radio diferente
    $('input[type=radio][name=nitOption]').change(function () {
        if (this.value === 'homologado') {
            lastConventionalValue = $('#administrativo_nit').val();
            $('#administrativo_nit').prop('readonly', true);
            $('#administrativo_nit').mask('00000000-0');
            var duiValue = $('#administrativo_dui').val();
            $('#administrativo_nit').val(duiValue);
        } else if (this.value === 'convencional') {
            $('#administrativo_nit').prop('readonly', false);
            $('#administrativo_nit').mask('0000-000000-000-0');
            $('#administrativo_nit').val(lastConventionalValue);
        }
    });

        // Actualiza NIT en tiempo real según DUI cuando está en modo homologado
        $('#administrativo_dui').on('input', function () {
          if ($('#homologado').is(':checked')) {
              $('#administrativo_nit').val($(this).val());
          }
      });
});