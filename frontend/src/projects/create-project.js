import { navigate } from "../router/router.js";
import { createProject } from "../services/projectService.js";
import { loadHtmlIntoApp } from "../utils/load-html.js";
import { getCurrentUser } from "../utils/storage.js";

function parseTags(value) {
    return value
        .split(",")
        .map((item) => item.trim())
        .filter(Boolean);
}

export function loadCreateProject() {
    loadHtmlIntoApp("../projects/create-project.html", "Error loading create-project page")
        .then((page) => {
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

            const form = document.getElementById("createProjectForm");

            form?.addEventListener("submit", async (event) => {
                event.preventDefault();

                try {
                    const project = await createProject({
                        userId: user.userId,
                        name: form.name.value.trim(),
                        description: form.description.value.trim(),
                        stage: form.stage.value.trim(),
                        githubUrl: form.githubUrl.value.trim(),
                        liveUrl: form.liveUrl.value.trim(),
                        supportRequired: form.supportRequired.value.trim(),
                        imageUrl: form.imageUrl.value.trim(),
                        stack: parseTags(form.stack.value),
                    });

                    navigate(`/projects/view?id=${project.projectId}`);
                } catch (error) {
                    alert(error.message || "Unable to create project");
                }
            });
        });
}
