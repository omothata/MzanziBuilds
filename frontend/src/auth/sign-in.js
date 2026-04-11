import { loadHtmlIntoApp } from "../utils/load-html.js";

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


                });
            }
        });
}
