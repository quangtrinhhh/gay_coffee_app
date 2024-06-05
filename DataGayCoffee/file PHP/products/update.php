<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $quanity = $_POST['quanity'];
    $categories_id = $_POST['categories_id'];

    $stmt = $conn->prepare('update PRODUCTS set NAME = :name, PRICE = :price, QUANTITY = :quanity, CATEGORIES_ID = :categories_id, where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':price', $price);
    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':categories_id', $categories_id);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
