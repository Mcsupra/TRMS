window.onload = function () {
    AJAX();

}

function AJAX(){
    
    //AJAX - Asynchronous JavaScript and XML
    //Initialize xhr object
    let xhr = new XMLHttpRequest();
    const url = "view-employee.html";
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
                        addRow(myRequest);
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
    let empIdCol = document.createElement("td");
    let firstNameCol = document.createElement("td");
    let lastNameCol = document.createElement("td");
    let titleCol = document.createElement("td");
    let balanceCol = document.createElement("td");
    let departmentCol = document.createElement("td");
    
    tableRow.appendChild(empIdCol);
    tableRow.appendChild(firstNameCol);
    tableRow.appendChild(lastNameCol);
    tableRow.appendChild(titleCol);
    tableRow.appendChild(balanceCol);
    tableRow.appendChild(departmentCol);
    table.appendChild(tableRow);
    
    empIdCol.innerHTML = myRequest.empId;
    firstNameCol.innerHTML = myRequest.firstName;
    lastNameCol.innerHTML = myRequest.lastName;
    titleCol.innerHTML = myRequest.title;
    balanceCol.innerHTML = "$"+myRequest.balance;
    departmentCol.innerHTML = myRequest.department;
 
    empIdCol.className = "table-style";
    firstNameCol.className = "table-style";
    lastNameCol.className = "table-style";
    titleCol.className = "table-style";
    balanceCol.className = "table-style";
    departmentCol.className = "table-style";
    tableRow.className = "table-style";
}