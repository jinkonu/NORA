const menuEvent = document.querySelectorAll(".menu_button");
for (let s of menuEvent) {
    s.addEventListener("click", e => {
        const current = e.currentTarget;
        if (current.value == "home") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "notification") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "search") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "watched") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "towatch") {
            let urlParam = current.value;
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "profile") {
            let urlParam = "member";
            window.open('/' + urlParam, '_self');
        }
    });
}