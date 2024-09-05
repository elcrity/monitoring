let selectedDate;
const getDatePickerTitle = elem => {
  // From the label or the aria-label
  const label = elem.nextElementSibling;
  let titleText = '';
  if (label && label.tagName === 'LABEL') {
    titleText = label.textContent;
  } else {
    titleText = elem.getAttribute('aria-label') || '';
  }
  console.log(titleText)
  return titleText;
}

const regDatepicker = (elem) => {
  const today = new Date();
  const datepicker = new Datepicker(elem, {
    'format': 'yyyy/mm/dd', // UK format
    'title': getDatePickerTitle(elem),
    'defaultViewDate': today
  });
  datepicker.setDate(today);
  elem.addEventListener('changeDate', (event) => {
    selectedDate = formatDateToLocalDateTime(datepicker.getDate());
    callDrawData(selectedId, isRepeat, selectedDate)
       .then((response) => {})
       .catch((error) => {
         // 에러가 발생한 경우의 로직
         console.error('데이터를 처리하는 중 오류가 발생했습니다:', error);
         // 사용자에게 에러 메시지 표시
         alert('데이터를 처리하는 중 오류가 발생했습니다.');
       });
  });
};

const formatDateToLocalDateTime = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더해줌
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}`;
};