<?php
require "../dbConnect.php";

try {
    $order_id_new = $_POST["order_id_new"];
    $order_id_old = $_POST["order_id_old"];

    // Update order_items
    $updateStmt = $conn->prepare("UPDATE order_items SET order_id = :order_id_new WHERE order_id = :order_id_old");
    $updateStmt->bindParam(':order_id_new', $order_id_new);
    $updateStmt->bindParam(':order_id_old', $order_id_old);
    $updateStmt->execute();

    // Delete from orders
    $deleteStmt = $conn->prepare("DELETE FROM orders WHERE id = :order_id_old");
    $deleteStmt->bindParam(':order_id_old', $order_id_old);
    $deleteStmt->execute();

    echo "Update and delete operations completed successfully.";
} catch (PDOException $pe) {
    die("Error: " . $pe->getMessage());
}
?>