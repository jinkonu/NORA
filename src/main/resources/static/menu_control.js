const menuEvent = document.querySelectorAll(".menu_button");
for (let s of menuEvent) {
    s.addEventListener("click", e => {
        const current = e.currentTarget;
        if (current.value == "search") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
    });
}