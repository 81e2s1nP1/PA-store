const prevButton = document.querySelector('.prev-button');
const nextButton = document.querySelector('.next-button');

prevButton.addEventListener('click', function() {
  document.querySelector('#carouselExampleIndicators').carousel('prev');
});

nextButton.addEventListener('click', function() {
  document.querySelector('#carouselExampleIndicators').carousel('next');
});
