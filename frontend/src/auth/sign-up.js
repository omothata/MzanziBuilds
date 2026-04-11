import { loadHtmlIntoApp } from "../utils/load-html.js";

export function loadSignup() {
    loadHtmlIntoApp("../auth/sign-up.html", "Error loading signup page")
        .then((page) => {
            if (!page) {
                return;
            }

            const form = document.getElementById("signupForm");

            // Form logic
            if (form) {
                form.addEventListener("submit", async(e) => {
                    e.preventDefault();
                    const email = form.email.value;
                    const password = form.password.value;


                });
            }
        });
}
