let urlPostJsonToDo = "http://localhost:8080/todolist/itemRestRepository"

async function postToDo(url) {
    const authStorage = window.localStorage;
    let data = {}
    let valueDescription = document.querySelector('.form-post-json>input[name="description"]')
    data.description = valueDescription.value
    data.user = {}
    data.user.id = 2
    let response = await fetch(url, {
        method: 'POST',
        mode: 'no-cors',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
            'authorization': authStorage.getItem('authorization')
        }
    });
    return await response
}