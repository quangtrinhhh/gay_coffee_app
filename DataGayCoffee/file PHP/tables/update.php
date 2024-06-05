<?php

try {

    require "../dbConnect.php";

    $id = $_POST['id'];
    $table_name = $_POST['table_name'];
    $status = $_POST['status'];
    $user_id = $_POST['user_id'];
    $table_price = $_POST['table_price'];

    $stmt = $conn->prepare('update TABLES set TABLE_NAME = :table_name, STATUS = :status,USER_ID = :user_id , TABLE_PRICE= :table_price where ID = :id');

    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':table_name', $table_name);
    $stmt->bindParam(':status', $status);
    $stmt->bindParam(':user_id', $user_id);
    $stmt->bindParam(':table_price', $table_price);

    $stmt->execute();

    echo "update data successful";
} catch (PDOException $pe) {
    die("Could not update data to the database $dbname :" . $pe->getMessage());
}
