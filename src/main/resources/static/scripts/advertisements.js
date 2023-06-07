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

  function resetFilters() {
    document.getElementById("type").selectedIndex = 0;
    document.getElementById("status").selectedIndex = 0;
    document.getElementById("category").selectedIndex = 0;
    document.getElementById("subcategory").selectedIndex = 0;
    document.getElementById("auction").selectedIndex = 0;
    document.getElementById("priceMin").value = "";
    document.getElementById("priceMax").value = "";
  }

});


