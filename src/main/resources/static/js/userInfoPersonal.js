document.addEventListener('DOMContentLoaded', () => {

    // 1. 카카오 주소 검색 API 연동 (embed 방식)
    const btnSearchBusinessAddress = document.getElementById('btnSearchBusinessAddress');
    const addressSearchModal = document.getElementById('addressSearchModal'); // <div id="addressSearchModal"> (모달 오버레이)
    const addressSearchContainer = document.getElementById('addressSearchContainer'); // <div id="addressSearchContainer"> (주소 검색 API가 삽입될 곳)

    if (btnSearchBusinessAddress && addressSearchModal && addressSearchContainer) { // closeAddressSearchModal은 이제 필요 없으므로 제거
        btnSearchBusinessAddress.addEventListener('click', function() {
            addressSearchModal.classList.add('active'); // 모달 오버레이 활성화

            new daum.Postcode({
                oncomplete: function(data) {
                    // 검색된 도로명 주소와 우편번호를 해당 입력 필드에 채워 넣습니다.
                    document.getElementById('businessRoadAddress').value = data.roadAddress;
                    document.getElementById('businessZipCode').value = data.zonecode;
                    // 상세 주소 필드에 포커스를 주어 사용자가 직접 나머지 주소를 입력하도록 유도합니다.
                    document.getElementById('businessDetailAddress').focus();

                    addressSearchModal.classList.remove('active'); // 주소 검색 완료 시 모달 비활성화
                    addressSearchContainer.innerHTML = ''; // embed된 주소 검색 UI 비우기
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

        // ⭐ 모달 외부 클릭 시 닫기 기능 추가 (기존 코드) ⭐
        addressSearchModal.addEventListener('click', function(event) {
            // 클릭된 요소가 모달 오버레이 자체이고, 모달 콘텐츠 영역이 아닌 경우
            if (event.target === addressSearchModal) {
                addressSearchModal.classList.remove('active');
                addressSearchContainer.innerHTML = '';
            }
        });

        // ⭐ ESC 키를 눌렀을 때 모달 닫기 (기존 코드) ⭐
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape' && addressSearchModal.classList.contains('active')) {
                addressSearchModal.classList.remove('active');
                addressSearchContainer.innerHTML = '';
            }
        });
    }

    // ⭐⭐⭐ 2. 사업자 등록번호 확인 기능 (Ajax로 구현) 시작 ⭐⭐⭐
    // ⭐ 새로 추가된 변수 선언: 사업장 관리확인 버튼 ⭐
    const btnVerifyBusinessId = document.getElementById('btnVerifyBusinessId');
    // ⭐ 새로 추가된 변수 선언: HTML에 추가된 결과를 표시할 div 요소 ⭐
    const businessVerificationResultDiv = document.getElementById('businessVerificationResult');

    // ⭐ 사업장 관리확인 버튼과 결과 표시 div가 모두 존재하는지 확인 ⭐
    if (btnVerifyBusinessId && businessVerificationResultDiv) {
        // ⭐ 사업장 관리확인 버튼에 클릭 이벤트 리스너 추가 ⭐
        btnVerifyBusinessId.addEventListener('click', async () => {
            // ⭐ 입력된 사업자등록번호 가져오기 ⭐
            const businessRegistrationNumber = document.getElementById('businessRegistrationNumber').value;
            // ⭐ 하이픈 제거 (국세청 API는 숫자만 받음) ⭐
            const cleanBusinessNumber = businessRegistrationNumber.replace(/-/g, '');

            // ⭐ 입력값 유효성 검사 ⭐
            if (!cleanBusinessNumber) {
                businessVerificationResultDiv.style.color = 'red';
                businessVerificationResultDiv.textContent = '사업자등록번호를 입력해주세요.';
                return;
            }
            if (cleanBusinessNumber.length !== 10 || isNaN(cleanBusinessNumber)) { // 10자리 숫자인지 확인
                businessVerificationResultDiv.style.color = 'red';
                businessVerificationResultDiv.textContent = '유효한 10자리 숫자로 된 사업자등록번호를 입력해주세요.';
                return;
            }

            // ⭐ 상태 메시지 초기화 및 로딩 표시 ⭐
            businessVerificationResultDiv.style.color = 'gray';
            businessVerificationResultDiv.textContent = '사업자 등록 상태 확인 중...';

            try {
                // ⭐ 여러분의 백엔드 API 엔드포인트로 Ajax (fetch API) 요청 ⭐
                const response = await fetch('/api/checkBusinessStatus', {
                    method: 'POST', // ⭐ POST 메소드 사용 ⭐
                    headers: {
                        'Content-Type': 'application/json' // ⭐ JSON 형식으로 요청 본문 전송 명시 ⭐
                    },
                    // ⭐ 요청 본문에 사업자번호 배열을 JSON 문자열로 변환하여 포함 ⭐
                    body: JSON.stringify({ businessNumbers: [cleanBusinessNumber] })
                });

                // ⭐ HTTP 응답이 성공(2xx)이 아니면 오류 발생 ⭐
                if (!response.ok) {
                    const errorText = await response.text(); // 서버에서 보낸 오류 메시지 텍스트 가져오기
                    throw new Error(errorText || '백엔드 API 호출 실패');
                }

                // ⭐ 서버 응답을 JSON 객체로 파싱 ⭐
                const data = await response.json();

                // ⭐ 응답 데이터 확인 및 UI 업데이트 ⭐
                if (data && data.data && data.data.length > 0) {
                    const statusInfo = data.data[0]; // 첫 번째 사업자 정보 가져옴
                    let statusMessage = '';
                    let statusColor = '';

                    // ⭐ 사업자 상태 코드에 따라 메시지와 색상 설정 ⭐
                    switch (statusInfo.b_stt_cd) {
                        case '01': // 계속사업자
                            statusMessage = `정상 사업자 (${statusInfo.tax_type})`;
                            statusColor = 'green';
                            break;
                        case '02': // 폐업자
                            const closeDt = statusInfo.close_dt;
                            const formattedCloseDt = closeDt ?
                                `${closeDt.substring(0, 4)}년 ${closeDt.substring(4, 6)}월 ${closeDt.substring(6, 8)}일` :
                                '정보 없음';
                            statusMessage = `폐업 사업자 (폐업일: ${formattedCloseDt})`;
                            statusColor = 'red';
                            break;
                        case '04': // 휴업자
                            statusMessage = `휴업 사업자`;
                            statusColor = 'orange';
                            break;
                        default: // 그 외 알 수 없는 상태
                            statusMessage = `확인된 상태: ${statusInfo.b_stt || '정보 없음'}`;
                            statusColor = 'gray';
                            break;
                    }
                    businessVerificationResultDiv.style.color = statusColor;
                    businessVerificationResultDiv.textContent = statusMessage;

                } else {
                    // ⭐ 조회된 사업자 정보가 없는 경우 ⭐
                    businessVerificationResultDiv.style.color = 'red';
                    businessVerificationResultDiv.textContent = '조회된 사업자 정보가 없거나 유효하지 않습니다.';
                }

            } catch (error) {
                // ⭐ API 호출 또는 처리 중 발생한 오류 처리 ⭐
                console.error('사업자등록상태 확인 중 오류 발생:', error);
                businessVerificationResultDiv.style.color = 'red';
                businessVerificationResultDiv.textContent = `오류 발생: ${error.message}`;
            }
        });
    }
    // ⭐⭐⭐ 2. 사업자 등록번호 확인 기능 (Ajax로 구현) 끝 ⭐⭐⭐


    // 3. 비밀번호 보이기/숨기기 토글 기능 (변동 없음, 기존 코드)
    const togglePassword1 = document.getElementById('togglePassword1');
    const passwordField = document.getElementById('password');
    const togglePassword2 = document.getElementById('togglePassword2');
    const confirmPasswordField = document.getElementById('PasswordConfirm');

    function setupPasswordToggle(toggleButton, passwordInput) {
        if (toggleButton && passwordInput) {
            toggleButton.addEventListener('click', () => {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);

                const iconElement = toggleButton.querySelector('i');
                if (iconElement) {
                    iconElement.classList.toggle('bi-eye');
                    iconElement.classList.toggle('bi-eye-slash');
                }
                const imgElement = toggleButton.querySelector('img.passwordEyesIcon');
                if (imgElement) {
                    // 실제 아이콘 이미지 파일이 두 종류(눈을 뜬/감은)라면 src를 변경
                    // 예시: imgElement.src = (type === 'text') ? '/images/eye-open.svg' : '/images/eye-closed.svg';
                    // 현재 HTML에서 동일한 이미지를 사용하므로, 이 부분의 src 변경 로직은 생략되어 있습니다.
                }
            });
        }
    }
    setupPasswordToggle(togglePassword1, passwordField);
    setupPasswordToggle(togglePassword2, confirmPasswordField);
});