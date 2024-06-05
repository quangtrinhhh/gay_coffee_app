<?php

try {
    require "../dbConnect.php";

    $quantity = $_POST['quantity'];
    $total_price = $_POST['total_price'];
    $product_id = $_POST['product_id'];
    $bill_id = $_POST['bill_id'];

    $stmt = $conn->prepare('INSERT INTO BILL_ITEMS (`quantity`, `total_price`, `product_id`, `bill_id`) VALUES (:quantity, :total_price, :product_id, :bill_id)');

    $stmt->bindParam(':quantity', $quantity);
    $stmt->bindParam(':total_price', $total_price);
    $stmt->bindParam(':product_id', $product_id);
    $stmt->bindParam(':bill_id', $bill_id);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
