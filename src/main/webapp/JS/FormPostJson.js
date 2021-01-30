let urlPostJsonToDo = "http://localhost:8080/todolist"

async function postToDo(url) {
    let data = {}
    let valueDescription = document.querySelector('.form-post-json>input[name="description"]')
    data.description = valueDescription.value
    let response = await fetch(url, {
        method: 'POST',
        mode: 'no-cors',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return await response
}