<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $status = $_POST['status'];
    $table_price = $_POST['table_price'];

    $stmt = $conn->prepare('update TABLES set STATUS = :status, TABLE_PRICE = :table_price where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':status', $status);
    $stmt->bindParam(':table_price', $table_price);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
