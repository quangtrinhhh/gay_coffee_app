<?php
require "../dbConnect.php";

try {
    $table_name = $_POST["table_name"];
    $table_id = $_POST["table_id"];



    $updateStmt = $conn->prepare("UPDATE tables
    SET table_name = :table_name
    WHERE id = :table_id;");
    $updateStmt->bindParam(':table_name', $table_name);
    $updateStmt->bindParam(':table_id', $table_id);
    $updateStmt->execute();


    echo "Update completed successfully.";
} catch (PDOException $pe) {
    die("Error: " . $pe->getMessage());
}
?>