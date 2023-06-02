$(document).ready(function() {
  const passwordInput1 = document.getElementById('password');
  const togglePasswordButton1 = document.getElementById('togglePassword1');
  const passwordInput2 = document.getElementById('repeatPassword');
  const togglePasswordButton2 = document.getElementById('togglePassword2');


  togglePasswordButton1.addEventListener('click', function () {
    if (passwordInput1.type === 'password') {
      passwordInput1.type = 'text';
      togglePasswordButton1.textContent = 'Сховати';
    } else {
      passwordInput1.type = 'password';
      togglePasswordButton1.textContent = 'Показати';
    }
  });

  togglePasswordButton2.addEventListener('click', function () {
    if (passwordInput2.type === 'password') {
      passwordInput2.type = 'text';
      togglePasswordButton2.textContent = 'Сховати';
    } else {
      passwordInput2.type = 'password';
      togglePasswordButton2.textContent = 'Показати';
    }
  });

  function validateForm() {
    var password = document.getElementById("password").value;
    var repeatPassword = document.getElementById("repeatPassword").value;
    if (password !== repeatPassword) {
      alert("Пароли не совпадают");
      return false; // Отменить отправку формы
    }

    return true; // Продолжить отправку формы
  }


  const form = document.getElementById("form1");
    form.addEventListener("submit", function(event) {
      if (!validateForm()) {
        event.preventDefault(); // Отменить отправку формы
      }
    });


});
