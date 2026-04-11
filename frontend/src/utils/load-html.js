function renderLoadError(message) {
  const app = document.getElementById("app");

  if (app) {
    app.innerHTML = `<p>${message}</p>`;
  }
}

export async function loadHtmlIntoApp(path, errorMessage = "Error loading page") {
  try {
    const response = await fetch(new URL(path, import.meta.url));

    if (!response.ok) {
      throw new Error(`Failed to load ${path}: ${response.status}`);
    }

    const html = await response.text();
    const parsed = new DOMParser().parseFromString(html, "text/html");
    const app = document.getElementById("app");

    if (!app) {
      throw new Error("Missing #app container");
    }

    document.title = parsed.title || document.title;
    app.innerHTML = parsed.body.innerHTML;

    return parsed;
  } catch (error) {
    console.error(errorMessage, error);
    renderLoadError(errorMessage);
    return null;
  }
}
