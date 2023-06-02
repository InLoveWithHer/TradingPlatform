var advertisementId;
var advertisementDeleteId;

function makeActive(advertisementId) {
  var url = "/advertisement/" + advertisementId + "/activate";

  $.ajax({
    type: "POST",
    url: url,
    success: function (data) {
      // Обновить страницу или выполнить другие действия при успешном изменении статуса
      location.reload();
    },
    error: function (xhr, status, error) {
      // Обработать ошибку при изменении статуса
      console.log(error);
    }
  });
}

function deleteAdvertisement(advertisementDeleteId) {
  var url = "/advertisement/" + advertisementDeleteId + "/delete";

  // Запомнить id удаляемого объявления в кнопке подтверждения удаления
  $('#confirmDeleteButton').attr('data-advertisement-id', advertisementDeleteId);

  // Показать модальное окно подтверждения удаления
  $('#deleteConfirmationModal').modal('show');
}

$(document).on('click', '#confirmDeleteButton', function() {
  // Получить id удаляемого объявления из кнопки подтверждения удаления
  advertisementDeleteId = $(this).attr('data-advertisement-id');
  console.log("id " + advertisementDeleteId);

  // Удалить объявление
  var url = "/advertisement/" + advertisementDeleteId + "/delete";
  $.ajax({
    type: "DELETE",
    url: url,
    success: function (data) {
      // Обновить страницу или выполнить другие действия при успешном удалении
      location.reload();
    },
    error: function (xhr, status, error) {
      // Обработать ошибку при удалении
      console.log(error);
    }
  });
});

$(document).ready(function() {
  $('.change-price-btn').each(function() {
    $(this).click(function() {
      advertisementId = $(this).attr('id');
    });
  });

  $('.delete-btn').each(function() {
    $(this).click(function() {
      advertisementDeleteId = $(this).attr('id'); // Получаем id конкретной кнопки удаления, по которой был произведен клик
      console.log("id " + advertisementDeleteId);

      // Удалить объявление
      deleteAdvertisement(advertisementDeleteId);
    });
  });

  $('form').on('submit', function(event) {
    event.preventDefault();

    var newPrice = $(this).find('#newPrice').val();
    var data = new FormData();
    data.append('advertisementId', advertisementId);
    data.append('newPrice', newPrice);

    $.ajax({
      url: '/advertisement/change-price',
      method: 'POST',
      data: data,
      processData: false,
      contentType: false,
      success: function(response) {
        // Обработка успешного ответа от сервера
        location.reload();
      },
      error: function(xhr, status, error) {
        // Обработка ошибки
        console.log(error);
      }
    });
  });
});
