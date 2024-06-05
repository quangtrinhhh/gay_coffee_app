<?php

try {
    require "../dbConnect.php";

    $table_name = $_POST['table_name'];
    $status = 0;
    $user_id = $_POST['user_id'];
    $table_price = 0;
    $shop_id = $_POST['shop_id'];

    $stmt = $conn->prepare('INSERT INTO tables (table_name, status, user_id, table_price, shop_id) 
                            VALUES (:table_name, :status, :user_id, :table_price, :shop_id)');

    $stmt->bindParam(':table_name', $table_name);
    $stmt->bindParam(':status', $status);
    $stmt->bindParam(':user_id', $user_id);
    $stmt->bindParam(':table_price', $table_price);
    $stmt->bindParam(':shop_id', $shop_id);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
?>