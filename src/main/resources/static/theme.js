function applyTheme(isDark) {
    const nav = document.getElementById('mainNav') || document.querySelector('nav.navbar');
    const btn = document.getElementById('themeToggle');

    if (nav) {
        nav.classList.toggle('navbar-dark', isDark);
        nav.classList.toggle('bg-dark', isDark);
        nav.classList.toggle('navbar-light', !isDark);
        nav.classList.toggle('bg-light', !isDark);
    }

    if (btn) {
        btn.classList.toggle('btn-outline-light', isDark);
        btn.classList.toggle('btn-outline-dark', !isDark);
        btn.innerHTML = isDark ? '<i class="bi bi-sun"></i>' : '<i class="bi bi-moon"></i>';
    }

    // Set the theme on the root element
    document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
}

// Rest of your functions remain the same
function toggleTheme() {
    const isDark = !document.body.classList.contains('dark-mode');
    document.body.classList.toggle('dark-mode', isDark);
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    applyTheme(isDark);
}

function loadTheme() {
    const saved = localStorage.getItem('theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const isDark = saved ? saved === 'dark' : prefersDark;

    if (isDark) {
        document.body.classList.add('dark-mode');
    }
    applyTheme(isDark);
}

document.addEventListener('DOMContentLoaded', loadTheme);