import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";

export function loadMainFeed() {
    loadHtmlIntoApp("../feed/main-feed.html", "Error loading main feed")
        .then((page) => {
            if (!page) {
                return;
            }

            const user = getCurrentUser();

            if (!user) {
                navigate("/sign-in");
                return;
            }

            if (!user.onboardingCompleted) {
                navigate("/onboarding/profile");
                return;
            }

            const welcome = document.getElementById("feedWelcome");

            if (welcome) {
                welcome.textContent = `Welcome back, ${user.name || user.username}.`;
            }
        });
}
