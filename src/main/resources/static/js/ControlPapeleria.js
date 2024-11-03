document.addEventListener('DOMContentLoaded', function () {
    var editModal = document.getElementById('editModal');
    editModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var id = button.getAttribute('data-id');
        var fecha = button.getAttribute('data-fecha');
        var cantidad = button.getAttribute('data-cantidad');
        var persona = button.getAttribute('data-persona');
        
        var modalId = editModal.querySelector('#editId');
        var modalFecha = editModal.querySelector('#editFecha');
        var modalCantidad = editModal.querySelector('#editCantidad');
        var modalPersona = editModal.querySelector('#editPersona');

        modalId.value = id;
        modalFecha.value = fecha;
        modalCantidad.value = cantidad;
        modalPersona.value = persona;
    });
});

document.addEventListener("DOMContentLoaded", function () {
    // Establecer la fecha actual en el campo de fecha de entrega
    var fechaCampo = document.getElementById("entregaFecha");
    var hoy = new Date().toISOString().split('T')[0];
    fechaCampo.value = hoy;

    var form = document.getElementById("formNuevaEntrega");

    form.addEventListener("submit", function (event) {
        var isValid = true;

        // Validar fecha (siempre debe estar llena ya que es proporcionada por el sistema)
        if (!fechaCampo.value) {
            fechaCampo.classList.add("is-invalid");
            isValid = false;
        } else {
            fechaCampo.classList.remove("is-invalid");
        }

        // Validar persona
        var persona = form.querySelector("#entregaPersona");
        if (!persona.value.trim()) {
            persona.classList.add("is-invalid");
            isValid = false;
        } else {
            persona.classList.remove("is-invalid");
        }

        // Validar selección de producto
        var producto = form.querySelector("[name='inventarioPapeleria']");
        if (producto.value === "") {
            producto.classList.add("is-invalid");
            isValid = false;
        } else {
            producto.classList.remove("is-invalid");
        }

        // Validar cantidad
        var cantidad = form.querySelector("#entregaCantidad");
        if (cantidad.value <= 0) {
            cantidad.classList.add("is-invalid");
            isValid = false;
        } else {
            cantidad.classList.remove("is-invalid");
        }

        if (!isValid) {
            event.preventDefault();
            event.stopPropagation();
            form.classList.add("was-validated");
        }
    });
});



$(document).ready(function() {
    $("#entregaPersona").on("input", function() {
        let query = $(this).val();
        
        if (query.length >= 2) { // Realizar la búsqueda cuando haya al menos 2 caracteres
            $.ajax({
                url: "/Biblioteca/Papeleria/buscarPersonas",
                type: "GET",
                data: { term: query },
                success: function(data) {
                    // Crear un contenedor para mostrar las sugerencias
                    let suggestions = "<ul class='list-group'>";
                    
                    // Recorrer los resultados y agregarlos a la lista de sugerencias
                    data.forEach(function(persona) {
                        suggestions += "<li class='list-group-item'>" + persona + "</li>";
                    });
                    suggestions += "</ul>";

                    // Mostrar las sugerencias debajo del input
                    $("#suggestions").html(suggestions);

                    // Manejar el clic en una sugerencia
                    $(".list-group-item").on("click", function() {
                        $("#entregaPersona").val($(this).text());
                        $("#suggestions").html(""); // Limpiar las sugerencias
                    });
                }
            });
        } else {
            $("#suggestions").html(""); // Limpiar las sugerencias si no hay suficientes caracteres
        }
    });
});

document.getElementById('cancelarBtn').addEventListener('click', function() {
    // Guardar el valor del campo de fecha
    let fechaEntrega = document.getElementById('entregaFecha').value;

    // Limpiar todos los campos del formulario
    document.getElementById('formNuevaEntrega').reset();

    // Restaurar el valor del campo de fecha
    document.getElementById('entregaFecha').value = fechaEntrega;
});