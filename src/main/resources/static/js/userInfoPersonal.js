document.addEventListener('DOMContentLoaded', () => {
    /** -------------------- 1. 카카오 주소 검색 API 연동 -------------------- **/
    const btnSearchBusinessAddress = document.getElementById('btnSearchBusinessAddress');
    const addressSearchModal = document.getElementById('addressSearchModal');
    const addressSearchContainer = document.getElementById('addressSearchContainer');

    if (btnSearchBusinessAddress && addressSearchModal && addressSearchContainer) {
        btnSearchBusinessAddress.addEventListener('click', function () {
            addressSearchModal.classList.add('active');
            new daum.Postcode({
                oncomplete: function (data) {
                    document.getElementById('businessRoadAddress').value = data.roadAddress;
                    document.getElementById('businessZipCode').value = data.zonecode;
                    document.getElementById('businessDetailAddress').focus();
                    addressSearchModal.classList.remove('active');
                    addressSearchContainer.innerHTML = '';
                },
                width: '100%',
                height: '100%',
                theme: {
                    bgColor: "#FFFFFF",
                    searchBgColor: "#ffffff",
                    postcodeTextColor: "#ff0000",
                    emphTextColor: "#2d60fa",
                    outlineColor: "#a3a3a3"
                }
            }).embed(addressSearchContainer);
        });

        addressSearchModal.addEventListener('click', function (event) {
            if (event.target === addressSearchModal) {
                addressSearchModal.classList.remove('active');
                addressSearchContainer.innerHTML = '';
            }
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && addressSearchModal.classList.contains('active')) {
                addressSearchModal.classList.remove('active');
                addressSearchContainer.innerHTML = '';
            }
        });
    }

    /** -------------------- 2. 사업자 등록번호 확인 -------------------- **/
    const btnVerifyBusinessId = document.getElementById('btnVerifyBusinessId');
    const businessVerificationResultDiv = document.getElementById('businessVerificationResult');

    if (btnVerifyBusinessId && businessVerificationResultDiv) {
        btnVerifyBusinessId.addEventListener('click', async () => {
            const businessRegistrationNumber = document.getElementById('businessRegistrationNumber').value;
            const cleanBusinessNumber = businessRegistrationNumber.replace(/-/g, '');

            if (!cleanBusinessNumber || cleanBusinessNumber.length !== 10 || isNaN(cleanBusinessNumber)) {
                businessVerificationResultDiv.style.color = 'red';
                businessVerificationResultDiv.textContent = '유효한 사업자등록번호를 입력해주세요.';
                return;
            }

            businessVerificationResultDiv.style.color = 'gray';
            businessVerificationResultDiv.textContent = '조회 중...';

            try {
                const response = await fetch('/api/checkBusinessStatus', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ businessNumbers: [cleanBusinessNumber] })
                });

                const data = await response.json();

                if (response.ok && data.data?.length > 0) {
                    const status = data.data[0].b_stt_cd;
                    const message = {
                        '01': [`정상 사업자 (${data.data[0].tax_type || ''})`, 'green'],
                        '02': [`폐업 사업자 (폐업일: ${data.data[0].close_dt ? `${data.data[0].close_dt.substring(0, 4)}년 ${data.data[0].close_dt.substring(4, 6)}월 ${data.data[0].close_dt.substring(6, 8)}일` : '정보 없음'})`, 'red'],
                        '04': ['휴업 사업자', 'orange']
                    }[status] || ['확인된 상태 없음', 'gray'];

                    businessVerificationResultDiv.style.color = message[1];
                    businessVerificationResultDiv.textContent = message[0];
                } else {
                    businessVerificationResultDiv.style.color = 'red';
                    businessVerificationResultDiv.textContent = '조회된 사업자 정보가 없습니다.';
                }

            } catch (err) {
                businessVerificationResultDiv.style.color = 'red';
                businessVerificationResultDiv.textContent = '오류 발생: ' + err.message;
            }
        });
    }

    /** -------------------- 3. 이메일 입력 및 중복 검사 -------------------- **/
    const emailIdInput = document.getElementById('emailId');
    const emailDomainInput = document.getElementById('emailDomainInput');
    const emailDomainSelect = document.getElementById('emailDomainSelect');
    const hiddenEmailInput = document.getElementById('email');
    const btnCheckId = document.getElementById('btnCheckId');
    const emailDuplicationMessageDiv = document.getElementById('emailDuplicationMessage');
    const emailCheckStatusInput = document.getElementById('emailCheckStatus');
    const registrationForm = document.getElementById('registrationForm');

    function toggleCustomEmailInput(show) {
        emailDomainInput.style.display = show ? 'inline-block' : 'none';
        if (!show) emailDomainInput.value = '';
    }

    function setEmailFieldsReadonly(isReadonly) {
        emailIdInput.readOnly = isReadonly;
        emailDomainInput.readOnly = isReadonly;
        emailDomainSelect.disabled = isReadonly;
        btnCheckId.disabled = isReadonly;

        const bgColor = isReadonly ? '#e9ecef' : '';
        emailIdInput.style.backgroundColor = bgColor;
        emailDomainInput.style.backgroundColor = bgColor;
        emailDomainSelect.style.backgroundColor = bgColor;

        emailIdInput.classList.toggle('is-valid', isReadonly);
        emailDomainInput.classList.toggle('is-valid', isReadonly && emailDomainInput.style.display !== 'none');
        emailDomainSelect.classList.toggle('is-valid', isReadonly);
    }

    function updateHiddenEmailField() {
        const id = emailIdInput.value.trim();
        const domain = emailDomainSelect.value === 'custom' ? emailDomainInput.value.trim() : emailDomainSelect.value;
        hiddenEmailInput.value = id && domain ? `${id}@${domain}` : '';
        emailCheckStatusInput.value = 'unchecked';
        emailDuplicationMessageDiv.textContent = '';
        setEmailFieldsReadonly(false);
    }

    function onEmailDomainSelectChange() {
        toggleCustomEmailInput(emailDomainSelect.value === 'custom');
        updateHiddenEmailField();
    }

    function initializeEmailDomain() {
        const initialEmail = hiddenEmailInput.value;
        let initialDomain = '';
        if (initialEmail && initialEmail.includes('@')) {
            initialDomain = initialEmail.split('@')[1];
        }

        let found = false;
        for (let option of emailDomainSelect.options) {
            if (option.value === initialDomain) {
                emailDomainSelect.value = initialDomain;
                found = true;
                break;
            }
        }

        if (!found || !initialDomain) {
            emailDomainSelect.value = 'custom';
            toggleCustomEmailInput(true);
            emailDomainInput.value = initialDomain || '';
        } else {
            toggleCustomEmailInput(false);
        }

        updateHiddenEmailField();

        if (initialEmail && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(initialEmail)) {
            setEmailFieldsReadonly(true);
            emailDuplicationMessageDiv.textContent = '이메일 정보가 이미 입력되어 있습니다.';
            emailDuplicationMessageDiv.style.color = 'gray';
            emailCheckStatusInput.value = 'available';
        }
    }

    if (emailIdInput && emailDomainInput && emailDomainSelect && hiddenEmailInput) {
        emailIdInput.addEventListener('input', updateHiddenEmailField);
        emailDomainInput.addEventListener('input', updateHiddenEmailField);
        emailDomainSelect.addEventListener('change', onEmailDomainSelectChange);
        initializeEmailDomain();
    }

    if (btnCheckId) {
        btnCheckId.addEventListener('click', async () => {
            updateHiddenEmailField();
            const email = hiddenEmailInput.value;

            if (!email) {
                emailDuplicationMessageDiv.textContent = '이메일 주소를 입력해주세요.';
                emailDuplicationMessageDiv.style.color = 'red';
                return;
            }

            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                emailDuplicationMessageDiv.textContent = '유효한 이메일 형식이 아닙니다.';
                emailDuplicationMessageDiv.style.color = 'red';
                return;
            }

            try {
                const res = await fetch('/api/checkEmailDuplication', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email })
                });
                const data = await res.json();
                if (res.ok && data.duplicated) {
                    emailDuplicationMessageDiv.textContent = '이미 사용 중인 이메일입니다.';
                    emailDuplicationMessageDiv.style.color = 'red';
                    setEmailFieldsReadonly(false);
                    emailCheckStatusInput.value = 'duplicated';
                } else {
                    emailDuplicationMessageDiv.textContent = '사용 가능한 이메일입니다.';
                    emailDuplicationMessageDiv.style.color = 'green';
                    setEmailFieldsReadonly(true);
                    emailCheckStatusInput.value = 'available';
                }
            } catch (err) {
                emailDuplicationMessageDiv.textContent = '중복 확인 중 오류가 발생했습니다.';
                emailDuplicationMessageDiv.style.color = 'red';
            }
        });
    }

    if (registrationForm) {
        registrationForm.addEventListener('submit', (event) => {
            if (emailCheckStatusInput.value !== 'available') {
                event.preventDefault();
                emailDuplicationMessageDiv.textContent = '이메일 중복 확인을 완료해주세요.';
                emailDuplicationMessageDiv.style.color = 'red';
                alert('이메일 중복 확인을 완료해주세요.');
            }
        });
    }

    /** -------------------- 4. 비밀번호 입력 및 확인 -------------------- **/
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('passwordConfirm');
    const passwordMatchFeedbackDiv = document.getElementById('passwordMatchFeedback');
    const lengthFeedback = document.getElementById('lengthFeedback');
    const uppercaseFeedback = document.getElementById('uppercaseFeedback');
    const lowercaseFeedback = document.getElementById('lowercaseFeedback');
    const numberFeedback = document.getElementById('numberFeedback');
    const specialCharFeedback = document.getElementById('specialCharFeedback');

    function updateFeedback(el, valid) {
        if (!el) return;
        const icon = el.querySelector('i');
        el.style.setProperty('color', valid ? 'green' : 'red', 'important');
        if (icon) {
            icon.classList.toggle('bi-check-circle-fill', valid);
            icon.classList.toggle('bi-x-circle-fill', !valid);
            icon.style.setProperty('color', valid ? 'green' : 'red', 'important');
        }
    }

    function checkPasswordMatch() {
        if (!passwordInput || !confirmPasswordInput) return;
        if (passwordInput.value === confirmPasswordInput.value) {
            passwordMatchFeedbackDiv.textContent = '비밀번호가 일치합니다.';
            passwordMatchFeedbackDiv.style.color = 'green';
        } else {
            passwordMatchFeedbackDiv.textContent = '비밀번호가 일치하지 않습니다.';
            passwordMatchFeedbackDiv.style.color = 'red';
        }
    }

    if (passwordInput) {
        passwordInput.addEventListener('input', () => {
            const pw = passwordInput.value;
            updateFeedback(lengthFeedback, pw.length >= 8 && pw.length <= 16);
            updateFeedback(uppercaseFeedback, /[A-Z]/.test(pw));
            updateFeedback(lowercaseFeedback, /[a-z]/.test(pw));
            updateFeedback(numberFeedback, /\d/.test(pw));
            updateFeedback(specialCharFeedback, /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(pw));
            checkPasswordMatch();
        });
    }

    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', checkPasswordMatch);
    }

    /** -------------------- 5. 비밀번호 보이기/숨기기 -------------------- **/
    function setupPasswordToggle(toggleButtonId, passwordFieldId) {
        const toggleButton = document.getElementById(toggleButtonId);
        const passwordField = document.getElementById(passwordFieldId);
        if (toggleButton && passwordField) {
            toggleButton.addEventListener('click', () => {
                const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordField.setAttribute('type', type);
                const icon = toggleButton.querySelector('i');
                if (icon) {
                    icon.classList.toggle('bi-eye');
                    icon.classList.toggle('bi-eye-slash');
                }
            });
        }
    }

    setupPasswordToggle('togglePassword1', 'password');
    setupPasswordToggle('togglePassword2', 'passwordConfirm');
});
햐