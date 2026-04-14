const BASE_URL = "http://localhost:8080/api";

function formatErrorMessage(path, status, errorBody) {
    if (!errorBody) {
        return `Request failed (${status}) for ${path}`;
    }

    const fieldErrors = errorBody.fieldErrors && typeof errorBody.fieldErrors === "object"
        ? Object.entries(errorBody.fieldErrors)
            .map(([field, message]) => `${field}: ${message}`)
            .join("\n")
        : "";

    const baseMessage = errorBody.message || errorBody.error || `Request failed (${status})`;

    return fieldErrors ? `${baseMessage}\n${fieldErrors}` : baseMessage;
}

export async function request(path, options = {}) {
    try{
        const headers = {
            "Content-Type": "application/json",
            ...(options.headers || {}),
        };

        const response = await fetch(BASE_URL + path, {
            ...options,
            headers,
        });

        // Handle non-OK responses
        if (!response.ok) {
            let errorMessage = `Request failed (${response.status}) for ${path}`;

            try {
                const errorBody = await response.json();
                errorMessage = formatErrorMessage(path, response.status, errorBody);
            } catch {
                errorMessage = await response.text() || errorMessage;
            }

            throw new Error(errorMessage);
        }

        //Parse JSON response
        return await response.json();
    } catch (error) {
        console.error("API Error: ", error.message);
        throw error;
    }
}
