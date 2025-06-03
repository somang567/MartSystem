// userTypeSignup.js 파일 내용
document.addEventListener('DOMContentLoaded', () => {
    const typeCards = document.querySelectorAll('.type-card');
    const btnNext = document.getElementById('btnNext');
    const selectedUserTypeInput = document.getElementById('selectedUserType');

    // 초기 로드 시 '동의하고 다음으로' 버튼 비활성화 (선택된 카드가 없을 때)
    btnNext.disabled = true;

    typeCards.forEach(card => {
        card.addEventListener('click', () => {
            // 모든 카드에서 'active' 클래스 제거
            typeCards.forEach(c => c.classList.remove('active'));

            // 클릭된 카드에 'active' 클래스 추가
            card.classList.add('active');

            // 숨겨진 입력 필드에 선택된 카드의 data-type 값 설정
            const type = card.getAttribute('data-type');
            selectedUserTypeInput.value = type;

            // '동의하고 다음으로' 버튼 활성화
            btnNext.disabled = false;
        });
    });

    // '동의하고 다음으로' 버튼 클릭 시 페이지 이동
    btnNext.addEventListener('click', () => {
        const type = selectedUserTypeInput.value;
        if (type) {
            // 이 경로가 컨트롤러의 @GetMapping("/register/signupAgree")와 일치해야 합니다.
            window.location.href = `/register/signupAgree?type=${type}`;
        }
    });
});