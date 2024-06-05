<?php

try {
    require "../dbConnect.php";

    $quantity = $_POST['quantity'];
    $price = $_POST['price'];
    $order_id = $_POST['order_id'];
    $product_id = $_POST['product_id'];

    $stmt = $conn->prepare('INSERT INTO ORDER_ITEMS (quantity,price,order_id,product_id) values (:quantity,:price,:order_id,:product_id)');

    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':price', $price);
    $stmt->bindParam(':order_id', $order_id);
    $stmt->bindParam(':product_id', $product_id);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
