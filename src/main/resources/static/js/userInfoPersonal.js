// 비밀번호 보이기/숨기기 토글 기능
document.addEventListener('DOMContentLoaded', () => {
    const togglePassword1 = document.getElementById('togglePassword1');
    const passwordField = document.getElementById('password');
    const togglePassword2 = document.getElementById('togglePassword2');
    const confirmPasswordField = document.getElementById('confirmPassword');

    if (togglePassword1 && passwordField) {
        togglePassword1.addEventListener('click', () => {
            const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordField.setAttribute('type', type);
            togglePassword1.querySelector('i').classList.toggle('bi-eye');
            togglePassword1.querySelector('i').classList.toggle('bi-eye-slash');
        });
    }

    if (togglePassword2 && confirmPasswordField) {
        togglePassword2.addEventListener('click', () => {
            const type = confirmPasswordField.getAttribute('type') === 'password' ? 'text' : 'password';
            confirmPasswordField.setAttribute('type', type);
            togglePassword2.querySelector('i').classList.toggle('bi-eye');
            togglePassword2.querySelector('i').classList.toggle('bi-eye-slash');
        });
    }
});