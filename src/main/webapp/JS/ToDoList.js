document.querySelector('.form-post-json>button[class="post"]')
    .addEventListener("click", async function (event) {
        await postToDo(urlPostJsonToDo)
        await visionCheckList()
        console.log("click")
    });

window.addEventListener("load", async function (event) {
    await visionCheckList()
    console.log("load")
});

document.querySelector('.check-list-element>input[type="checkbox"]')
    .addEventListener("click", async function (event) {
        await visionCheckList()
        console.log("click")
    });