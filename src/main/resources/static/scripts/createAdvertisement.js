$(document).ready(function() {
    $('#price').on('input', function() {
        if ($(this).val() < 0) {
            $(this).val(0);
        }
    });
    $('#auctionStartingBid').on('input', function() {
        if ($(this).val() < 0) {
            $(this).val(0);
        }
    });
});

$(document).ready(function() {
    $('#price').on('input', function() {
        var inputValue = $(this).val();
        if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(inputValue)) {
            $(this).val(inputValue.replace(/^0+/, '').replace(/^\./, ''));
        }
    });
    $('#auctionStartingBid').on('input', function() {
        var inputValue = $(this).val();
        if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(inputValue)) {
            $(this).val(inputValue.replace(/^0+/, '').replace(/^\./, ''));
        }
    });
});

$(document).ready(function() {
    $('#isAuction').on('change', function() {
        if ($(this).is(':checked')) {
            $('#price').val('').prop('disabled', true);
            $('#auctionDuration').prop('disabled', false);
            $('#auctionStartingBid').prop('disabled', false);
        } else {
            $('#price').prop('disabled', false);
            $('#auctionDuration').prop('disabled', true);
            $('#auctionStartingBid').val('').prop('disabled', true);
        }
    });
});

$(document).ready(function() {
    $('#category').change(function() {
        var selectedCategory = $(this).val();
        if (selectedCategory) {
            $.ajax({
                type: 'GET',
                url: '/subcategory/' + selectedCategory,
                success: function(subcategories) {
                    var subcategorySelect = $('#subcategory');
                    subcategorySelect.empty();
                    subcategorySelect.append('<option value="">-- Select Subcategory --</option>');
                    $.each(subcategories, function(index, subcategory) {
                        subcategorySelect.append('<option value="' + subcategory + '">' + subcategory + '</option>');
                    });
                    subcategorySelect.prop('disabled', false);
                },
                error: function() {
                    console.error('Error fetching subcategories');
                }
            });
        } else {
            $('#subcategory').prop('disabled', true);
        }
    });
});

function getName(str) {
  if (str.lastIndexOf('\\')) {
    var i = str.lastIndexOf('\\') + 1;
  } else {
    var i = str.lastIndexOf('/') + 1;
  }
  var filename = str.slice(i);
  var maxLength = 13; // Максимальная длина имени файла
  if (filename.length > maxLength) {
    filename = filename.slice(0, maxLength) + '...'; // Обрезаем имя файла и добавляем многоточие в конце
  }
  var uploaded = document.getElementById("fileFormLabel");
  uploaded.innerHTML = filename;
}

function capitalizeFirstLetter(input) {
  var value = input.value;
  if (value.length > 0) {
    var firstChar = value.charAt(0);
    if (firstChar.toLowerCase() === firstChar) {
      input.value = firstChar.toUpperCase() + value.slice(1);
    }
  }
}





