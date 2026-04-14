import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";
import { saveFollowedSkills } from "../services/authService.js";

function parseTags(value) {
    return value
        .split(",")
        .map((item) => item.trim())
        .filter(Boolean);
}

export function loadFollowedSkills() {
    loadHtmlIntoApp("../onboarding/followed-skills.html", "Error loading followed skills")
        .then((page) => {
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

            const form = document.getElementById("followedSkillsForm");

            if (!form) {
                return;
            }

            form.addEventListener("submit", async (event) => {
                event.preventDefault();

                try {
                    await saveFollowedSkills({
                        userId: currentUser.userId,
                        followedSkills: parseTags(form.followedSkills.value),
                    });
                    navigate("/onboarding/follow");
                } catch (error) {
                    alert(error.message || "Unable to save your interests");
                }
            });
        });
}
