(() => {
    'use strict'

    const forms = document.querySelectorAll('.needs-validation')
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()

                // Find the first invalid element
                const firstInvalidElement = form.querySelector(':invalid')
                if (firstInvalidElement) {
                    // Scroll to the first invalid element
                    firstInvalidElement.scrollIntoView({ behavior: 'smooth', block: 'center' })
                    // Optionally focus on the first invalid element
                    firstInvalidElement.focus()
                }
            }

            form.classList.add('was-validated')
        }, false)
    })
})()