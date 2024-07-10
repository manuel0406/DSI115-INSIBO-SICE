$(document).ready(function () {
    $('#administrativo_celular').mask('0000-0000');
    $('#administrativo_telefono_fijo').mask('0000-0000');
    $('#administrativo_dui').mask('00000000-0');
    $('#administrativo_nup').mask('000000000000');
    $('#administrativo_nit').mask('0000-000000-000-0');

    // Variable para almacenar el último valor ingresado
    let lastConventionalValue = '';

    $('input[type=radio][name=nitOption]').change(function () {
      if (this.value === 'homologado') {
        // Guarda el valor actual antes de cambiar a homologado
        lastConventionalValue = $('#administrativo_nit').val();
        $('#administrativo_nit').prop('readonly', true);
        $('#administrativo_nit').mask('00000000-0');
        var duiValue = $('#administrativo_dui').val();
        $('#administrativo_nit').val(duiValue);
      } else if (this.value === 'convencional') {
        // Restaura el último valor ingresado cuando se vuelve a convencional
        $('#administrativo_nit').prop('readonly', false);
        $('#administrativo_nit').mask('0000-000000-000-0');
        $('#administrativo_nit').val(lastConventionalValue);
      }
    });
  });