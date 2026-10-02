// Shared script for login, register, and todos pages
const SERVER_URL = "http://localhost:8081";
let token = localStorage.getItem("token");
const urlParams = new URLSearchParams(window.location.search);
if (urlParams.has('token')) {
    token = urlParams.get('token');
    localStorage.setItem("token", token);
    window.history.replaceState({}, document.title, window.location.pathname);
}
// Login page logic
function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    })
        .then(async (response) => {
            const text = await response.text();
            if (!response.ok) {
                // backend returns plain text error like "User Not Registerd"
                throw new Error(text || "Login failed");
            }
            // success response is JSON: {"token": "..."}
            return JSON.parse(text);
        })
        .then(data => {
            localStorage.setItem("token", data.token);
            token = data.token;
            window.location.href = "todos.html";
        })
        .catch(error => {
            alert(error.message);
        });
}

// Register page logic
function register() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    })
        .then(async (response) => {
            const text = await response.text();
            if (!response.ok) {
                // backend returns plain text error like "Email allready present"
                throw new Error(text || "Registration Failed");
            }
            alert("Registration Successful! Please Login");
            window.location.href = "login.html";
        })
        .catch(error => {
            alert(error.message);
        });
}

// Render single todo card (Includes Description)
function createTodoCard(todo) {
    const card = document.createElement("div");
    card.className = "todo-card";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.className = "todo-checkbox"; // Apply CSS class for styling
    checkbox.checked = todo.completed;
    checkbox.addEventListener("change", function () {
        const updatedTodo = { ...todo, completed: checkbox.checked };
        updateTodoStatus(updatedTodo);
    });

    const contentDiv = document.createElement("div");
    contentDiv.className = "todo-content";

    const titleSpan = document.createElement("span");
    titleSpan.className = "todo-title";
    titleSpan.textContent = todo.title;

    if (todo.completed) {
        titleSpan.style.textDecoration = "line-through";
        titleSpan.style.color = "#aaa";
    }

    contentDiv.appendChild(titleSpan);

    // Display description if present
    if (todo.description) {
        const descP = document.createElement("p");
        descP.className = "todo-description";
        descP.textContent = todo.description;
        if (todo.completed) descP.style.color = "#aaa";
        contentDiv.appendChild(descP);
    }

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "X";
    deleteBtn.onclick = function () { deleteTodo(todo.id); };

    card.appendChild(checkbox);
    card.appendChild(contentDiv);
    card.appendChild(deleteBtn);

    return card;
}

// Load all todos
function loadTodos() {
    if (!token) {
        alert("Please Login First");
        window.location.href = "login.html";
        return;
    }

    fetch(`${SERVER_URL}/getTodos`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(async (response) => {
            const data = await response.json().catch(() => null);
            if (!response.ok) {
                throw new Error((data && data.message) || "Failed to get todos");
            }
            return data;
        })
        .then((todos) => {
            const todoList = document.getElementById("todo-list");
            todoList.innerHTML = "";

            if (!todos || todos.length === 0) {
                todoList.innerHTML = `<p id="empty-message">No Todos yet. Add one below!</p>`;
            } else {
                todos.forEach(todo => {
                    todoList.appendChild(createTodoCard(todo));
                });
            }
        })
        .catch(error => {
            alert(error.message);
            const todoList = document.getElementById("todo-list");
            if (todoList) {
                todoList.innerHTML = `<p style="color: red">Failed to load Todos!</p>`;
            }
        });
}

// Add new todo (Title + Description)
function addTodo() {
    if (!token) {
        alert("Please Login First");
        window.location.href = "login.html";
        return;
    }

    const titleInput = document.getElementById("todoInput");
    // Looks for description input by id 'todoDescription' or 'descriptionInput'
    const descInput = document.getElementById("todoDescription") || document.getElementById("descriptionInput");

    const title = titleInput ? titleInput.value.trim() : "";
    const description = descInput ? descInput.value.trim() : "";

    if (!title) {
        alert("Please enter a title");
        return;
    }

    fetch(`${SERVER_URL}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            title: title,
            description: description,
            completed: false
        })
    })
        .then(async (response) => {
            const data = await response.json().catch(() => ({}));
            if (!response.ok) {
                throw new Error(data.message || "Failed to add todo");
            }
            return data;
        })
        .then(() => {
            if (titleInput) titleInput.value = "";
            if (descInput) descInput.value = "";
            loadTodos();
        })
        .catch(error => {
            alert(error.message);
        });
}

// Update todo status
function updateTodoStatus(todo) {
    fetch(`${SERVER_URL}/update`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(todo)
    })
        .then(async (response) => {
            if (!response.ok) {
                const data = await response.json().catch(() => ({}));
                throw new Error(data.message || "Failed to update todo");
            }
        })
        .then(() => loadTodos())
        .catch(error => alert(error.message));
}

// Delete todo
function deleteTodo(id) {
    fetch(`${SERVER_URL}/delete/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(async (response) => {
            if (!response.ok) {
                const data = await response.json().catch(() => ({}));
                throw new Error(data.message || "Failed to delete todo");
            }
        })
        .then(() => loadTodos())
        .catch(error => alert(error.message));
}

// Page-specific initializations
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("todo-list")) {
        loadTodos();
    }
});