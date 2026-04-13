const BASE_URL = "http://localhost:8080/api";

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
            let errorMessage = "Request failed";

            try {
                const errorBody = await response.json();
                errorMessage = errorBody.message || errorBody.error || errorMessage;
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
