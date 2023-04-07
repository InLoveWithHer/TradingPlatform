function checkEmail() {
    const emailInput = document.getElementById("email");
    const emailValue = emailInput.value.trim();
    if (emailValue.length > 0) {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/checkEmail", true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.isEmailExists) {
                    emailInput.setCustomValidity("Этот email уже зарегистрирован");
                } else {
                    emailInput.setCustomValidity("");
                }
            }
        };
    xhr.send("email=" + emailValue);
    }
}

//Перевірка валідності емайла
//function validateEmail(email) {
//     const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//     return re.test(email);
//}
//
//const form = document.getElementById("registerForm");
//const emailInput = document.getElementById("email");
//
//form.addEventListener("submit", function(event) {
//     if (!validateEmail(emailInput.value)) {
//         alert("Please enter a valid email address");
//         event.preventDefault();
//     }
//});