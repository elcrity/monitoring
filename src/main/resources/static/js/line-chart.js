(async function() {
    const response = await fetch('/api/acquisitions');
    const data = await response.json();

    new Chart(
     document.getElementById('acquisitions'),
     {
       type: 'line',
       data: {
         labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct'],
         datasets: [{
             label: 'Acquisitions by year',
             data: [10, 20, 15, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105],
             fill: false,
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
               callback: function(value, index, values) {
                 // 4개마다 눈금을 표시하도록 설정
                 if (index % 4 === 0) {
                   return value;
                 } else {
                   return '';
                 }
               }
             },
             grid: {
               display: true,
               drawBorder: false, // 차트 테두리에 그리드 선을 그리지 않음
               drawOnChartArea: true, // 차트 영역 안에 그리드 선을 그림
               lineWidth: 1,
               color: function(context) {
                 return context.tick.value % 4 === 0 ? 'rgba(0, 0, 0, 1)' : 'rgba(0, 0, 0, 0)';
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