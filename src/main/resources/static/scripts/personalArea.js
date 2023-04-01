$(document).on('click', '#saveUser', function() {
  var name = $('#name').val();
  var phone = $('#phone').val();
  $.ajax({
    type: 'POST',
    url: '/save-user',
    data: {name: name, phone: phone},
    success: function(data) {
      $('#modalNamePhone').modal('hide');
      alert('Дані успішно збережено');
    },
    error: function() {
      alert('Помилка збереження даних');
    }
  });
});

//$('.modal').on('click', function(event) {
//    if ($(event.target).hasClass('modal')) {
//        event.stopPropagation();
//    }
//});

