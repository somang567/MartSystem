// /js/signup.js

document.addEventListener('DOMContentLoaded', function() {
    const chkAll = document.getElementById('chkAll');
    const chkRequired = document.querySelectorAll('.chk-required');
    const chkOptional = document.querySelectorAll('.chk-optional');
    const btnNext = document.getElementById('btnNext');

    // [1] '모두 동의' 체크박스가 변경될 때
    chkAll.addEventListener('change', function() {
        const isChecked = this.checked;
        chkRequired.forEach(checkbox => checkbox.checked = isChecked);
        chkOptional.forEach(checkbox => checkbox.checked = isChecked);
        checkAllRequired(); // 필수 약관 동의 상태 확인 및 버튼 활성화/비활성화
    });

    // [2] 개별 체크박스가 변경될 때
    [...chkRequired, ...chkOptional].forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            // 모든 필수 약관이 체크되었는지 확인
            const allRequiredChecked = Array.from(chkRequired).every(cb => cb.checked);

            // '모두 동의' 체크박스 상태 업데이트
            // 모든 필수 AND 모든 선택 약관이 체크되어야 '모두 동의'도 체크됨
            if (allRequiredChecked && Array.from(chkOptional).every(cb => cb.checked)) {
                chkAll.checked = true;
            } else {
                chkAll.checked = false;
            }

            checkAllRequired(); // 필수 약관 동의 상태 확인 및 버튼 활성화/비활성화
        });
    });

    // [3] 필수 약관 동의 여부에 따라 '동의하고 가입하기' 버튼 활성화/비활성화
    function checkAllRequired() {
        const allRequiredChecked = Array.from(chkRequired).every(checkbox => checkbox.checked);
        if (allRequiredChecked) {
            btnNext.removeAttribute('disabled');
        } else {
            btnNext.setAttribute('disabled', 'true');
        }
    }

    // [4] 페이지 로드 시 버튼 초기 상태 확인
    checkAllRequired();

    // [5] 아코디언 열기/닫기 시 아이콘 변경
    document.querySelectorAll('.view-terms-btn').forEach(button => {
        const icon = button.querySelector('i');
        const targetId = button.getAttribute('data-bs-target');
        const target = document.querySelector(targetId);

        if (target) {
            // 펼쳐지기 시작할 때 아이콘 변경
            target.addEventListener('show.bs.collapse', () => {
                icon.classList.remove('bi-chevron-down');
                icon.classList.add('bi-chevron-up');
            });

            // 접히기 시작할 때 아이콘 변경
            target.addEventListener('hide.bs.collapse', () => {
                icon.classList.remove('bi-chevron-up');
                icon.classList.add('bi-chevron-down');
            });

            // 페이지 로드 시 초기 상태에 따라 아이콘 설정
            if (target.classList.contains('show')) { // Bootstrap 'show' 클래스로 현재 상태 확인
                icon.classList.remove('bi-chevron-down');
                icon.classList.add('bi-chevron-up');
            } else {
                icon.classList.remove('bi-chevron-up');
                icon.classList.add('bi-chevron-down');
            }
        }
    });
});