window.onload = function () {
    AJAX();

}

function AJAX(){
    
    //AJAX - Asynchronous JavaScript and XML
    //Initialize xhr object
    let xhr = new XMLHttpRequest();
    const url = window.location.pathname;
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
                //logic to add guest to table
                if (xhr.status === 200) {
                    let myRequest = JSON.parse(xhr.responseText);
                    myRequest.forEach(element => {
                        console.log(myRequest);
                        addRow(element);
                    });
                }
                break;
        }
    }
    //opens up the request
    xhr.open("POST", url, true);
    //sends request
    xhr.send();
}

let addRow = function (myRequest) {
    let table = document.getElementById("request-table");
    let tableRow = document.createElement("tr");
    let fromEmpCol = document.createElement("td");
    let toEmpCol = document.createElement("td");
    let messageCol = document.createElement("td");


    tableRow.appendChild(fromEmpCol);
    tableRow.appendChild(toEmpCol);
    tableRow.appendChild(messageCol);
    table.appendChild(tableRow);

    fromEmpCol.innerHTML = myRequest.fromEmp;
    toEmpCol.innerHTML = myRequest.toEmp;
    messageCol.innerHTML = myRequest.message;

    fromEmpCol.className = "table-style";
    toEmpCol.className = "table-style";
    messageCol.className = "table-style";
    tableRow.className = "table-style";
}