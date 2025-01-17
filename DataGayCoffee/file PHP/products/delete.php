<?php

try {
    require "../dbConnect.php";

    $id = $_POST['id'];

    $stmt = $conn->prepare('DELETE FROM PRODUCTS WHERE ID = :id');

    $stmt->bindParam(':id', $id);

    $stmt->execute();

    echo "Delete data successful";
} catch (PDOException $pe) {
    die("Could not delete data from the database $dbname: " . $pe->getMessage());
}
