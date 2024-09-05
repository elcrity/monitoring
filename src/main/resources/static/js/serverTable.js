let timeLabel;
let isRepeat = false;
let serverHistory;
let selectedId;

async function deleteServer(serverId) {
  const confirmed = confirm('서버를 삭제하시겠습니까?');
  if (!confirmed) return;

  try {
    const response = await fetch(`/api/delete/${serverId}`, {
      method: 'DELETE',
    });

    if (response.ok) {
      // 서버 응답이 성공적이면 해당 항목을 화면에서 제거
      document.getElementById(`deleteButton-${serverId}`).closest('#serverTable').remove();
    } else {
      console.error('Failed to delete the server');
    }
  } catch (error) {
    console.error('Error deleting server:', error);
  }
}

const callDrawData = async (serverId, isRepeat, date = null) => {
  const selectedDay = date ? date : formatDateToLocalDateTime(new Date());
  try {
    serverHistory = await fetch('/getHistory', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        serverId,
        isRepeat,
        date : selectedDay // 날짜 추가
      })
    }).then((response) => response.json());
    const timeLabel = makeAxis(serverHistory);
    await drawChart(serverHistory, timeLabel, isRepeat);
  } catch (error) {
    console.error('Error callDrawData:', error);
  }
}

const callHistory = async (serverId, isRepeat, selectedDate) => {
  try {
    const response = await fetch(`/getLogs/${serverId}`, {
      method: 'GET',
    });

    if (response.ok) {
      const historyBody = document.getElementById('historyBody');
      if (historyBody) {
        const html = await response.text();
        // 기존 HTML 내용과 비교하여 변경된 부분만 업데이트
        if (!isRepeat) {
          historyBody.innerHTML = html;
          // console.log(html)
        }
        // 데이터를 가졍로때 isRepeat면 #menu에 있는 내용만 교체하기
        if(isRepeat){
          const parser = new DOMParser();
          const doc = parser.parseFromString(html, 'text/html');
          // 새로운 HTML에서 #menu 요소를 가져오기
          const newMenu = doc.querySelector('#menu');
          const oldMenu = document.getElementById('menu');
          oldMenu.innerHTML = newMenu.innerHTML;
        }
        // Display block 상태 유지
        if (historyBody.style.display !== 'block') {
          historyBody.style.display = 'block';
        }
        updateUsageColors();
        // Fetch and draw chart
        callDrawData(serverId, isRepeat, selectedDate)
           .then((response) => {})
           .catch((error) => {
             // 에러가 발생한 경우
             console.error('데이터를 처리하는 중 오류가 발생했습니다:', error);
             alert('데이터를 처리하는 중 오류가 발생했습니다.');
           });
      }
    } else {
      console.error('Fetch error:', response);
    }
  } catch (error) {
    console.error('Error show History:', error);
  }
};

const showHistory = async (serverId) => {
  clearInterval(mainIntervalId);
  clearInterval(intervalId);
  selectedDate = formatDateToLocalDateTime(new Date());
  selectedId=serverId;
  isRepeat = false;
  xScale.min = 0;
  xScale.max = 1440;
  await callHistory(serverId);


  const elems = document.querySelectorAll('.datepicker_input');
  elems.forEach(elem => regDatepicker(elem));
  // 이미 intervalId가 설정되어 있으면 추가로 설정하지 않음
  if (!intervalId) {
    // 서버 정보 클릭시 기존의 메인 화면 갱신하는 interval을 정지 후 history를 갱신하는 코드와 동기화
    mainIntervalId = setInterval(async  () => {
      isRepeat = true;
      await fetchData();
      updateIndicators();
    }, 10000); // 10초마다 호출
    intervalId = setInterval(async  () => {
      // 드로우만 다시해서 menu 데이터 갱신이 아노딤
      await callHistory(serverId, isRepeat, selectedDate); // 서버 ID를 사용하여 showHistory 호출
      updateUsageColors();
    }, 10000); // 10초마다 호출
  }
}
