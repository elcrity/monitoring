// (async function() {
//   const response = await fetch('/api/doughnut');
//   const doughnutCharData = await response.json();
//   new Chart(
//      document.getElementById('doughnut'),
//      {
//        type: 'doughnut',
//        data: {
//          labels: doughnutCharData.labels,
//          datasets: [{
//            label: '',
//            data: doughnutCharData.data,
//            backgroundColor: doughnutCharData.backgroundColor,
//            hoverOffset: 4
//          }]
//        }
//      }
//   );
// })();

  (async function() {
  try {
  // 데이터 가져오기
  const response1 = await fetch('/api/doughnut/1');
  const doughnutChartData1 = await response1.json();

  const response2 = await fetch('/api/doughnut/2');
  const doughnutChartData2 = await response2.json();

  const response3 = await fetch('/api/doughnut/3');
  const doughnutChartData3 = await response3.json();

  // 차트 그리기
  new Chart(
  document.getElementById('doughnut1'),
{
  type: 'doughnut',
  data: {
  labels: doughnutChartData1.labels,
  datasets: [{
  label: '',
  data: doughnutChartData1.data,
  backgroundColor: doughnutChartData1.backgroundColor,
  hoverOffset: 4
}]
}
}
  );

  new Chart(
  document.getElementById('doughnut2'),
{
  type: 'doughnut',
  data: {
  labels: doughnutChartData2.labels,
  datasets: [{
  label: '',
  data: doughnutChartData2.data,
  backgroundColor: doughnutChartData2.backgroundColor,
  hoverOffset: 4
}]
}
}
  );

  new Chart(
  document.getElementById('doughnut3'),
{
  type: 'doughnut',
  data: {
  labels: doughnutChartData3.labels,
  datasets: [{
  label: '',
  data: doughnutChartData3.data,
  backgroundColor: doughnutChartData3.backgroundColor,
  hoverOffset: 4
}]
}
}
  );
} catch (error) {
  console.error('Error fetching or parsing data:', error);
}
})();