import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";

export function loadHome() {
    loadHtmlIntoApp("../home/home.html", "Error loading home page")
        .then((page) => {
            if (!page) {
                return;
            }

            // Home screen navigation
            document.getElementById("sign-in")?.addEventListener("click", (e) => {
                e.preventDefault();
                navigate("/sign-in");
            });

            document.getElementById("sign-up")?.addEventListener("click", (e) => {
                e.preventDefault();
                navigate("/sign-up");
            });
        });
}
