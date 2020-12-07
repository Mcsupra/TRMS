window.onload = function () {
    AJAX();

}

function AJAX(){
    
    //AJAX - Asynchronous JavaScript and XML
    //Initialize xhr object
    let xhr = new XMLHttpRequest();
    const url = "view-all-requests.html";
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
    let reqIdCol = document.createElement("td");
    let reqDateCol = document.createElement("td");
    let eventDateCol = document.createElement("td");
    let eventTimeCol = document.createElement("td");
    let eventLocCol = document.createElement("td");
    let eventTypeCol = document.createElement("td");
    let eventCostCol = document.createElement("td");
    let supervisor = document.createElement("td");
    let dept_head = document.createElement("td");
    let benco = document.createElement("td");
    let reqStatusCol = document.createElement("td");
    let addDocsCol = document.createElement("td");
    let empIdCol = document.createElement("td");

    tableRow.appendChild(reqIdCol);
    tableRow.appendChild(reqDateCol);
    tableRow.appendChild(eventDateCol);
    tableRow.appendChild(eventTimeCol);
    tableRow.appendChild(eventLocCol);
    tableRow.appendChild(eventTypeCol);
    tableRow.appendChild(eventCostCol);
    tableRow.appendChild(supervisor);
    tableRow.appendChild(dept_head);
    tableRow.appendChild(benco);
    tableRow.appendChild(reqStatusCol);
    tableRow.appendChild(addDocsCol);
    tableRow.appendChild(empIdCol);
    table.appendChild(tableRow);

    reqIdCol.innerHTML = myRequest.reqId;
    reqDateCol.innerHTML = myRequest.requestDate;
    eventDateCol.innerHTML = myRequest.eventDate;
    eventTimeCol.innerHTML = myRequest.eventTime;
    eventLocCol.innerHTML = myRequest.eventLoc;
    eventTypeCol.innerHTML = myRequest.eventType;
    eventCostCol.innerHTML = "$"+myRequest.event_cost;
    supervisor.innerHTML = myRequest.supervisor; 
    dept_head.innerHTML = myRequest.deptHead;

    if (myRequest.currentStatus == 'DENIED'){
        benco.innerHTML = 'false';
    }else if(myRequest.supervisor == true && myRequest.deptHead == true && myRequest.benco == true){
        benco.innerHTML = 'true'
        +'<form action="reimbursement-approval" method="GET">'
        +'<input name="reqId" id="reqId" value='+myRequest.reqId+'></input>'
        +'<button type="submit">Go to Reimbursement</button>';
    }  
    else if (myRequest.supervisor == true && myRequest.deptHead == true){
        benco.innerHTML = 
        '<form action="approve-request" method="POST">'
            +'<input name="reqId" id="reqId" value='+myRequest.reqId+'></input>'
            +'<input name="title" id="title" value="BENCO"></input>'
            +'<label for="approval">Approval:</label>'
                +'<select id="approval" name="approval">'
                    +'<option value="APPROVE">Approve</option>'
                    +'<option value="DENY">Deny</option>'
            +'</select>'
            +'<button type="submit">Submit</button>'
    }else{
        benco.innerHTML = myRequest.benco;
    }
    
    reqStatusCol.innerHTML = myRequest.currentStatus;
    addDocsCol.innerHTML = myRequest.additional_docs;
    empIdCol.innerHTML = myRequest.empId;

    reqIdCol.className = "table-style";
    reqDateCol.className = "table-style";
    eventDateCol.className = "table-style";
    eventTimeCol.className = "table-style";
    eventLocCol.className = "table-style";
    eventTypeCol.className = "table-style";
    eventCostCol.className = "table-style";
    supervisor.className = "table-style";
    dept_head.className = "table-style";
    benco.className = "table-style";
    reqStatusCol.className = "table-style";
    addDocsCol.className = "table-style";
    empIdCol.className = "table-style";
    tableRow.className = "table-style";
}