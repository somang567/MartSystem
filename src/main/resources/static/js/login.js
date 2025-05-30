document.addEventListener('DOMContentLoaded', function() {
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const loginButton = document.querySelector('.btn-login');

    function checkInputs() {
        if (emailInput.value.trim().length > 0 && passwordInput.value.trim().length > 0) {
            loginButton.classList.add('active');
            loginButton.removeAttribute('disabled');
        } else {
            loginButton.classList.remove('active');
            loginButton.setAttribute('disabled', 'true');
        }
    }

    emailInput.addEventListener('input', checkInputs);
    passwordInput.addEventListener('input', checkInputs);
});