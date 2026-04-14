import { getProject, toggleProjectLike, addProjectComment } from "../services/projectService.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";
import { getProjectUpdates, createProjectUpdate } from "../services/projectService.js";

function getProjectIdFromUrl() {
    const searchParams = new URLSearchParams(window.location.search);
    return searchParams.get("id");
}

function renderComments(comments) {
    if (!comments.length) {
        return "<p>No comments yet.</p>";
    }

    return comments.map((comment) => `
        <article>
            <strong>${comment.authorName}</strong>
            <span>@${comment.authorUsername}</span>
            <p>${comment.content}</p>
        </article>
    `).join("");
}

function renderReadme(readmeContent) {
    if (!readmeContent) {
        return "<p>README could not be loaded or this repository does not expose one publicly.</p>";
    }

    return `<pre>${readmeContent
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")}</pre>`;
}

function renderProject(container, project, updates = [], currentUser) {
    const isOwner = currentUser && currentUser.userId === project.ownerId;
    container.innerHTML = `
        <header>
            <h1>${project.name}</h1>
            <p>by ${project.ownerName} (@${project.ownerUsername})</p>
        </header>
        <section>
            <p>${project.description}</p>
            <p><strong>Stage:</strong> ${project.stage}</p>
            <p><strong>Support required:</strong> ${project.supportRequired || "Not specified"}</p>
            <p><strong>Stack:</strong> ${project.stack.join(", ") || "Not specified"}</p>
            <p><strong>GitHub:</strong> ${project.githubUrl ? `<a href="${project.githubUrl}" target="_blank" rel="noreferrer">Open repository</a>` : "Not provided"}</p>
            <p><strong>Live link:</strong> ${project.liveUrl ? `<a href="${project.liveUrl}" target="_blank" rel="noreferrer">Open project</a>` : "Not provided"}</p>
        </section>
        <section>
            <button id="toggleLikeButton">${project.likedByCurrentUser ? "Unlike" : "Like"} (${project.likeCount})</button>
        </section>
        <section>
            <h2>Updates</h2>

            ${isOwner ? `
                <form id="updateForm">
                    <input type="text" id="updateTitle" placeholder="Update title">
                    <textarea id="updateContent" placeholder="What did you work on?"></textarea>
                    <input type="number" id="updateProgress" placeholder="Progress %">
                    <button type="submit">Post Update</button>
                </form>
            ` : ""}

            <div id="projectUpdates">
                ${renderUpdates(updates)}
            </div>
        </section>
        <section>
            <h2>README</h2>
            ${renderReadme(project.readmeContent)}
        </section>
        <section>
            <h2>Comments</h2>
            <div id="projectComments">${renderComments(project.comments)}</div>
            <form id="projectCommentForm">
                <textarea id="commentContent" name="content" rows="4" placeholder="Add your thoughts"></textarea>
                <button type="submit">Post comment</button>
            </form>
        </section>
    `;
}

function bindProjectActions(container, projectId, user) {
    document.getElementById("toggleLikeButton")?.addEventListener("click", async () => {
        if (!user) {
            alert("Sign in to like this project");
            return;
        }

        const result = await toggleProjectLike(projectId, user.userId);
        const refreshed = await getProject(projectId, user.userId);
        renderProject(container, {
            ...refreshed,
            likeCount: result.likeCount,
            likedByCurrentUser: result.likedByCurrentUser,
        });
        bindProjectActions(container, projectId, user);
    });

    document.getElementById("projectCommentForm")?.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!user) {
            alert("Sign in to comment on this project");
            return;
        }

        const textarea = document.getElementById("commentContent");

        await addProjectComment(projectId, {
            userId: user.userId,
            content: textarea.value.trim(),
        });

        const refreshed = await getProject(projectId, user.userId);
        renderProject(container, refreshed);
        bindProjectActions(container, projectId, user);
    });
}

export function loadProjectDetails() {
    loadHtmlIntoApp("../projects/project-details.html", "Error loading project details")
        .then(async (page) => {
            if (!page) {
                return;
            }

            const user = getCurrentUser();
            const projectId = getProjectIdFromUrl();
            const container = document.getElementById("projectDetailsPage");

            if (!projectId || !container) {
                if (container) {
                    container.innerHTML = "<p>Project not found.</p>";
                }
                return;
            }

            try {
                const project = await getProject(projectId, user?.userId);
                const updates = await getProjectUpdates(projectId);
                renderProject(container, project, updates, user);
                bindProjectActions(container, projectId, user);
                bindUpdateActions(container, projectId, user);
            } catch (error) {
                container.innerHTML = `<p>${error.message || "Unable to load project details."}</p>`;
            }
        });
}

function renderUpdates(updates) {
    if (!updates.length) {
        return "<p>No updates yet.</p>";
    }

    return updates.map(update => `
        <article class="update-card">
            <h3>${update.title || "Update"}</h3>
            <p>${update.content}</p>
            <small>${new Date(update.createdAt).toLocaleString()}</small>
            ${
                update.progressPercent !== null && update.progressPercent !== undefined
                    ? `<div>Progress: ${update.progressPercent}%</div>`
                    : ""
            }
        </article>
    `).join("");
}

function bindUpdateActions(container, projectId, user) {
    const form = document.getElementById("updateForm");

    if (!form) return; // not owner

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const body = {
            title: document.getElementById("updateTitle").value,
            content: document.getElementById("updateContent").value,
            progressPercent: parseInt(document.getElementById("updateProgress").value)
        };

        await createProjectUpdate(projectId, body);

        const updatedProject = await getProject(projectId, user?.userId);
        const updates = await getProjectUpdates(projectId);

        renderProject(container, updatedProject, updates, user);
        bindProjectActions(container, projectId, user);
        bindUpdateActions(container, projectId, user);
    });
}