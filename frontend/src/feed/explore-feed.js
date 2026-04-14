import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";

export function loadExploreFeed() {
    loadHtmlIntoApp("../feed/explore-feed.html", "Error loading explore feed")
        .then((page) => {
            if (!page) {
                return;
            }

            if (!getCurrentUser()) {
                navigate("/sign-in");
                return;
            }

            if (!getCurrentUser().onboardingCompleted) {
                navigate("/onboarding/profile");
            }
        });
}
