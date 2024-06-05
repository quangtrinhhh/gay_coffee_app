<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $quantity = $_POST['quantity'];
    $price = $_POST['price'];

    $stmt = $conn->prepare('UPDATE ORDER_ITEMS SET QUANTITY = :quantity, PRICE = :price WHERE ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':price', $price);

    $stmt->execute();

    echo "Update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database: " . $pe->getMessage());
}

?>