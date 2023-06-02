$(document).ready(function() {
  window.addEventListener('load', function() {
    var titleElement = document.querySelector('.card-text');
    if (titleElement) {
      var titleText = titleElement.innerText;
      if (titleText.length > 40) {
        titleElement.innerText = titleText.substring(0, 40) + '...';
      }
    }
  });

//  // Функция для получения максимальной ставки для каждого объявления
//  function getMaxBid(advertisementId) {
//    console.log("Fetching max bid for advertisement ID: " + advertisementId);
//    $.get('/advertisement/' + advertisementId + '/max-bid', function(data) {
//      console.log("Max bid for advertisement ID " + advertisementId + ": " + data);
//      // Обновляем значение максимальной ставки на странице
//      $('#maxBid_' + advertisementId).text(data);
//    });
//  }
//
//  // Вызываем функцию getMaxBid только для объявлений с аукционом
//  $('.advertisement').each(function() {
//    var advertisementId = $(this).data('advertisement-id');
//    var hasAuction = $(this).data('has-auction');
//    console.log("advertisementId: " + advertisementId);
//    if (hasAuction) {
//      getMaxBid(advertisementId);
//    }
//  });
});


