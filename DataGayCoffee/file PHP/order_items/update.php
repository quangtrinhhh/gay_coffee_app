<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $quantity = $_POST['quantity'];
    $price = $_POST['price'];
    $order_id = $_POST['order_id'];
    $product_id = $_POST['product_id'];

    $stmt = $conn->prepare('update ORDER_ITEMS set QUANTITY = :quantity, PRICE = :price, ORDER_ID = :order_id, PRODUCT_ID = :product_id, where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':price', $price);
    $stmt->bindParam(':order_id', $order_id);
    $stmt->bindParam(':product_id', $product_id);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
