<?php

try {
    require "../dbConnect.php";

    $name = $_POST['name'];

    $stmt = $conn->prepare('INSERT INTO CATEGORIES values (:name)');

    $stmt->bindParam(':name', $name);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
