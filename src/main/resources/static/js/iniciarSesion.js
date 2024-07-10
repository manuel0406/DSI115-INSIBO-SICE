function validarCorreo(inputId, spanId) {
    // Obtener el valor del campo de entrada
    var correo = document.getElementById(inputId).value;
    // Obtener el elemento span para mostrar el mensaje de error
    var mensajeError = document.getElementById(spanId);
    // Expresión regular para validar el formato del correo electrónico
    var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // Verificar si el correo electrónico cumple con el formato esperado
    if (regex.test(correo)) {
        mensajeError.style.display = 'none'; // Ocultar el mensaje de error
    } else {
        mensajeError.style.display = 'inline'; // Mostrar el mensaje de error
    }
}

function moveToNextInput(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Evitar que el formulario se envíe
        var form = event.target.form;
        var index = Array.prototype.indexOf.call(form, event.target);
        if (index + 1 < form.elements.length) {
            form.elements[index + 1].focus();
        }
    }
}

// Llamar a la función validarCorreo al quitar el foco del campo de entrada
document.getElementById('username').addEventListener('blur', function() {
    validarCorreo('username', 'correoInvalido');
});

// También llamar a la función validarCorreo al presionar la tecla Enter
document.getElementById('username').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        validarCorreo('username', 'correoInvalido');
        moveToNextInput(event);
    }
});

document.getElementById('password').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        moveToNextInput(event);
    }
});

function validateForm() {
    const correo = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const botonIniciar = document.getElementById('botonIniciar');

    if (correo && password) {
        botonIniciar.disabled = false;
    } else {
        botonIniciar.disabled = true;
    }
}