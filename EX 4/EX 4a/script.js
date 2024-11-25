document.getElementById('todoForm').addEventListener('submit', function(event) {
    event.preventDefault(); 

    const taskTitle = document.getElementById('taskTitle').value.trim();
    const taskDescription = document.getElementById('taskDescription').value.trim();
    const taskPriority = document.getElementById('taskPriority').value;

    
    if (!taskTitle) {
        alert("Please enter a task title.");
        return;
    }

   
    const task = {
        title: taskTitle,
        description: taskDescription || "No description provided",
        priority: taskPriority,
        completed: false,
    };

    
    addTaskToList(task);
    document.getElementById('todoForm').reset();
});

function addTaskToList(task) {
    const taskList = document.getElementById('taskList');
    
    
    const li = document.createElement('li');
    li.classList.add(task.priority);  

    
    li.innerHTML = `
        <strong>${task.title}</strong> (${task.priority} priority)<br>
        <span>${task.description}</span><br>
        <button onclick="toggleCompletion(this)">Mark as Completed</button>
    `;

    
    taskList.appendChild(li);
}

function toggleCompletion(button) {
    const li = button.parentElement;
    
    
    if (button.textContent.includes('Completed')) {
        button.textContent = 'Mark as Incomplete';
        li.style.textDecoration = 'line-through';  // Mark as completed (strikethrough)
    } else {
        button.textContent = 'Mark as Completed';
        li.style.textDecoration = 'none';  // Remove strikethrough
    }
}
