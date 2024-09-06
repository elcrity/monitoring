let timeLabel;
let isRepeat = false;
let serverHistory;
let selectedId;
let timeDelay = 10000;

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

const callDrawData = async (serverId, repeated, date = null) => {
  // const selectedDay = date ? date : formatDateToLocalDateTime(new Date());
  const selectedDay = date;
  // console.log("called Drawdata : " , JSON.stringify({
  //   serverId,
  //   isRepeat : Boolean(repeated),
  //   date : selectedDay // 날짜 추가
  // }))
  try {
    serverHistory = await fetch('/getHistory', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        serverId,
        isRepeat : Boolean(repeated),
        date : selectedDay // 날짜 추가
      })
    }).then(async (response) => {
      if (response.ok) {
        return response.json()
      } else {
        const errorText = await response.text();
        window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
      }
    })
    const timeLabel = makeAxis(serverHistory);
    await drawChart(serverHistory, timeLabel, repeated);
  } catch (error) {
    console.error('데이터를 처리하는 중 오류가 발생했습니다:', error);
    alert('데이터를 처리하는 중 오류가 발생했습니다.');
  }
}

const callHistory = async (serverId, repeated) => {
  try {
    const response = await fetch(`/getLogs/${serverId}`, {
      method: 'GET',
    });

    if (response.ok) {
      const historyBody = document.getElementById('historyBody');
      if (historyBody) {
        const html = await response.text();
        // 기존 HTML 내용과 비교하여 변경된 부분만 업데이트
        if (!repeated) {
          historyBody.innerHTML = html;
          // console.log(html)
        }else{
        // 데이터를 가졍로때 repeated면 #menu에 있는 내용만 교체하기
          const parser = new DOMParser();
          const doc = parser.parseFromString(html, 'text/html');
          // 새로운 HTML에서 #menu 요소를 가져오기
          const newMenu = doc.getElementById('menu');
          const oldMenu = document.getElementById('menu');
          oldMenu.innerHTML = newMenu.innerHTML;
        }

        // Display block 상태 유지
        if (historyBody.style.display !== 'block') {
          historyBody.style.display = 'block';
        }
        updateUsageColors();

      }
    } else {
      const errorText = await response.text();
      window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
    }
  } catch (error) {
    console.log("에?러 : ",error)
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
  await callHistory(serverId).then(()=>callDrawData(serverId, isRepeat, selectedDate));

  const elems = document.querySelectorAll('.datepicker_input');
  elems.forEach(elem => regDatepicker(elem));
  // 이미 intervalId가 설정되어 있으면 추가로 설정하지 않음
  if (!intervalId) {
    // 서버 정보 클릭시 기존의 메인 화면 갱신하는 interval을 정지 후 history를 갱신하는 코드와 동기화
    mainIntervalId = setInterval(async  () => {
      await fetchData();
      updateIndicators();
    }, timeDelay); // 10초마다 호출
    intervalId = setInterval(async  () => {
      isRepeat = true;
      // 드로우만 다시해서 menu 데이터 갱신이 아노딤
      await callHistory(serverId, isRepeat); // 서버 ID를 사용하여 showHistory 호출
      await callDrawData(serverId, isRepeat, selectedDate)
      updateUsageColors();
    }, timeDelay); // 10초마다 호출
  }
}
