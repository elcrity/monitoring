(async function () {
  const response = await fetch('/api/acquisitions');
  const data = await response.json();
  const labels = data.map(item => item.year);
  const counts = data.map(item => item.count);
  console.log("labels : ", labels);
  console.log("counts : ", counts);
  new Chart(
     document.getElementById('acquisitions'),
     {
       type: 'line',
       data: {
         labels: labels,
         datasets: [{
           label: 'Acquisitions by year',
           data: counts,
           fill: false,
           pointRadius: 0, // 데이터 지점의 점을 제거
           pointHoverRadius: 0, // 데이터 지점에 마우스를 올릴 때 점을 제거
           tension : 0.2
         }]
       },
       options: {
         scales: {
           x: {
             display: true,
             title: {
               display: true,
               text: 'Date'
             },
             ticks: {
               display: true,
               callback: function (value, index, values) {
                 const label = labels[index];
                 const month = label.split('.')[1];
                 // 01, 06, 12 월에만 눈금 표시
                 if (index === 0 || index === labels.length -1 || month === "06") {
                   return label;
                 }
                 // 그 외에는 빈 문자열 반환 (눈금 표시하지 않음)
                 return '';
               }
             },
             grid: {
               display: true,
               drawBorder: false, // 차트 테두리에 그리드 선을 그리지 않음
               drawOnChartArea: true, // 차트 영역 안에 그리드 선을 그림
               lineWidth: 1,
               color: function (context) {
                 const index = context.tick.value;
                 const label = labels[index];
                 const month = label.split('.')[1];
                 // 01, 06, 12 월에만 그리드 선 표시
                 return (index === 0 || index === labels.length -1 || month === "06")
                    ? 'rgba(0, 0, 0, 1)' : 'rgba(0, 0, 0, 0)';
               }
             }
           },
           y: {
             display: true,
             title: {
               display: true,
               text: 'Value'
             }
           }
         }
       }
     }
  );
})();