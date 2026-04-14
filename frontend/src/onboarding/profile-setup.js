import { navigate } from "../router/router.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser, setCurrentUser } from "../utils/storage.js";
import { saveProfileSetup } from "../services/authService.js";

function parseTags(value) {
    return value
        .split(",")
        .map((item) => item.trim())
        .filter(Boolean);
}

export function loadProfileSetup() {
    loadHtmlIntoApp("../onboarding/profile-setup.html", "Error loading profile setup")
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

            const form = document.getElementById("profileSetupForm");

            if (!form) {
                return;
            }

            form.addEventListener("submit", async (event) => {
                event.preventDefault();

                try {
                    await saveProfileSetup({
                        userId: currentUser.userId,
                        bio: form.bio.value.trim(),
                        location: form.location.value.trim(),
                        profilePictureUrl: form.profilePictureUrl.value.trim(),
                        websiteUrl: form.websiteUrl.value.trim(),
                        githubUrl: form.githubUrl.value.trim(),
                        linkedinUrl: form.linkedinUrl.value.trim(),
                        xUrl: form.xUrl.value.trim(),
                        skills: parseTags(form.skills.value),
                    });

                    setCurrentUser({
                        ...currentUser,
                        location: form.location.value.trim(),
                    });
                    navigate("/onboarding/interests");
                } catch (error) {
                    alert(error.message || "Unable to save your profile");
                }
            });
        });
}
