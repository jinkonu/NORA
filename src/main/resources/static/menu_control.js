const menuEvent = document.querySelectorAll(".menu_button");
for (let s of menuEvent) {
    s.addEventListener("click", e => {
        const current = e.currentTarget;
        if (current.value == "home") {
            //let urlParam = current.value;
            window.open('/', '_self');
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
            let urlParam = "bookmark/seen";
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "towatch") {
            let urlParam = "bookmark/toSee";
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "profile") {
            let urlParam = "member";
            //let memberId = document.getElementById('user_id').value;
            //window.open('/' + urlParam + '/' + memberId, '_self');
            window.open('/' + urlParam, '_self');
        }
        if (current.value == "settings") {
            let urlParam = "settings/password";
            //let memberId = document.getElementById('user_id').value;
            //window.open('/' + urlParam + '/' + memberId, '_self');
            window.open('/' + urlParam, '_self');
        }
    });
}