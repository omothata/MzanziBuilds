import { request } from "./api.js";

export function signup(data) {
    return request("/auth/signup", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function signin(data) {
    return request("/auth/signin", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function saveProfileSetup(data) {
    return request("/onboarding/profile", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function saveFollowedSkills(data) {
    return request("/onboarding/skills", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function getSuggestedAccounts(userId) {
    return request(`/onboarding/suggestions?userId=${encodeURIComponent(userId)}`);
}

export function completeOnboarding(data) {
    return request("/onboarding/follow-suggestions", {
        method: "POST",
        body: JSON.stringify(data),
    });
}
