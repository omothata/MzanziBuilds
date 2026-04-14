import { navigate } from "../router/router.js";
import { getFeedUpdates } from "../services/projectService.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";

function renderFeed(container, updates) {
    if (!container) {
        return;
    }

    if (!updates.length) {
        container.innerHTML = "<p>No activity yet. Follow builders or post updates.</p>";
        return;
    }

     container.innerHTML = updates.map((update) => `
        <article class="feed-card">
            <h3>${update.title || "Project Update"}</h3>

            <p>${update.content}</p>

            <p>
                <strong>${update.username}</strong> updated 
                <a href="/projects/view?id=${update.projectId}" data-link>
                    ${update.projectName}
                </a>
            </p>

            <p class="meta">
                ${new Date(update.createdAt).toLocaleString()}
            </p>

            ${
                update.progressPercent !== null && update.progressPercent !== undefined
                    ? `<div class="progress">
                           Progress: ${update.progressPercent}%
                       </div>`
                    : ""
            }
        </article>
    `).join("");

}

export function loadMainFeed() {
    loadHtmlIntoApp("../feed/main-feed.html", "Error loading main feed")
        .then(async (page) => {
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
            const feed = document.getElementById("projectFeed");

            if (welcome) {
                welcome.textContent = `Welcome back, ${user.name || user.username}.`;
            }

            try {
                const updates = await getFeedUpdates();
    renderFeed(feed, updates);
            } catch (error) {
                if (feed) {
                    feed.innerHTML = `<p>${error.message || "Unable to load feed."}</p>`;
                }
            }
        });
}
