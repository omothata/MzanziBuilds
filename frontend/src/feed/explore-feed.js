import { navigate } from "../router/router.js";
import { getExploreProjects } from "../services/projectService.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";

function renderProjects(container, projects) {
    if (!container) {
        return;
    }

    if (!projects.length) {
        container.innerHTML = "<p>No explore projects matched your followed skills yet.</p>";
        return;
    }

    container.innerHTML = projects.map((project) => `
        <article>
            <h2><a href="/projects/view?id=${project.projectId}" data-link>${project.name}</a></h2>
            <p>${project.description}</p>
            <p><strong>Stack:</strong> ${project.stack.join(", ") || "Not specified"}</p>
            <p>by ${project.ownerName} (@${project.ownerUsername})</p>
        </article>
    `).join("");
}

export function loadExploreFeed() {
    loadHtmlIntoApp("../feed/explore-feed.html", "Error loading explore feed")
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

            const feed = document.getElementById("exploreProjectFeed");

            try {
                const projects = await getExploreProjects(user.userId);
                renderProjects(feed, projects);
            } catch (error) {
                if (feed) {
                    feed.innerHTML = `<p>${error.message || "Unable to load explore projects."}</p>`;
                }
            }
        });
}
