const CURRENT_USER_KEY = "mzanzibuilds.currentUser";

export function setCurrentUser(user) {
    localStorage.setItem(CURRENT_USER_KEY, JSON.stringify(user));
}

export function mergeCurrentUser(updates) {
    const currentUser = getCurrentUser();

    if (!currentUser) {
        return null;
    }

    const nextUser = {
        ...currentUser,
        ...updates,
    };

    setCurrentUser(nextUser);
    return nextUser;
}

export function getCurrentUser() {
    const savedUser = localStorage.getItem(CURRENT_USER_KEY);

    if (!savedUser) {
        return null;
    }

    try {
        return JSON.parse(savedUser);
    } catch {
        localStorage.removeItem(CURRENT_USER_KEY);
        return null;
    }
}

export function clearCurrentUser() {
    localStorage.removeItem(CURRENT_USER_KEY);
}
