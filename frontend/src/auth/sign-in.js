import { loadHtmlIntoApp } from "../utils/load-html.js";
import { navigate } from "../router/router.js";
import { signin } from "../services/authService.js";
import { setCurrentUser } from "../utils/storage.js";

export function loadSignin() {
    loadHtmlIntoApp("../auth/sign-in.html", "Error loading signin page")
        .then((page) => {
            if (!page) {
                return;
            }

            const form = document.getElementById("signInForm");

            // Form logic
            if (form) {
                form.addEventListener("submit", async(e) => {
                    e.preventDefault();
                    const identifier = form.identifier.value.trim();
                    const password = form.password.value;

                    try {
                        const user = await signin({ identifier, password });
                        setCurrentUser(user);
                        navigate(user.onboardingCompleted ? "/feed" : "/onboarding/profile");
                    } catch (error) {
                        alert(error.message || "Sign-in failed");
                    }

                });
            }
        });
}
