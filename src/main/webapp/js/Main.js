document.addEventListener("DOMContentLoaded", () => {
    const fadeElements = document.querySelectorAll(".fade-in");

    fadeElements.forEach(el => {
        el.style.opacity = 0;
        el.style.transition = "opacity 0.8s ease-in-out";
        setTimeout(() => {
            el.style.opacity = 1;
        }, 100);
    });
});
