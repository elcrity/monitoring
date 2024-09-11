const getDatePickerTitle = elem => {
  // From the label or the aria-label
  const label = elem.nextElementSibling;
  let titleText = '';
  if (label && label.tagName === 'LABEL') {
    titleText = label.textContent;
  } else {
    titleText = elem.getAttribute('aria-label') || '';
  }
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
    dataKeySet = ['cpuUsage', 'memoryUsage', 'diskUsage1'];
    isRepeat = false;
    if (intervalIdMap.has(selectedId)) {
      clearInterval(intervalIdMap.get(selectedId)); // 기존 interval 제거
    }
    clearInterval(mainIntervalId)
    selectedDate = formatDateToLocalDateTime(datepicker.getDate());
    callDrawData(selectedId, isRepeat, selectedDate);

    mainIntervalId = setInterval(async () => {
      await fetchData();
      updateIndicators();
    }, timeDelay);

    const newIntervalId = setInterval(async () => {
      isRepeat = true;
      await callHistory(selectedId, isRepeat);
      await callDrawData(selectedId, isRepeat, selectedDate);
    }, timeDelay);

    // intervalIdMap에 새로운 intervalId 저장
    intervalIdMap.set(selectedId, newIntervalId); // 서버별 intervalId 저장
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