<?php
// Load the XML file
$xml = simplexml_load_file("notes.xml");

// Check if the XML file was loaded successfully
if ($xml === false) {
    echo "Failed to load XML file.";
    exit;
}

// Display the title of the note-taking app
echo "<h1>My Notes</h1>";

// Loop through each note and display its details
foreach ($xml->note as $note) {
    echo "<div style='margin-bottom: 20px; padding: 10px; border: 1px solid #ccc;'>";
    echo "<h3>" . $note->title . "</h3>";
    echo "<p><strong>Content:</strong> " . $note->content . "</p>";
    echo "<p><strong>Created At:</strong> " . $note->timestamp . "</p>";
    echo "</div>";
}
