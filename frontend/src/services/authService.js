import { request } from "./api.js";

export function signup(data) {
    return request("/auth/signup", {
        method: "POST",
        body: JSON.stringify(data),
    });
}