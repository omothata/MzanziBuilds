import { loadHtmlIntoApp } from "../utils/load-html.js";
import { navigate } from "../router/router.js";
import { signup } from "../services/authService.js";

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
                    const name = form.name.value.trim();
                    const email = form.email.value.trim();
                    const password = form.password.value;
                    const username = form.username.value.trim();

                    try {
                        await signup({ name, username, email, password });
                        alert("Account created successfully");

                        navigate("/sign-in");
                    } catch(err) {
                        alert(err.message || "Sign-up failed");
                    }

                });
            }
        });
}
