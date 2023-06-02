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

