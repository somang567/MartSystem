document.addEventListener('DOMContentLoaded', () => {

    /** ------------------------------ 1. 카카오 주소 검색 API 연동 ------------------------------ **/
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

    /** ------------------------------ 2. 사업자 등록번호 확인 기능 ------------------------------ **/
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

    /** ------------------------------ 3. 이메일 입력 처리 및 중복 검사 ------------------------------ **/
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

        const readonlyBgColor = '#e9ecef'; // 회색 배경
        const defaultBgColor = ''; // 기본 배경

        emailIdInput.style.backgroundColor = isReadonly ? readonlyBgColor : defaultBgColor;
        emailDomainInput.style.backgroundColor = isReadonly ? readonlyBgColor : defaultBgColor;
        emailDomainSelect.style.backgroundColor = isReadonly ? readonlyBgColor : defaultBgColor;

        // is-valid 클래스를 토글하여 Bootstrap의 유효성 피드백 스타일 적용 (녹색 테두리 등)
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

    function initializeEmailDomain() {
        const initialEmail = hiddenEmailInput.value;
        let initialDomain = '';
        if (initialEmail && initialEmail.includes('@')) {
            initialDomain = initialEmail.substring(initialEmail.indexOf('@') + 1);
        }

        let foundPredefinedDomain = false;
        for (let i = 0; i < emailDomainSelect.options.length; i++) {
            if (emailDomainSelect.options[i].value === initialDomain) {
                emailDomainSelect.value = initialDomain;
                foundPredefinedDomain = true;
                break;
            }
        }

        if (!foundPredefinedDomain || !initialDomain) {
            emailDomainSelect.value = 'custom';
            toggleCustomEmailInput(true);
            emailDomainInput.value = initialDomain || '';
        } else {
            toggleCustomEmailInput(false);
        }

        updateHiddenEmailField();

        if (initialEmail && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(initialEmail)) {
            setEmailFieldsReadonly(true);
            emailDuplicationMessageDiv.textContent = '이메일 정보가 이미 입력되어 있습니다. 수정이 필요하면 필드를 변경하거나 관리자에게 문의하세요.';
            emailDuplicationMessageDiv.style.color = 'gray';
            emailCheckStatusInput.value = 'available';
        } else {
            setEmailFieldsReadonly(false);
            emailCheckStatusInput.value = 'unchecked';
        }
    }

    if (emailIdInput && emailDomainInput && emailDomainSelect && hiddenEmailInput && btnCheckId && emailDuplicationMessageDiv && emailCheckStatusInput) {
        emailIdInput.addEventListener('input', updateHiddenEmailField);
        emailDomainInput.addEventListener('input', updateHiddenEmailField);
        emailDomainSelect.addEventListener('change', onEmailDomainSelectChange); // JS에서 이벤트 리스너 등록

        // 페이지 로드 시 초기화 함수 호출
        initializeEmailDomain();
    }

    function onEmailDomainSelectChange() {
        toggleCustomEmailInput(emailDomainSelect.value === 'custom');
        updateHiddenEmailField();
    }

    if (btnCheckId) {
        btnCheckId.addEventListener('click', async () => {
            updateHiddenEmailField();
            const email = hiddenEmailInput.value;

            if (!email) {
                emailDuplicationMessageDiv.textContent = '이메일 주소를 입력해주세요.';
                emailDuplicationMessageDiv.style.color = 'red';
                emailCheckStatusInput.value = 'unchecked';
                return;
            }

            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                emailDuplicationMessageDiv.textContent = '유효한 이메일 형식이 아닙니다.';
                emailDuplicationMessageDiv.style.color = 'red';
                emailCheckStatusInput.value = 'unchecked';
                return;
            }

            try {
                const response = await fetch('/api/checkEmailDuplication', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email })
                });

                const data = await response.json();

                if (response.ok && data.duplicated) {
                    emailDuplicationMessageDiv.textContent = '이미 사용 중인 이메일입니다. 다른 이메일을 사용해주세요.';
                    emailDuplicationMessageDiv.style.color = 'red';
                    setEmailFieldsReadonly(false);
                    emailCheckStatusInput.value = 'duplicated';
                } else if (response.ok && !data.duplicated) {
                    emailDuplicationMessageDiv.textContent = '사용 가능한 이메일입니다.';
                    emailDuplicationMessageDiv.style.color = 'green';
                    setEmailFieldsReadonly(true);
                    emailCheckStatusInput.value = 'available';
                } else {
                    throw new Error('서버 오류');
                }
            } catch (error) {
                emailDuplicationMessageDiv.textContent = '중복 확인 중 오류가 발생했습니다.';
                emailDuplicationMessageDiv.style.color = 'red';
                setEmailFieldsReadonly(false);
                emailCheckStatusInput.value = 'unchecked';
            }
        });
    }

    if (registrationForm) {
        registrationForm.addEventListener('submit', (event) => {
            if (emailCheckStatusInput.value !== 'available') {
                event.preventDefault();
                emailDuplicationMessageDiv.textContent = '이메일 중복 확인을 완료하고 사용 가능한 이메일인지 확인해야 합니다.';
                emailDuplicationMessageDiv.style.color = 'red';
                alert('이메일 중복 확인을 완료해주세요.');
            }
        });
    }

    /** ------------------------------ 4. 비밀번호 보이기/숨기기 & 유효성 검사 ------------------------------ **/
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('passwordConfirm');
    const passwordMatchFeedbackDiv = document.getElementById('passwordMatchFeedback'); // 비밀번호 일치 피드백 div
    const passwordValidationFeedbackDiv = document.getElementById('passwordValidationFeedback'); // 비밀번호 유효성 피드백 div

    // 각 피드백 요소들을 직접 참조합니다.
    const lengthFeedback = document.getElementById('lengthFeedback');
    const uppercaseFeedback = document.getElementById('uppercaseFeedback');
    const lowercaseFeedback = document.getElementById('lowercaseFeedback');
    const numberFeedback = document.getElementById('numberFeedback');
    const specialCharFeedback = document.getElementById('specialCharFeedback');

    // 개별 피드백 항목 업데이트 함수
    const updateFeedback = (element, isValid) => {
        if (!element) {
            // console.warn(`Feedback element not found: ${element ? element.id : 'unknown'}`); // 디버깅 로그 제거
            return;
        }
        const icon = element.querySelector('i');

        // 텍스트 색상 변경 (!important 추가하여 강제 적용)
        element.style.setProperty('color', isValid ? 'green' : 'red', 'important');

        if (icon) {
            icon.classList.toggle('bi-x-circle-fill', !isValid);
            icon.classList.toggle('bi-check-circle-fill', isValid);
            // 아이콘 색상도 텍스트 색상과 동일하게 강제 적용
            icon.style.setProperty('color', isValid ? 'green' : 'red', 'important');
        } else {
            // console.warn(`Icon element (<i>) not found inside: ${element.id}`); // 디버깅 로그 제거
        }
        // console.log(`Feedback for ${element.id}: isValid=${isValid}, color=${element.style.color}`); // 디버깅 로그 제거
    };

    // 비밀번호 유효성 검사 (실시간)
    if (passwordInput && passwordValidationFeedbackDiv) {
        passwordInput.addEventListener('input', () => {
            const password = passwordInput.value;

            const isLengthValid = password.length >= 8 && password.length <= 16;
            updateFeedback(lengthFeedback, isLengthValid);

            const isUppercaseValid = /[A-Z]/.test(password);
            updateFeedback(uppercaseFeedback, isUppercaseValid);

            const isLowercaseValid = /[a-z]/.test(password);
            updateFeedback(lowercaseFeedback, isLowercaseValid);

            const isNumberValid = /[0-9]/.test(password);
            updateFeedback(numberFeedback, isNumberValid);

            const isSpecialCharValid = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(password);
            updateFeedback(specialCharFeedback, isSpecialCharValid);

            checkPasswordMatch();
        });
    }

    // 비밀번호 일치 여부 확인
    if (passwordInput && confirmPasswordInput && passwordMatchFeedbackDiv) {
        const checkPasswordMatch = () => {
            if (passwordInput.value.length === 0 && confirmPasswordInput.value.length === 0) {
                passwordMatchFeedbackDiv.textContent = ''; // 둘 다 비어있으면 메시지 없음
                passwordMatchFeedbackDiv.style.color = ''; // 색상 초기화
            } else if (passwordInput.value === confirmPasswordInput.value) {
                passwordMatchFeedbackDiv.textContent = '비밀번호가 일치합니다.';
                passwordMatchFeedbackDiv.style.color = 'green';
            } else {
                passwordMatchFeedbackDiv.textContent = '비밀번호가 일치하지 않습니다.';
                passwordMatchFeedbackDiv.style.color = 'red';
            }
        };
        // 비밀번호 또는 확인 비밀번호 입력 시마다 체크
        passwordInput.addEventListener('input', checkPasswordMatch);
        confirmPasswordInput.addEventListener('input', checkPasswordMatch);
        // 초기 로드 시 한 번 실행
        checkPasswordMatch();
    }

    // 비밀번호 보이기/숨기기 토글
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