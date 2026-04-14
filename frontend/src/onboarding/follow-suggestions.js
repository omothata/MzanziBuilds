import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser, setCurrentUser } from "../utils/storage.js";
import { completeOnboarding, getSuggestedAccounts } from "../services/authService.js";

function renderSuggestions(container, suggestions) {
    if (!container) {
        return;
    }

    if (!suggestions.length) {
        container.innerHTML = "<p>No suggestions yet. You can finish onboarding now and follow people later.</p>";
        return;
    }

    container.innerHTML = suggestions.map((account) => `
        <label>
            <input type="checkbox" name="accountIds" value="${account.userId}">
            <strong>${account.name}</strong>
            <span>@${account.username}</span>
            <p>${account.bio || "New builder to discover"}</p>
            <small>${account.location || "Location not added yet"}</small>
        </label>
    `).join("");
}

async function finishOnboarding(currentUser, accountIds) {
    const onboarding = await completeOnboarding({
        userId: currentUser.userId,
        accountIds,
    });

    setCurrentUser({
        ...currentUser,
        onboardingCompleted: onboarding.onboardingCompleted,
        followingIds: onboarding.followingIds,
    });

    navigate("/feed");
}

export function loadFollowSuggestions() {
    loadHtmlIntoApp("../onboarding/follow-suggestions.html", "Error loading follow suggestions")
        .then(async (page) => {
            if (!page) {
                return;
            }

            const currentUser = getCurrentUser();

            if (!currentUser) {
                navigate("/sign-in");
                return;
            }

            if (currentUser.onboardingCompleted) {
                navigate("/feed");
                return;
            }

            const suggestionsContainer = document.getElementById("suggestedAccounts");
            const form = document.getElementById("followSuggestionsForm");
            const skipButton = document.getElementById("skipSuggestions");

            try {
                const suggestions = await getSuggestedAccounts(currentUser.userId);
                renderSuggestions(suggestionsContainer, suggestions);
            } catch (error) {
                if (suggestionsContainer) {
                    suggestionsContainer.innerHTML = "<p>Could not load suggestions right now.</p>";
                }
            }

            form?.addEventListener("submit", async (event) => {
                event.preventDefault();

                const selectedAccountIds = Array.from(
                    document.querySelectorAll('input[name="accountIds"]:checked')
                ).map((input) => Number(input.value));

                try {
                    await finishOnboarding(currentUser, selectedAccountIds);
                } catch (error) {
                    alert(error.message || "Unable to finish onboarding");
                }
            });

            skipButton?.addEventListener("click", async () => {
                try {
                    await finishOnboarding(currentUser, []);
                } catch (error) {
                    alert(error.message || "Unable to finish onboarding");
                }
            });
        });
}
