$(document).ready(function () {
  // Función para actualizar el listado de docentes
  function actualizarListadoDocentes(idMateria, duiDocenteActual) {
    $.ajax({
      url: "/DocentesMax",
      type: "GET",
      data: { idMateria: idMateria },
      success: function (response) {
        var docentesHtml = '<option value="">Seleccionar docente...</option>';
        response.forEach(function (docente) {
          docentesHtml +=
            '<option value="' +
            docente.duiDocente +
            '"' +
            (docente.duiDocente === duiDocenteActual ? " selected" : "") +
            ">" +
            docente.nombreDocente +
            " " +
            docente.apellidoDocente +
            "</option>";
        });
        $("#profesor").html(docentesHtml);
        // Inicialmente desactivar el botón si el docente seleccionado es el mismo que el actual
        toggleSubmitButton(duiDocenteActual);
      },
      error: function (error) {
        $("#profesor").html(
          '<option value="">Error al cargar los docentes</option>'
        );
      },
    });
  }

  // Función para habilitar o deshabilitar el botón de actualización
  function toggleSubmitButton(duiDocenteActual) {
    var duiDocenteSeleccionado = $("#profesor").val();
    if (
      duiDocenteSeleccionado === "" ||
      duiDocenteActual === duiDocenteSeleccionado
    ) {
      $("#submit-button").prop("disabled", true);
    } else {
      $("#submit-button").prop("disabled", false);
    }
  }

  // Al abrir el modal de actualización
  $("#actualizarMateriaModal").on("show.bs.modal", function (event) {
    var button = $(event.relatedTarget);
    var idMateria = button.data("id-materia");
    var duiDocenteActual = button.data("profesor");

    // Actualizar el listado de docentes
    actualizarListadoDocentes(idMateria, duiDocenteActual);

    // Llenar el formulario con los datos actuales
    $("#idAsignacion").val(button.data("id"));
    $("#editarIdMateria").val(button.data("id-materia"));
    $("#editarMateria").val(button.data("materia"));
    $("#editarBachillerato").val(button.data("cod-bachillerato"));
    $("#profesor")
      .val(button.data("profesor"))
      .data("current-docente", duiDocenteActual);
  });

  // Validar el formulario antes de enviarlo
  $("#profesor").on("change", function () {
    var duiDocenteActual = $("#profesor").data("current-docente");
    toggleSubmitButton(duiDocenteActual);
  });

  $("#editarAsignacionForm").on("submit", function (event) {
    event.preventDefault(); // Evita el envío automático del formulario

    var duiDocenteActual = $("#profesor").data("current-docente");
    var duiDocenteSeleccionado = $("#profesor").val();

    if (
      duiDocenteSeleccionado === "" ||
      duiDocenteActual === duiDocenteSeleccionado
    ) {
      $("#error-message").removeClass("d-none");
      $("#submit-button").prop("disabled", true); // Desactiva el botón si la selección es vacía o el mismo docente
    } else {
      $("#error-message").addClass("d-none");
      $("#submit-button").prop("disabled", false); // Habilita el botón si la selección es diferente
      this.submit(); // Envía el formulario si la validación pasa
    }
  });
});

// Capturar clic en el botón de eliminar y asignar el href al botón de confirmación
$(document).on("click", ".delete-btn", function () {
  const idAsignacion = $(this).data("id");
  const idMateria = $(this).data("id-materia");

  // Formar la URL para eliminar con los parámetros
  const deleteUrl =
    "/eliminarAsignacion?idMateria=" +
    idMateria +
    "&idAsignacion=" +
    idAsignacion;

  // Asignar la URL al botón de confirmación en el modal
  $("#confirmDeleteButton").attr("href", deleteUrl);
});
