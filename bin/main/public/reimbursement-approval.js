window.onload = function () {
    AJAX();
} 

let AJAX = function () {
    //AJAX - Asynchronous JavaScript and XML
    //Initialize xhr object
    let xhr = new XMLHttpRequest();
    const url = window.location.pathname;
    console.log(url);
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
                        console.log(myRequest);
                        document.getElementById("thisReqHeader").innerHTML = myRequest.reqId;
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
    let reimburIdcol = document.createElement("td");
    let statusCol = document.createElement("td");
    let projAmountCol = document.createElement("td");
    let actualAmountCol = document.createElement("td");
    let reqIdCol = document.createElement("td");
    
    tableRow.appendChild(reimburIdcol);
    tableRow.appendChild(statusCol);
    tableRow.appendChild(projAmountCol);
    tableRow.appendChild(actualAmountCol);
    tableRow.appendChild(reqIdCol);
    table.appendChild(tableRow);
    
    reimburIdcol.innerHTML = myRequest.reimburId;
    statusCol.innerHTML = myRequest.status;
    projAmountCol.innerHTML = "$"+myRequest.projectedAmount;
    if (myRequest.status == 'PENDING')
        actualAmountCol.innerHTML = '<form action="/update-reimbursement" method="POST">'
        +'<input name="reqId" id="reqId"class="hide"value='+myRequest.reqId+'></input>'
        +'<input name="actualAmount" id="actualAmount" placeholder="actualAmount"></input>'
        +'<button type="submit">Final Approval</button>'
    else
        actualAmountCol.innerHTML = myRequest.actualAmount;

    reqIdCol.innerHTML = myRequest.reqId;
 
    reimburIdcol.className = "table-style";
    statusCol.className = "table-style";
    projAmountCol.className = "table-style";
    actualAmountCol.className = "table-style";
    reqIdCol.className = "table-style";
    tableRow.className = "table-style";
}