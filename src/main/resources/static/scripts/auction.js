$(document).ready(function() {
  var advertisementId = window.location.pathname.split('/').pop();
  var auctionID;
  var maxUserBid = 0;
  var isAuthenticated = false; // Инициализация значения аутентификации при загрузке страницы
  var maxBid = 0; // Переменная для хранения максимальной ставки
  var currentUserID;
  var userId;

  userId = $('#userId').text();
  $('#userId').remove()

  isUserLoggedIn().then(function(result) {
    isAuthenticated = Boolean(result.authenticated);
    if(isAuthenticated) {
        $.ajax({
          url: '/api/user/currentUserId',
          type: 'GET',
          success: function(userID) {
              currentUserID = userID;

              if (userId == currentUserID) {
                // Если пользователь является автором объявления, блокируем поле ввода и кнопку
                $('#bid-amount').prop('disabled', true);
                $('#submit-bid').prop('disabled', true);
                $('#submit-bid').addClass('disabled');
              }
          },
          error: function(jqXHR, textStatus, errorThrown) {
            // Обработка ошибки
          }
        });
      }
  }).catch(function(error) {
    console.log(error);
  });

  $.ajax({
    url: '/advertisement/' + advertisementId + '/auction',
    type: 'GET',
    success: function(auction) {
      // Проверка наличия аукциона
      if (auction != null) {
        loadBids();
        auctionID = auction.id
      }
    },
    error: function(jqXHR, textStatus, errorThrown) {
      // Обработка ошибки
    }
  });

  $('#bid-form').submit(function(event) {
    event.preventDefault();

    if (!isAuthenticated) {
      // Если пользователь не авторизован, выводим сообщение и прерываем отправку запроса
      showAlert('Для того щоб зробити ставку, авторізуйтесь.');
      return;
    }

    var advertisementId = window.location.pathname.split('/').pop();
    var formData = new FormData();
    var price = $('#bid-amount').val();
    formData.append('price', price);

    var startingBidText = $('#starting-bid').text().trim();
    var startingBid = parseFloat(startingBidText.replace(',', '.'));

    if(maxUserBid == maxBid) {
      showAlert('Ваша ставка перемагає на аукціоні, тому ви не можете створити нову.');
      return;
    } else {
      if(maxBid >= startingBid) {
        if (parseFloat(price) <= maxBid) {
          showAlert('Мінімальна сума ставки: ' + (maxBid + 1) + ' грн');
          return;
        }
      } else {
        if (parseFloat(price) < startingBid + 1) {
          showAlert('Мінімальна сума ставки: ' + (startingBid + 1) + ' грн');
          return;
        }
      }
    }

    $.ajax({
      url: '/advertisement/' + advertisementId + '/auction/createBids',
      type: 'POST',
      data: formData,
      contentType: false,
      processData: false,
      success: function(data) {
        loadBids();
      },
      error: function(jqXHR, textStatus, errorThrown) {
        // Обработка ошибки
      }
    });
  });

  async function loadBids() {
    var advertisementId = window.location.pathname.split('/').pop();
    var bids = await $.ajax({
      url: '/advertisement/' + advertisementId + '/auction/bids',
      type: 'GET',
    });

    if(isAuthenticated) {
      maxUserBid = await getMaxBidForUserInAuction(currentUserID, auctionID);
    }

    $('#bids-list').empty();

    for (var i = 0; i < bids.length; i++) {
      var bid = bids[i];
      var userName = await getUserNameByBidId(bid.id);
      var createdAt = formatDate(bid.createdAt);
      var listItem = $('<li>').text(userName + ' - ' + createdAt + ' - ' + bid.bid + ' грн');
      $('#bids-list').append(listItem); // Используем метод append() для добавления элемента в конец списка

      // Обновляем максимальную ставку, если текущая ставка больше
      if (bid.bid > maxBid) {
        maxBid = bid.bid;
      }
    }

    // Отобразить только последние четыре ставки
    var displayedBids = 0; // Счетчик отображенных ставок

    $('#bids-list li').each(function() {
      if (displayedBids < bids.length - 4) {
        $(this).hide(); // Скрыть ставку, если отобразили уже более четырех ставок
      }
      displayedBids++;
    });
  }

  function formatDate(dateString) {
    var months = [
      'січня',
      'лютого',
      'березня',
      'квітня',
      'травня',
      'червня',
      'липня',
      'серпня',
      'вересня',
      'жовтня',
      'листопада',
      'грудня'
    ];

    var date = new Date(dateString);
    var currentDate = new Date(); // Текущая дата
    var day = date.getDate();
    var monthIndex = date.getMonth();
    var year = date.getFullYear();
    var monthName = months[monthIndex];

    // Если дата совпадает с текущей датой (сегодня), добавляем время
    if (
      date.getDate() === currentDate.getDate() &&
      date.getMonth() === currentDate.getMonth() &&
      date.getFullYear() === currentDate.getFullYear()
    ) {
      var hours = date.getHours();
      var minutes = date.getMinutes();
      var formattedTime = hours + ':' + (minutes < 10 ? '0' + minutes : minutes);

      return 'Сьогодні о ' + formattedTime;
    }

    // Если год совпадает с текущим годом, не выводим год
    if (year === currentDate.getFullYear()) {
      return day + ' ' + monthName;
    }

    return day + ' ' + monthName + ' ' + year;
  }

  function getUserNameByBidId(bidId) {
    return new Promise(function(resolve, reject) {
      $.ajax({
        url: '/bid/' + bidId + '/user/name',
        type: 'GET',
        success: function(data) {
          resolve(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
          reject(errorThrown);
        }
      });
    });
  }

  function isUserLoggedIn() {
    return new Promise(function(resolve, reject) {
      $.ajax({
        url: '/api/user/authenticated',
        type: 'GET',
        success: function(data) {
          resolve({ authenticated: data }); // Возвращаем объект с свойством 'authenticated'
        },
        error: function(jqXHR, textStatus, errorThrown) {
          reject(errorThrown);
        }
      });
    });
  }

  async function getMaxBidForUserInAuction(userId, auctionId) {
    try {
      var response = await $.ajax({
        url: '/user/' + userId + '/auction/' + auctionId + '/maxBid',
        type: 'GET'
      });
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  }

  function showAlert(message) {
    $('#alert-message').text(message);
    $('#custom-alert').modal('show');
  }
});