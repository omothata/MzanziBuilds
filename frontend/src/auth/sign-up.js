import { loadHtmlIntoApp } from "../utils/load-html.js";
import { navigate } from "../router/router.js";
import { signup } from "../services/authService.js";
import { setCurrentUser } from "../utils/storage.js";

function showSignupError(error) {
    alert(error.message || "Sign-up failed");
}

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
                        const user = await signup({ name, username, email, password });
                        setCurrentUser(user);
                        alert("Account created successfully");
                        navigate("/onboarding/profile");
                    } catch(err) {
                        showSignupError(err);
                    }

                });
            }
        });
}
