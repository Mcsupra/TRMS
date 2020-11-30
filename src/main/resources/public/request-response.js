window.onload = function () {
    AJAX();
}

function AJAX(){
    
    //AJAX - Asynchronous JavaScript and XML
    //Initialize xhr object
    let xhr = new XMLHttpRequest();
    const url = "http://localhost:9091/request-response.html/";
    //sets up ready state handler
    xhr.onreadystatechange = function () {
        switch (xhr.readyState) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                if (xhr.status === 200) {
                    let myRequest = JSON.parse(xhr.responseText);
                };
                break;
        }
    }
    //opens up the request
    xhr.open("POST", url, true);
    //sends request
    xhr.send();
}