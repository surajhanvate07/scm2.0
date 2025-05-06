// Get current theme from local storage or default to "light"
let currentTheme = getTheme();

// Initialize theme and button functionality
initThemeToggle();

function initThemeToggle() {
	const htmlElement = document.querySelector("html");
	const themeToggleButton = document.querySelector("#theme_change_button");
	const buttonLabel = themeToggleButton.querySelector("span");

	// Apply the current theme to the page
	htmlElement.classList.add(currentTheme);

	// Set initial button text
	updateButtonText();

	// Handle theme toggle on button click
	themeToggleButton.addEventListener("click", () => {
		const previousTheme = currentTheme;
		currentTheme = currentTheme === "light" ? "dark" : "light";

		// Update HTML class and local storage
		htmlElement.classList.replace(previousTheme, currentTheme);
		setTheme(currentTheme);

		// Update button label
		updateButtonText();
	});

	// Update button text based on current theme
	function updateButtonText() {
		buttonLabel.textContent = currentTheme === "light" ? "Dark" : "Light";
	}
}

// Save theme to local storage
function setTheme(theme) {
	localStorage.setItem("theme", theme);
}

// Retrieve theme from local storage or return default
function getTheme() {
	return localStorage.getItem("theme") || "light";
}
