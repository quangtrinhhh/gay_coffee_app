<?php

try {
    require "../dbConnect.php";

    $table_name = $_POST['table_name'];
    $status = $_POST['status'];
    $user_id = $_POST['user_id'];
    $table_price = $_POST['table_price'];

    $stmt = $conn->prepare('INSERT INTO TABLES values (:table_name,:status,:user_id,:table_price)');

    $stmt->bindParam(':table_name', $table_name);
    $stmt->bindParam(':status', $status);
    $stmt->bindParam(':user_id', $user_id);
    $stmt->bindParam(':table_price', $table_price);

    $stmt->execute();

    echo "insert data successful";
} catch (PDOException $pe) {
    die("Could not insert data to the database $dbname :" . $pe->getMessage());
}
