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
    $('#isAuction').on('change', function() {
        if ($(this).is(':checked')) {
            $('#price').prop('disabled', true);
            $('#auctionDuration').prop('disabled', false);
            $('#auctionStartingBid').prop('disabled', false);
        } else {
            $('#price').prop('disabled', false);
            $('#auctionDuration').prop('disabled', true);
            $('#auctionStartingBid').prop('disabled', true);
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


