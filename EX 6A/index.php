<?php

include 'connect.php';

// Handle form submission for creating a new user
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['username'])) {
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT); // Hash the password for security

    // Insert data into the database
    $sql = "INSERT INTO users (username, email, password) VALUES ('$username', '$email', '$password')";

    if ($conn->query($sql) === TRUE) {
        echo "New user created successfully!<br>";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}

// Fetch data from the database
$sql = "SELECT id, username, email FROM users";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "ID: " . $row["id"] . " - Name: " . $row["username"] . " - Email: " . $row["email"] . "<br>";
    }
} else {
    echo "No users found.<br>";
}

?>

<form method="post">
    <input type="text" name="username" placeholder="Username" required><br>
    <input type="email" name="email" placeholder="Email" required><br>
    <input type="password" name="password" placeholder="Password" required><br>
    <input type="submit" value="Create User">
</form>

<?php
// Close the connection after all operations are complete
$conn->close();
?>