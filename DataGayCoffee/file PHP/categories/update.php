<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];

    $name = $_POST['name'];


    $stmt = $conn->prepare('update CATEGORIES set NAME = :name where ID = :id');

    $stmt->bindParam(':name', $name);

    $stmt->bindParam(':id', $id);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
