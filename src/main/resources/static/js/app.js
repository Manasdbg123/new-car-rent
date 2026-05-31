document.addEventListener("DOMContentLoaded", () => {

    /*
    ======================================
    MODALS
    ======================================
    */

    const loginModal =
        document.getElementById("loginModal");

    const registerModal =
        document.getElementById("registerModal");

    /*
    ======================================
    OPEN LOGIN
    ======================================
    */

    document
        .querySelector(".login-btn")
        .addEventListener("click", () => {

            loginModal.style.display = "flex";
        });

    /*
    ======================================
    OPEN REGISTER
    ======================================
    */

    document
        .querySelector(".register-btn")
        .addEventListener("click", () => {

            registerModal.style.display = "flex";
        });

    /*
    ======================================
    CLOSE LOGIN
    ======================================
    */

    document
        .getElementById("closeLogin")
        .addEventListener("click", () => {

            loginModal.style.display = "none";
        });

    /*
    ======================================
    CLOSE REGISTER
    ======================================
    */

    document
        .getElementById("closeRegister")
        .addEventListener("click", () => {

            registerModal.style.display = "none";
        });

    /*
    ======================================
    LOGIN FORM
    ======================================
    */

    document
        .getElementById("loginForm")
        .addEventListener("submit", async (e) => {

            e.preventDefault();

            const email =
                document.getElementById("loginEmail").value;

            const password =
                document.getElementById("loginPassword").value;

            alert(
                "Login request sent for: " + email
            );

            /*
            ======================================
            FUTURE BACKEND API
            ======================================

            fetch('/api/auth/login', {
                method: 'POST'
            })

            */
        });

    /*
    ======================================
    REGISTER FORM
    ======================================
    */

    document
        .getElementById("registerForm")
        .addEventListener("submit", async (e) => {

            e.preventDefault();

            const email =
                document.getElementById("registerEmail").value;

            alert(
                "Registration request sent for: " + email
            );

        });

    /*
    ======================================
    BOOK BUTTONS
    ======================================
    */

    const bookButtons =
        document.querySelectorAll(".book-btn");

    bookButtons.forEach(button => {

        button.addEventListener("click", () => {

            const car =
                button.closest(".car-card")
                    .querySelector("h3")
                    .innerText;

            alert(
                "Booking started for " + car
            );

        });

    });

});