document.addEventListener("DOMContentLoaded", function() {
    var today = new Date().toISOString().split('T')[0]; // Obtiene la fecha en formato 'YYYY-MM-DD'
    document.getElementById('fechaPrestamo').value = today; // Asigna la fecha al input
});

document.addEventListener("DOMContentLoaded", function() {
    const confirmDevolverBtn = document.getElementById("confirmDevolverBtn");
    const devolverForm = document.getElementById("devolverForm");

    // Variables para almacenar los datos del préstamo seleccionado
    let prestamoId = null;
    let inventarioId = null;
    let cantidadPrestamo = null;

    // Agregar eventos a todos los botones de devolución
    document.querySelectorAll(".btn-devolver").forEach(button => {
      button.addEventListener("click", function() {
        // Obtener los datos del préstamo desde los atributos data-id, data-inventario, y data-cantidad
        prestamoId = this.getAttribute("data-id");
        inventarioId = this.getAttribute("data-inventario");
        cantidadPrestamo = this.getAttribute("data-cantidad");
      });
    });

    // Manejar el clic en el botón de confirmar devolución
    confirmDevolverBtn.addEventListener("click", function() {
      if (prestamoId && inventarioId && cantidadPrestamo) {
        // Actualizar la acción del formulario con el id del préstamo seleccionado
        devolverForm.action = `/Biblioteca/Prestamos/devolver/${prestamoId}`;

        // Asegurar que el idInventario y cantidadPrestamo se envíen correctamente
        const idInventarioInput = devolverForm.querySelector("input[name='idInventario']");
        const cantidadPrestamoInput = devolverForm.querySelector("input[name='cantidadPrestamo']");
        
        idInventarioInput.value = inventarioId;
        cantidadPrestamoInput.value = cantidadPrestamo;

        // Enviar el formulario
        devolverForm.submit();
      }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formPrestamo");

    form.addEventListener("submit", function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }

        form.classList.add("was-validated");
    });
});


$(document).ready(function() {
  $('#alumnoInput').on('input', function() {
      let term = $(this).val();
      
      if (term.length >= 2) { // Realizar la búsqueda cuando haya al menos 2 caracteres
          $.ajax({
              url: '/Biblioteca/buscarAlumnos',  // Endpoint del controlador para buscar alumnos
              type: 'GET',
              data: { term: term },
              success: function(alumnos) {
                  // Crear un contenedor para mostrar las sugerencias
                  let suggestions = "<ul class='list-group'>";

                  if (alumnos.length > 0) {
                      alumnos.forEach(function(alumno) {
                          suggestions += "<li class='list-group-item' data-id='" + alumno.idAlumno + "'>" + alumno.nombreAlumno + ' ' + alumno.apellidoAlumno + "</li>";
                      });
                  } else {
                      suggestions += "<li class='list-group-item'>No se encontraron resultados</li>";
                  }

                  suggestions += "</ul>";

                  // Mostrar las sugerencias debajo del input
                  $('#alumnoSuggestions').html(suggestions).removeClass('hidden');

                  // Manejar el clic en una sugerencia
                  $('.list-group-item').on('click', function() {
                      $('#alumnoInput').val($(this).text());
                      $('#alumnoId').val($(this).data('id')); // Establece el idAlumno en el campo oculto
                      $('#alumnoSuggestions').html(""); // Limpiar las sugerencias
                  });
              }
          });
      } else {
          $('#alumnoSuggestions').html(""); // Limpiar las sugerencias si no hay suficientes caracteres
      }
  });

  $(document).click(function(e) {
      if (!$(e.target).closest('#alumnoInput, #alumnoSuggestions').length) {
          $('#alumnoSuggestions').addClass('hidden');
      }
  });
});


document.getElementById('cancelarBtn').addEventListener('click', function() {
  // Guardar el valor del campo de fecha
  let fechaEntrega = document.getElementById('fechaPrestamo').value;

  // Limpiar todos los campos del formulario
  document.getElementById('formPrestamo').reset();

  // Restaurar el valor del campo de fecha
  document.getElementById('fechaPrestamo').value = fechaEntrega;
});


$(document).ready(function() {
  $('#formPrestamo').on('submit', function(e) {
      // Verifica si el alumnoId está vacío
      if ($('#alumnoId').val() === '') {
          e.preventDefault(); // Evita el envío del formulario
          $('#alumnoInput').addClass('is-invalid'); // Marcar el campo como inválido
          $('#alumnoInput').focus(); // Enfocar el campo de entrada
      } else {
          $('#alumnoInput').removeClass('is-invalid'); // Remover clase si el campo es válido
      }
  });
});
