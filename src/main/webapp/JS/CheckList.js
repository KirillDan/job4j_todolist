async function getAll() {
    const authStorage = window.localStorage;
    let authToken = authStorage.getItem('authorization')
    console.log(authToken)
    const urlItemRepository = 'http://localhost:8080/todolist/itemRestRepository'
    let response = await fetch(urlItemRepository, {
        method: 'GET',
        mode: 'no-cors',
        headers: {
            'authorization': authToken
        }
    })    
    return await response.json()
}


async function setDone(id) {
    const authStorage = window.localStorage;
    let urlSetDone = 'http://localhost:8080/todolist/itemRestRepository/setDone/'
    urlSetDone = urlSetDone + id
    let response = await fetch(urlSetDone, {
        method: 'GET',
        mode: 'no-cors',
        headers: {
            'authorization': authStorage.getItem('authorization')
        }
    })
    return await response
}

async function setNotDone(id) {
    const authStorage = window.localStorage;
    let urlSetNotDone = 'http://localhost:8080/todolist/itemRestRepository/setNotDone/'
    urlSetNotDone = urlSetNotDone + id
    let response = await fetch(urlSetNotDone, {
        method: 'GET',
        mode: 'no-cors',
        headers: {
            'authorization': authStorage.getItem('authorization')
        }
    })
    return await response
}

let visionCheckList = async function () {
    let allData = await getAll()
    
    let checkboxShowNotDone = document.querySelector('.check-list-element>input[type="checkbox"]')
    if (checkboxShowNotDone.checked) {
        allData = allData.filter(el => el.done == false)
    }
    document.querySelectorAll('.check-list-element>table>tbody').forEach(i => i.innerHTML = '')
    let element = document.querySelector('.check-list-element>table>tbody')
    allData.forEach(dataRow => {
        let arrayRow = Object.entries(dataRow)
        let tr = document.createElement("tr")
        let tdInputCheck = document.createElement("td")
        tr.appendChild(tdInputCheck)
        let tdDesc = document.createElement("td")
        tr.appendChild(tdDesc)
        let tdCreated = document.createElement("td")
        tr.appendChild(tdCreated)
        let tdAuthor = document.createElement("td")
        tr.appendChild(tdAuthor)
        let inputElement = document.createElement("input")
        inputElement.type = "checkbox"
        inputElement.addEventListener("click", async function(event) {
            let id = dataRow["id"]
            if (event.target.checked) {
                await setDone(id)
            } else {
                await setNotDone(id)
            }
        })
        tdInputCheck.appendChild(inputElement)
        arrayRow.forEach(([key, value]) => {
            if (key == "done") {
                inputElement.checked = value
            }
            if (key == "description") {
                tdDesc.textContent = value
            }
            if (key == "created") {
                let timeValue = new String(value)
                timeValue = timeValue.substring(0, timeValue.indexOf("["))
                timeValue = new Date(timeValue)
                tdCreated.textContent = timeValue.toLocaleDateString()
            }
            if (key == "user") {
                tdAuthor.textContent = value.name
            }
        });
        element.appendChild(tr)
    });
}