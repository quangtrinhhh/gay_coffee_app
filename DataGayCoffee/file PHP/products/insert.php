<?php

try {
    require "../dbConnect.php";

    $name = $_POST['name'];
    $price = $_POST['price'];
    $quanity = $_POST['quanity'];
    $categories_id = $_POST['categories_id'];

    $stmt = $conn->prepare('INSERT INTO PRODUCTS values (:name,:price,:quanity,:categories_id)');

    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':price', $price);
    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':categories_id', $categories_id);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
