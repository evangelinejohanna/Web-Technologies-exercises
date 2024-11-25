<?php
// Database configuration
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "my_database";

// Create a connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check if the connection was successful
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} else {
    echo "Connected successfully to the database.";
}

// Close the connection when done
$conn->close();
