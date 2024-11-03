// VALIDAR CAMPOS VACIOS
document.addEventListener('DOMContentLoaded', function () {
    // Selecciona el formulario
    var form = document.querySelector('.needs-validation');
    
    // Agrega el evento de submit para validar el formulario
    form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
});

// MÉTODO DE SALTO A SIGUIENTE INPUT
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input[data-next]');
    inputs.forEach(input => {
      input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
          e.preventDefault();
          const nextInputId = this.dataset.next;
          const nextInput = document.getElementById(nextInputId);
          if (nextInput) {
            nextInput.focus();
          }
        }
      });
    });
});