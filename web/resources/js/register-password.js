document.addEventListener("DOMContentLoaded", function () {
    var password = document.getElementById("password");
    var confirmPassword = document.getElementById("passwordv");

    if (!password || !confirmPassword) {
        return;
    }

    var requirements = {
        reqLength: function (value) { return value.length >= 8; },
        reqUpper: function (value) { return /[A-Z]/.test(value); },
        reqLower: function (value) { return /[a-z]/.test(value); },
        reqNumber: function (value) { return /[0-9]/.test(value); },
        reqSpecial: function (value) { return /[^A-Za-z0-9]/.test(value); }
    };

    function setState(element, valid, pending) {
        // Replace the previous feedback state before applying the current one.
        element.classList.remove("feedback-pending", "feedback-valid", "feedback-invalid");
        element.classList.add(pending ? "feedback-pending" : valid ? "feedback-valid" : "feedback-invalid");
    }

    function updatePasswordFeedback() {
        var value = password.value;
        var confirmValue = confirmPassword.value;

        // Requirement element ids match the keys in the requirements map.
        Object.keys(requirements).forEach(function (id) {
            var element = document.getElementById(id);
            setState(element, requirements[id](value), value.length === 0);
        });

        var match = document.getElementById("passwordMatch");
        if (value.length === 0 && confirmValue.length === 0) {
            match.textContent = "Passwords have not been entered yet.";
            setState(match, false, true);
        } else if (value === confirmValue) {
            match.textContent = "Passwords match.";
            setState(match, true, false);
        } else {
            match.textContent = "Passwords do not match.";
            setState(match, false, false);
        }
    }

    password.addEventListener("input", updatePasswordFeedback);
    confirmPassword.addEventListener("input", updatePasswordFeedback);
    updatePasswordFeedback();
});
