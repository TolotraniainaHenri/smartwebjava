// =================================================
// NAVBAR PRO - SMARTWEB AI
// - lien actif detecte automatiquement depuis l'URL
// - pastille coulissante au survol
// - effet de reduction + barre de progression au defilement
// - menu mobile (clic, Echap, clic exterieur)
// =================================================

(function () {

    const nav = document.getElementById("snav");

    if (!nav) {
        return;
    }

    const menu = nav.querySelector(".snav-menu");
    const burger = nav.querySelector(".snav-burger");
    const indicator = nav.querySelector(".snav-indicator");
    const links = Array.from(nav.querySelectorAll(".snav-link"));

    // ---------- 1. Lien actif ----------

    let page = window.location.pathname.split("/").pop().toLowerCase();
    page = page.replace(/\.(jsp|html?)$/, "");

    if (page === "") {
        page = "index";
    }

    const active = links.find(function (link) {
        return link.dataset.page === page;
    }) || null;

    if (active) {
        active.classList.add("is-active");
        active.setAttribute("aria-current", "page");
    }

    // ---------- 2. Pastille coulissante (desktop) ----------

    function moveIndicator(target) {

        if (!indicator || window.getComputedStyle(indicator).display === "none") {
            return;
        }

        if (!target) {
            indicator.style.opacity = "0";
            return;
        }

        indicator.style.width = target.offsetWidth + "px";
        indicator.style.transform = "translateX(" + target.offsetLeft + "px)";
        indicator.style.opacity = "1";
    }

    function placeInitialIndicator() {

        // positionnement initial sans animation
        indicator.style.transition = "none";
        moveIndicator(active);

        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                indicator.style.transition = "";
            });
        });
    }

    links.forEach(function (link) {
        link.addEventListener("mouseenter", function () { moveIndicator(link); });
        link.addEventListener("focus", function () { moveIndicator(link); });
    });

    menu.addEventListener("mouseleave", function () { moveIndicator(active); });
    menu.addEventListener("focusout", function () { moveIndicator(active); });

    // ---------- 3. Defilement ----------

    let ticking = false;

    function onScroll() {

        const scrollTop = window.scrollY || document.documentElement.scrollTop;
        const max = document.documentElement.scrollHeight - window.innerHeight;
        const progress = max > 0 ? Math.min(scrollTop / max, 1) : 0;

        nav.classList.toggle("is-scrolled", scrollTop > 10);
        nav.style.setProperty("--snav-progress", progress.toFixed(4));

        ticking = false;
    }

    window.addEventListener("scroll", function () {
        if (!ticking) {
            ticking = true;
            requestAnimationFrame(onScroll);
        }
    }, { passive: true });

    // ---------- 4. Menu mobile ----------

    function setMenu(open) {

        menu.classList.toggle("is-open", open);
        burger.classList.toggle("is-open", open);
        burger.setAttribute("aria-expanded", String(open));
        burger.setAttribute("aria-label", open ? "Fermer le menu" : "Ouvrir le menu");
    }

    burger.addEventListener("click", function (event) {
        event.stopPropagation();
        setMenu(!menu.classList.contains("is-open"));
    });

    links.forEach(function (link) {
        link.addEventListener("click", function () { setMenu(false); });
    });

    document.addEventListener("click", function (event) {
        if (!nav.contains(event.target)) {
            setMenu(false);
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            setMenu(false);
            burger.focus();
        }
    });

    window.addEventListener("resize", function () {
        if (window.innerWidth > 860) {
            setMenu(false);
        }
        moveIndicator(active);
    });

    // ---------- Initialisation ----------

    onScroll();
    placeInitialIndicator();

    if (document.fonts && document.fonts.ready) {
        document.fonts.ready.then(function () { moveIndicator(active); });
    }

    window.addEventListener("load", function () { moveIndicator(active); });

})();