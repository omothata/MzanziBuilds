import { getCelebrationWall } from "../services/projectService.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";

function renderWall(container, users) {
    if (!users.length) {
        container.innerHTML = "<p>No completed projects yet.</p>";
        return;
    }

    container.innerHTML = users.map(user => `
        <div class="user-card" data-user-id="${user.userId}">
            <img src="${user.profileImageUrl || '/default-avatar.png'}" alt="profile">
            <h3>${user.name || user.username}</h3>
            <p>@${user.username}</p>
            <p>✅ ${user.completedProjectsCount} completed projects</p>
        </div>
    `).join("");
}

function bindNavigation() {
    document.querySelectorAll(".user-card").forEach(card => {
        card.addEventListener("click", () => {
            const userId = card.dataset.userId;
            window.location.href = `/profile/view?id=${userId}`;
        });
    });
}

export function loadCelebrationWall() {
    loadHtmlIntoApp("../celebration/celebration-wall.html", "Error loading wall")
        .then(async () => {
            const container = document.getElementById("celebrationWall");

            try {
                const users = await getCelebrationWall();
                renderWall(container, users);
                bindNavigation();
            } catch (err) {
                container.innerHTML = `<p>${err.message}</p>`;
            }
        });
}