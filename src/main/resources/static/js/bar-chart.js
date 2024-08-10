(async function() {
  const response = await fetch('/api/bar');
  const barCharData = await response.json();
  new Chart(
     document.getElementById('bar'),
     {
       type: 'bar',
       data: {
         labels: barCharData.labels,
         datasets: [{
           label: 'My First Dataset',
           data: barCharData.data,
           backgroundColor: barCharData.backgroundColor,
           borderColor: barCharData.borderColor,
           borderWidth: 1
         }]
       },
       options: {
         scales: {
           y: {
             beginAtZero: true
           }
         }
       },
     }
  );
})();