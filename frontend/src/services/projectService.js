import { request } from "./api.js";

export function createProject(data) {
    return request("/projects", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function getMainFeedProjects(userId) {
    return request(`/projects/feed?userId=${encodeURIComponent(userId)}`);
}

export function getExploreProjects(userId) {
    return request(`/projects/explore?userId=${encodeURIComponent(userId)}`);
}

export function getProject(projectId, userId) {
    const suffix = userId ? `?userId=${encodeURIComponent(userId)}` : "";
    return request(`/projects/${encodeURIComponent(projectId)}${suffix}`);
}

export function addProjectComment(projectId, data) {
    return request(`/projects/${encodeURIComponent(projectId)}/comments`, {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function toggleProjectLike(projectId, userId) {
    return request(`/projects/${encodeURIComponent(projectId)}/likes`, {
        method: "POST",
        body: JSON.stringify({ userId }),
    });
}

export function getFeedUpdates() {
    return request("/feed");
}

export function getProjectUpdates(projectId) {
    return request(`/projects/${encodeURIComponent(projectId)}/updates`);
}

export function createProjectUpdate(projectId, body) {
    return request(`/projects/${encodeURIComponent(projectId)}/updates`, {
        method: "POST",
        body: JSON.stringify(body),
    });
}

export async function getCelebrationWall() {
    const res = await fetch("/api/celebration-wall");

    if (!res.ok) {
        throw new Error("Failed to load celebration wall");
    }

    return res.json();
}